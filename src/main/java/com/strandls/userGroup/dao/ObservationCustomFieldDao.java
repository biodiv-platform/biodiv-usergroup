/**
 * 
 */
package com.strandls.userGroup.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

import com.strandls.userGroup.pojo.ObservationCustomField;
import com.strandls.userGroup.pojo.UserGroupCustomFieldMapping;
import com.strandls.userGroup.util.AbstractDAO;

/**
 * @author Abhishek Rudra
 *
 */
public class ObservationCustomFieldDao extends AbstractDAO<ObservationCustomField, Long> {

	private final Logger logger = LoggerFactory.getLogger(ObservationCustomFieldDao.class);

	/**
	 * @param sessionFactory
	 */

	@Inject
	protected ObservationCustomFieldDao(SessionFactory sessionFactory) {
		super(sessionFactory);
	}

	@Override
	public ObservationCustomField findById(Long id) {
		Session session = sessionFactory.openSession();
		ObservationCustomField result = null;
		try {
			result = session.get(ObservationCustomField.class, id);
		} catch (Exception e) {
			logger.error(e.getMessage());
		} finally {
			session.close();
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	public List<ObservationCustomField> findByObservationIdUGidCFId(Long observationId, Long customFieldId,
			Long userGroupId) {
		Session session = sessionFactory.openSession();
		String qry = "from ObservationCustomField where observationId = :id and customFieldId = :cfId and userGroupId = :ugId";
		List<ObservationCustomField> result = null;
		try {
			Query<ObservationCustomField> query = session.createQuery(qry);
			query.setParameter("id", observationId);
			query.setParameter("cfId", customFieldId);
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
	public void bulkSoftDeleteObsCfMappingByUgId(Long id) {
		Session session = sessionFactory.openSession();
		Transaction transaction = null;
		String qry = "update observation_custom_field set is_usergroup_deleted = true where user_group_id = :id";
		try {
			transaction = session.beginTransaction();
			Query<ObservationCustomField> query = session.createSQLQuery(qry);
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
