/** */
package com.strandls.userGroup;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Recoverable;
import com.rabbitmq.client.RecoveryListener;

/**
 * Owns the single, long-lived RabbitMQ {@link Connection} for this
 * application. Callers should not ask this class for a {@link Channel} to
 * hold on to; instead get the connection via {@link #connect()} and obtain
 * channels through {@link RabbitChannelProvider}, which hands out one channel
 * per thread as RabbitMQ's client requires.
 *
 * @author Abhishek Rudra
 */
public class RabbitMqConnection {

	private static final Logger logger = LoggerFactory.getLogger(RabbitMqConnection.class);
	public static final String EXCHANGE_BIODIV = "biodiv";
	private static final String OBSERVATION_QUEUE = "observationQueue";
	private static final String ROUTING_OBSERVATION = "observation";

	public static final String MAILING_QUEUE;
	public static final String MAILING_ROUTINGKEY;

	private static final String RABBITMQ_HOST;
	private static final Integer RABBITMQ_PORT;
	private static final String RABBITMQ_USERNAME;
	private static final String RABBITMQ_PASSWORD;

	private static final int MAX_CONNECT_ATTEMPTS = 5;
	private static final long INITIAL_BACKOFF_MILLIS = 2000L;
	private static final long MAX_BACKOFF_MILLIS = 30000L;

	private static final AtomicInteger THREAD_COUNTER = new AtomicInteger();

	static {
		InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream("config.properties");

		Properties properties = new Properties();
		try {
			properties.load(in);
		} catch (IOException e) {
			logger.error(e.getMessage());
		}

		RABBITMQ_HOST = properties.getProperty("rabbitmq_host");
		RABBITMQ_PORT = Integer.parseInt(properties.getProperty("rabbitmq_port"));
		RABBITMQ_USERNAME = properties.getProperty("rabbitmq_username");
		RABBITMQ_PASSWORD = properties.getProperty("rabbitmq_password");

		MAILING_QUEUE = properties.getProperty("rabbitmq_queue");
		MAILING_ROUTINGKEY = properties.getProperty("rabbitmq_routingKey");
		try {
			in.close();
		} catch (IOException e) {
			logger.error(e.getMessage());
		}
	}

	/**
	 * Opens the single application-wide connection, retrying with backoff if
	 * the broker isn't reachable yet, and declares the exchange/queue
	 * topology once. The returned connection has automatic recovery enabled,
	 * so the connection and any open channels reconnect on their own if the
	 * network drops later - but topology recovery is deliberately left to us
	 * (see the recovery listener below) rather than the client's built-in
	 * mechanism, which ties recorded declarations to the specific channel
	 * that made them and cannot redeclare them once that channel is closed.
	 */
	public Connection connect() throws IOException, TimeoutException {

		ConnectionFactory factory = buildConnectionFactory();

		long backoff = INITIAL_BACKOFF_MILLIS;
		for (int attempt = 1; attempt <= MAX_CONNECT_ATTEMPTS; attempt++) {
			try {
				Connection connection = factory.newConnection("biodiv-usergroup");
				connection.addShutdownListener(cause -> {
					if (!cause.isInitiatedByApplication()) {
						logger.error("RabbitMQ connection to {}:{} closed unexpectedly: {}", factory.getHost(),
								factory.getPort(), cause.getMessage());
					}
				});
				if (connection instanceof Recoverable) {
					((Recoverable) connection).addRecoveryListener(new RecoveryListener() {
						@Override
						public void handleRecovery(Recoverable recoverable) {
							logger.info("RabbitMQ connection to {}:{} recovered; redeclaring topology",
									factory.getHost(), factory.getPort());
							try {
								declareTopology(connection);
								logger.info("RabbitMQ topology redeclared successfully after recovery");
							} catch (IOException e) {
								logger.error("Failed to redeclare RabbitMQ topology after recovery", e);
							}
						}

						@Override
						public void handleRecoveryStarted(Recoverable recoverable) {
							logger.warn("RabbitMQ connection to {}:{} attempting automatic recovery...",
									factory.getHost(), factory.getPort());
						}
					});
				}
				logger.info("Connected to RabbitMQ at {}:{} (attempt {}/{})", factory.getHost(), factory.getPort(),
						attempt, MAX_CONNECT_ATTEMPTS);
				declareTopology(connection);
				return connection;
			} catch (IOException | TimeoutException e) {
				if (attempt == MAX_CONNECT_ATTEMPTS) {
					logger.error("Could not connect to RabbitMQ at {}:{} after {} attempts", factory.getHost(),
							factory.getPort(), MAX_CONNECT_ATTEMPTS);
					throw e;
				}
				logger.warn("RabbitMQ connection attempt {}/{} failed ({}); retrying in {} ms", attempt,
						MAX_CONNECT_ATTEMPTS, e.getMessage(), backoff);
				sleep(backoff);
				backoff = Math.min(backoff * 2, MAX_BACKOFF_MILLIS);
			}
		}

		// Unreachable: the loop above always either returns or throws on the last attempt.
		throw new IOException("Failed to connect to RabbitMQ after " + MAX_CONNECT_ATTEMPTS + " attempts");
	}

	private ConnectionFactory buildConnectionFactory() {
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost(RABBITMQ_HOST);
		factory.setPort(RABBITMQ_PORT);
		factory.setUsername(RABBITMQ_USERNAME);
		factory.setPassword(RABBITMQ_PASSWORD);

		// Reconnect (and recover already-open channels) automatically if the
		// connection drops after startup. Topology recovery is deliberately
		// OFF: the client's built-in version ties recorded exchanges/queues/
		// bindings to the channel that declared them, and silently fails to
		// redeclare them if that channel was ever closed. We redeclare
		// topology ourselves via the recovery listener above instead.
		factory.setAutomaticRecoveryEnabled(true);
		factory.setTopologyRecoveryEnabled(false);
		factory.setNetworkRecoveryInterval(5000);
		factory.setConnectionTimeout(10000);
		factory.setRequestedHeartbeat(30);

		// Daemon + clearly named so a leaked thread (e.g. surviving a webapp
		// redeploy) never blocks JVM/Tomcat shutdown and is easy to spot in a
		// thread dump instead of showing up as an anonymous rabbitmq-client thread.
		factory.setThreadFactory(runnable -> {
			Thread thread = new Thread(runnable, "rabbitmq-usergroup-" + THREAD_COUNTER.incrementAndGet());
			thread.setDaemon(true);
			return thread;
		});

		return factory;
	}

	private void declareTopology(Connection connection) throws IOException {
		try (Channel setupChannel = connection.createChannel()) {
			setupChannel.exchangeDeclare(EXCHANGE_BIODIV, "direct");
			setupChannel.queueDeclare(OBSERVATION_QUEUE, false, false, false, null);
			setupChannel.queueBind(OBSERVATION_QUEUE, EXCHANGE_BIODIV, ROUTING_OBSERVATION);
			setupChannel.queueDeclare(MAILING_QUEUE, false, false, false, null);
			setupChannel.queueBind(MAILING_QUEUE, EXCHANGE_BIODIV, MAILING_ROUTINGKEY);
		} catch (TimeoutException e) {
			throw new IOException("Timed out declaring RabbitMQ topology", e);
		}
	}

	private void sleep(long millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
	}
}
