package com.strandls.userGroup.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.strandls.userGroup.pojo.UserGroupDataTable;
import com.strandls.userGroup.util.AbstractDAO;

import jakarta.inject.Inject;

public class UserGroupDataTableDao extends AbstractDAO<UserGroupDataTable, Long> {

	private final Logger logger = LoggerFactory.getLogger(UserGroupHabitatDao.class);

	/**
	 * @param sessionFactory
	 */
	@Inject
	protected UserGroupDataTableDao(SessionFactory sessionFactory) {
		super(sessionFactory);
	}

	@Override
	public UserGroupDataTable findById(Long id) {
		UserGroupDataTable result = null;
		Session session = sessionFactory.openSession();
		try {
			result = session.get(UserGroupDataTable.class, id);
		} catch (Exception e) {
			logger.error(e.getMessage());
		} finally {
			session.close();
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	public List<UserGroupDataTable> findByDataTableId(Long dataTableId) {
		String qry = "from UserGroupDataTable where dataTableId = :dtId";
		Session session = sessionFactory.openSession();
		List<UserGroupDataTable> result = new ArrayList<UserGroupDataTable>();
		try {
			Query<UserGroupDataTable> query = session.createQuery(qry);
			query.setParameter("dtId", dataTableId);
			result = query.getResultList();
		} catch (Exception e) {
			logger.error(e.getMessage());
		} finally {
			session.close();
		}
		return result;
	}

	@SuppressWarnings({ "rawtypes" })
	public Long findTotalDatatableByUserGroup(Long ugId) {

		Session session = sessionFactory.openSession();
		String qry = "select count(*) from UserGroupDataTable  where  userGroupId = :ugId";
		Long total = null;
		try {
			Query query = session.createQuery(qry);
			query.setParameter("ugId", ugId);
			total = (Long) query.uniqueResult();

		} catch (Exception e) {
			logger.error(e.getMessage());
		} finally {
			session.close();
		}
		return total;
	}

	@SuppressWarnings("unchecked")
	public List<UserGroupDataTable> findDatatableByUserGroupId(Long userGroupId, Integer limit, Integer offset) {
		String qry = "from UserGroupDataTable where userGroupId = :ugId ";
		List<UserGroupDataTable> result = null;
		Integer firstResult = offset != null ? offset : 0;
		Session session = sessionFactory.openSession();
		try {
			Query<UserGroupDataTable> query = session.createQuery(qry);
			query.setParameter("ugId", userGroupId);
			query.setFirstResult(firstResult);
			if (limit != null) {
				query.setMaxResults(limit);
			}
			result = query.getResultList();
		} catch (Exception e) {
			logger.error(e.getMessage());
		} finally {
			session.close();
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	public UserGroupDataTable checkDatatableUGMApping(Long datatableId, Long userGroupId) {
		String qry = "from UserGroupDataTable where dataTableId = :datatableId and userGroupId = :ugId";
		UserGroupDataTable result = null;
		Session session = sessionFactory.openSession();
		try {
			Query<UserGroupDataTable> query = session.createQuery(qry);
			query.setParameter("datatableId", datatableId);
			query.setParameter("ugId", userGroupId);
			result = query.getSingleResult();
		} catch (Exception e) {
			logger.error(e.getMessage());
		} finally {
			session.close();
		}
		return result;
	}
}
