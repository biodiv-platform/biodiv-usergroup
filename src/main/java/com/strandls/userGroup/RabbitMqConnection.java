/** */
package com.strandls.userGroup;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.concurrent.TimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
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

	public Channel setRabbitMQConnetion() throws IOException, TimeoutException {

		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost(RABBITMQ_HOST);
		factory.setPort(RABBITMQ_PORT);
		factory.setUsername(RABBITMQ_USERNAME);
		factory.setPassword(RABBITMQ_PASSWORD);
		Connection connection = factory.newConnection();
		Channel channel = connection.createChannel();
		channel.exchangeDeclare(EXCHANGE_BIODIV, "direct");
		channel.queueDeclare(OBSERVATION_QUEUE, false, false, false, null);
		channel.queueBind(OBSERVATION_QUEUE, EXCHANGE_BIODIV, ROUTING_OBSERVATION);
		channel.queueDeclare(MAILING_QUEUE, false, false, false, null);
		channel.queueBind(MAILING_QUEUE, EXCHANGE_BIODIV, MAILING_ROUTINGKEY);

		return channel;
	}
}
