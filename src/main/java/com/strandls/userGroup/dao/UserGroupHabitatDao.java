/**
 * 
 */
package com.strandls.userGroup.dao;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.strandls.userGroup.pojo.UserGroupHabitat;
import com.strandls.userGroup.pojo.UserGroupSpeciesGroup;
import com.strandls.userGroup.util.AbstractDAO;

/**
 * @author Abhishek Rudra
 *
 */
public class UserGroupHabitatDao extends AbstractDAO<UserGroupHabitat, Long> {

	private final Logger logger = LoggerFactory.getLogger(UserGroupHabitatDao.class);

	/**
	 * @param sessionFactory
	 */
	@Inject
	protected UserGroupHabitatDao(SessionFactory sessionFactory) {
		super(sessionFactory);
	}

	@Override
	public UserGroupHabitat findById(Long id) {
		UserGroupHabitat result = null;
		Session session = sessionFactory.openSession();
		try {
			result = session.get(UserGroupHabitat.class, id);
		} catch (Exception e) {
			logger.error(e.getMessage());
		} finally {
			session.close();
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	public List<UserGroupHabitat> findByUserGroupId(Long userGroupId) {
		String qry = "from UserGroupHabitat where userGroupId = :ugId";
		Session session = sessionFactory.openSession();
		List<UserGroupHabitat> result = new ArrayList<UserGroupHabitat>();
		try {
			Query<UserGroupHabitat> query = session.createQuery(qry);
			query.setParameter("ugId", userGroupId);
			result = query.getResultList();
		} catch (Exception e) {
			logger.error(e.getMessage());
		} finally {
			session.close();
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	public void deleteHabitatsMappingByUgId(Long id) {
		Session session = sessionFactory.openSession();
		Transaction transaction = null;
		String qry = "delete from user_group_habitat where user_group_habitats_id = :id";
		try {
			transaction = session.beginTransaction();
			Query<UserGroupSpeciesGroup> query = session.createSQLQuery(qry);
			query.setParameter("id", id);
			query.executeUpdate();
			transaction.commit();
		} catch (Exception e) {
			if (transaction != null) {
				transaction.rollback();
			}
			logger.error(e.getMessage());
		} finally {
			session.close();
		}
	}

}
