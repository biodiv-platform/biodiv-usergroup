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

//	public UsergroupSpeciesFieldMapping addUserGroupSpeciesFields(UsergroupSpeciesFieldMapping mappingList) {
//		Session session = sessionFactory.openSession();
//		Transaction tx = null;
//		// UsergroupSpeciesFieldMapping savedMappings = new ArrayList<>();
//
//		UsergroupSpeciesFieldMapping result = new UsergroupSpeciesFieldMapping();
//
//		try {
//			tx = session.beginTransaction();
//
////			for (UsergroupSpeciesFieldMapping mapping : mappingList) {
////				session.save(mapping);
////				savedMappings.add(mapping);
////			}
//
//			result = (UsergroupSpeciesFieldMapping) session.save(mappingList);
//
//			tx.commit();
//			return result;
//
//		} catch (Exception e) {
//			if (tx != null && tx.isActive()) {
//				tx.rollback();
//			}
//			logger.error(e.getMessage());
//			return result;
//
//		} finally {
//			session.close();
//		}
//	}

	public UsergroupSpeciesFieldMapping addUserGroupSpeciesField(UsergroupSpeciesFieldMapping mapping) {
		Session session = sessionFactory.openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			session.save(mapping);
			tx.commit();
			return mapping;
		} catch (Exception e) {
			if (tx != null && tx.isActive()) {
				tx.rollback();
			}
			logger.error(e.getMessage());
			return null;
		} finally {
			session.close();
		}
	}

	public boolean deleteUserGroupSpeciesField(UsergroupSpeciesFieldMapping mapping) {
		Session session = sessionFactory.openSession();
		Transaction tx = null;

		try {
			tx = session.beginTransaction();

			// Create query with composite key conditions
			String hql = "DELETE FROM UsergroupSpeciesFieldMapping WHERE usergroupId = :ugId AND speciesFieldId = :sfId";
			Query<?> query = session.createQuery(hql);
			query.setParameter("ugId", mapping.getUsergroupId());
			query.setParameter("sfId", mapping.getSpeciesFieldId());
			query.executeUpdate();

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
