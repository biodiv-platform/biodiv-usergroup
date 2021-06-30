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

import com.strandls.userGroup.pojo.UserGroupCreatedOnDateRule;
import com.strandls.userGroup.pojo.UserGroupTaxonomicRule;
import com.strandls.userGroup.util.AbstractDAO;

/**
 * @author Abhishek Rudra
 *
 */
public class UserGroupTaxonomicRuleDao extends AbstractDAO<UserGroupTaxonomicRule, Long> {

	private final Logger logger = LoggerFactory.getLogger(UserGroupTaxonomicRuleDao.class);

	/**
	 * @param sessionFactory
	 */
	@Inject
	protected UserGroupTaxonomicRuleDao(SessionFactory sessionFactory) {
		super(sessionFactory);
	}

	@Override
	public UserGroupTaxonomicRule findById(Long id) {
		UserGroupTaxonomicRule result = null;
		Session session = sessionFactory.openSession();
		try {
			result = session.get(UserGroupTaxonomicRule.class, id);
		} catch (Exception e) {
			logger.error(e.getMessage());
		} finally {
			session.close();
		}
		return result;
	}

	public List<UserGroupTaxonomicRule> findByUserGroupIdIsEnabled(Long userGroupId) {
		String qry = "from UserGroupTaxonomicRule where userGroupId = :ugId and isEnabled = true";
		 return findUserGroupByQuery(userGroupId,qry);
	}

	public List<UserGroupTaxonomicRule> findAllByUserGroupId(Long userGroupId) {
		String qry = "from UserGroupTaxonomicRule where userGroupId = :ugId";
		return findUserGroupByQuery(userGroupId,qry);
	}
	
	@SuppressWarnings("unchecked")
	private List<UserGroupTaxonomicRule> findUserGroupByQuery(Long userGroupId, String qry) {
		Session session = sessionFactory.openSession();
		List<UserGroupTaxonomicRule> result = null;
		try {
			Query<UserGroupTaxonomicRule> query = session.createQuery(qry);
			query.setParameter("ugId", userGroupId);
			result = query.getResultList();
		} catch (Exception e) {
			logger.error(e.getMessage());
		} finally {
			session.close();
		}
		return result;
	}


}
