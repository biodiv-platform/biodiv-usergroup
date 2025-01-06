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

import com.strandls.userGroup.pojo.UsergroupSpeciesFieldMapping;
import com.strandls.userGroup.util.AbstractDAO;

public class UserGroupSpeciesFieldMappingDao extends AbstractDAO<UsergroupSpeciesFieldMapping, Long> {

	private final Logger logger = LoggerFactory.getLogger(UserGroupSpeciesFieldMappingDao.class);

	@Inject
	protected UserGroupSpeciesFieldMappingDao(SessionFactory sessionFactory) {
		super(sessionFactory);
	}

	@SuppressWarnings("unchecked")
	public List<UsergroupSpeciesFieldMapping> findSpeciesFieldsByUgId(Long ugId) {
		List<UsergroupSpeciesFieldMapping> result = new ArrayList<UsergroupSpeciesFieldMapping>();
		String qry = "from UsergroupSpeciesFieldMapping where usergroupId = :ugId";
		Session session = sessionFactory.openSession();
		try {
			Query<UsergroupSpeciesFieldMapping> query = session.createQuery(qry);
			query.setParameter("ugId", ugId);
			return query.getResultList();
		} catch (Exception e) {
			logger.error(e.getMessage());
		} finally {
			session.close();
		}
		return result;
	}

	public List<UsergroupSpeciesFieldMapping> addUserGroupSpeciesFields(
			List<UsergroupSpeciesFieldMapping> mappingList) {
		Session session = sessionFactory.openSession();
		Transaction tx = null;
		List<UsergroupSpeciesFieldMapping> savedMappings = new ArrayList<>();

		try {
			tx = session.beginTransaction();

			for (UsergroupSpeciesFieldMapping mapping : mappingList) {
				session.save(mapping);
				savedMappings.add(mapping);
			}

			tx.commit();
			return savedMappings;

		} catch (Exception e) {
			if (tx != null && tx.isActive()) {
				tx.rollback();
			}
			logger.error(e.getMessage());
			return new ArrayList<>();

		} finally {
			session.close();
		}
	}

	public boolean deleteUserGroupSpeciesFields(List<UsergroupSpeciesFieldMapping> mappingList) {
		Session session = sessionFactory.openSession();
		Transaction tx = null;

		try {
			tx = session.beginTransaction();

			for (UsergroupSpeciesFieldMapping mapping : mappingList) {
				// Create query with composite key conditions
				String hql = "DELETE FROM UsergroupSpeciesFieldMapping WHERE usergroupId = :ugId AND speciesFieldId = :sfId";
				Query<?> query = session.createQuery(hql);
				query.setParameter("ugId", mapping.getUsergroupId());
				query.setParameter("sfId", mapping.getSpeciesFieldId());
				query.executeUpdate();
			}

			tx.commit();
			return true;

		} catch (Exception e) {
			if (tx != null && tx.isActive()) {
				tx.rollback();
			}
			logger.error(e.getMessage());
			return false;

		} finally {
			session.close();
		}
	}

	@Override
	public UsergroupSpeciesFieldMapping findById(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

}
