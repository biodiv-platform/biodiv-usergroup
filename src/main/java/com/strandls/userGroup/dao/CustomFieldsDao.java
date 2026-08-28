/** */
package com.strandls.userGroup.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.strandls.userGroup.pojo.CustomFields;
import com.strandls.userGroup.util.AbstractDAO;

import jakarta.inject.Inject;

/**
 * @author Abhishek Rudra
 */
public class CustomFieldsDao extends AbstractDAO<CustomFields, Long> {

	private final Logger logger = LoggerFactory.getLogger(CustomFieldsDao.class);

	/**
	 * @param sessionFactory
	 */
	@Inject
	protected CustomFieldsDao(SessionFactory sessionFactory) {
		super(sessionFactory);
	}

	@Override
	public CustomFields findById(Long id) {
		Session session = sessionFactory.openSession();
		CustomFields result = null;
		try {
			result = session.get(CustomFields.class, id);
		} catch (Exception e) {
			logger.error(e.getMessage());
		} finally {
			session.close();
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	public List<CustomFields> findByIds(List<Long> ids) {
		if (ids == null || ids.isEmpty())
			return new ArrayList<CustomFields>();

		Session session = sessionFactory.openSession();
		List<CustomFields> result = null;
		String qry = "from CustomFields where id IN (:ids)";
		try {
			Query<CustomFields> query = session.createQuery(qry);
			query.setParameter("ids", ids);
			result = query.getResultList();
		} catch (Exception e) {
			logger.error(e.getMessage());
		} finally {
			session.close();
		}
		return result;
	}
}
