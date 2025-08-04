/**
 * 
 */
package com.strandls.userGroup.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

import com.strandls.userGroup.pojo.UserGroup;
import com.strandls.userGroup.util.AbstractDAO;

/**
 * @author Abhishek Rudra
 *
 */
public class UserGroupDao extends AbstractDAO<UserGroup, Long> {

	private final Logger logger = LoggerFactory.getLogger(UserGroupDao.class);

	/**
	 * @param sessionFactory
	 */
	@Inject
	protected UserGroupDao(SessionFactory sessionFactory) {
		super(sessionFactory);
		// TODO Auto-generated constructor stub
	}

	@Override
	public UserGroup findById(Long id) {
		Session session = sessionFactory.openSession();
		UserGroup entity = null;
		try {
			entity = session.get(UserGroup.class, id);
		} catch (Exception e) {
			logger.error(e.getMessage());
		} finally {
			session.close();
		}
		return entity;
	}

	@SuppressWarnings("unchecked")
	public List<UserGroup> findUgListByIds(List<Long> ids) {
		Session session = sessionFactory.openSession();

		String qry = "from UserGroup where id IN (:ugIds)";
		try {
			Query<UserGroup> query = session.createQuery(qry);
			query.setParameter("ugIds", ids);
			return query.getResultList();

		} catch (Exception e) {
			logger.error(e.getMessage());
		} finally {
			session.close();
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public List<UserGroup> findFilterRule() {
		Session session = sessionFactory.openSession();
		List<UserGroup> result = null;
		String qry = "from UserGroup where newFilterRule is not null";
		try {
			Query<UserGroup> query = session.createQuery(qry);
			result = query.getResultList();

		} catch (Exception e) {
			logger.error(e.getMessage());
		} finally {
			session.close();
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	public List<UserGroup> findFilterRuleGroupWise(String groupIds) {
		Session session = sessionFactory.openSession();
		List<UserGroup> result = null;
		String qry = "from UserGroup where id IN (:groupIds) newFilterRule is not null";
		try {
			Query<UserGroup> query = session.createQuery(qry);
			query.setParameter("groupIds", groupIds);
			result = query.getResultList();

		} catch (Exception e) {
			logger.error(e.getMessage());
		} finally {
			session.close();
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	public Long getObservationAuthor(String observationId) {
		String qry = "SELECT author_id from observation where id = observationId";
		Session session = sessionFactory.openSession();
		try {
			qry = qry.replace("observationId", observationId);
			Query<Object> query = session.createNativeQuery(qry);
			Object resultObject = query.getSingleResult();
			Long authorId = Long.parseLong(resultObject.toString());
			return authorId;
		} catch (Exception e) {
			logger.error(e.getMessage());
		} finally {
			session.close();
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	public String findMediaToggleByUgId(Long ugId) {
		String qry = "SELECT mediaToggle from UserGroup where id = :ugId";
		Session session = sessionFactory.openSession();
		try {
			Query<Object> query = session.createQuery(qry);
			query.setParameter("ugId", ugId);
			return query.getSingleResult().toString();
		} catch (Exception e) {
			logger.error(e.getMessage());
		} finally {
			session.close();
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	public List<UserGroup> findByGroupId(Long ugId) {
		Session session = sessionFactory.openSession();
		List<UserGroup> result = null;
		String qry = "from UserGroup where group_id = :ugId";
		try {
			Query<UserGroup> query = session.createQuery(qry);
			query.setParameter("ugId", ugId);
			result = query.getResultList();

		} catch (Exception e) {
			logger.error(e.getMessage());
		} finally {
			session.close();
		}
		return result;
	}
	
	@SuppressWarnings("unchecked")
	public UserGroup findByGroupIdByLanguageId(Long ugId, Long langId) {
		Session session = sessionFactory.openSession();
		UserGroup result = null;
		String qry = "from UserGroup where group_id = :ugId and language_id = :langId";
		try {
			Query<UserGroup> query = session.createQuery(qry);
			query.setParameter("ugId", ugId);
			query.setParameter("langId", langId);
			result = query.getResultList().get(0);

		} catch (Exception e) {
			logger.error(e.getMessage());
		} finally {
			session.close();
		}
		return result;
	}
	
	@SuppressWarnings("unchecked")
	public boolean isWebAddressAllowedForGroup(String webAddress, Long excludeGroupId) {
	    Session session = sessionFactory.openSession();
	    boolean isAllowed = true;

	    String qry = "SELECT COUNT(*) FROM UserGroup WHERE webaddress = :webAddress";
	    if (excludeGroupId != null) {
	        qry+=" AND group_id != :excludeGroupId";
	    }

	    try {
	        Query<Long> query = session.createQuery(qry);
	        query.setParameter("webAddress", webAddress);
	        if (excludeGroupId != null) {
	            query.setParameter("excludeGroupId", excludeGroupId);
	        }

	        Long count = query.uniqueResult();
	        isAllowed = (count == 0); // allowed only if no conflict

	    } catch (Exception e) {
	        logger.error("Error checking webaddress uniqueness: {}", e.getMessage());
	        isAllowed = false; // safest to assume false if there's an error
	    } finally {
	        session.close();
	    }

	    return isAllowed;
	}


}
