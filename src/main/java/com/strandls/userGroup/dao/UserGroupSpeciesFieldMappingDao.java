package com.strandls.userGroup.dao;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
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

	@Override
	public UsergroupSpeciesFieldMapping findById(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

}
