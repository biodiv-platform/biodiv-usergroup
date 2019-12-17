/**
 * 
 */
package com.strandls.userGroup.dao;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.strandls.userGroup.pojo.UserGroupMemberRole;
import com.strandls.userGroup.util.AbstractDAO;

/**
 * @author Abhishek Rudra
 *
 */
public class UserGroupMemberRoleDao extends AbstractDAO<UserGroupMemberRole, Long> {

	private final Logger logger = LoggerFactory.getLogger(UserGroupMemberRole.class);

	InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream("config.properties");

	/**
	 * @param sessionFactory
	 */

	@Inject
	protected UserGroupMemberRoleDao(SessionFactory sessionFactory) {
		super(sessionFactory);
	}

	@Override
	public UserGroupMemberRole findById(Long id) {
		Session session = sessionFactory.openSession();
		UserGroupMemberRole entity = null;
		try {
			entity = session.get(UserGroupMemberRole.class, id);
		} catch (Exception e) {
			logger.error(e.getMessage());
		} finally {
			session.close();
		}
		return entity;
	}

	@SuppressWarnings("unchecked")
	public List<UserGroupMemberRole> getUserGroup(Long sUserId) {

		String qry = "from UserGroupMemberRole where sUserId = :sUserId";
		Session session = sessionFactory.openSession();
		List<UserGroupMemberRole> result = new ArrayList<UserGroupMemberRole>();
		try {
			Query<UserGroupMemberRole> query = session.createQuery(qry);
			query.setParameter("sUserId", sUserId);
			result = query.getResultList();
		} catch (Exception e) {
			logger.error(e.getMessage());
		} finally {
			session.close();
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	public List<UserGroupMemberRole> findUserGroupbyUserIdRole(Long userId) {

		Properties properties = new Properties();
		try {
			properties.load(in);
		} catch (IOException e) {
			e.printStackTrace();
		}
		String expert = properties.getProperty("userGroupExpert");
		String founder = properties.getProperty("userGroupFounder");

		String qry = "from UserGroupMemberRole where sUserId = :userId and roleId in ( " + founder + " , " + expert + ")";
		Session session = sessionFactory.openSession();
		List<UserGroupMemberRole> result = null;
		try {
			Query<UserGroupMemberRole> query = session.createQuery(qry);
			query.setParameter("userId", userId);

			result = query.getResultList();

		} catch (Exception e) {
			logger.error(e.getMessage());
		} finally {
			session.close();
		}
		return result;
	}

}
