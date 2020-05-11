/**
 * 
 */
package com.strandls.userGroup.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.strandls.userGroup.pojo.UserGroupFilterRule;
import com.strandls.userGroup.util.AbstractDAO;

/**
 * @author Abhishek Rudra
 *
 */
public class UserGroupFilterRuleDao extends AbstractDAO<UserGroupFilterRule, Long> {

	private final Logger logger = LoggerFactory.getLogger(UserGroupFilterRuleDao.class);

	/**
	 * @param sessionFactory
	 */
	@Inject
	protected UserGroupFilterRuleDao(SessionFactory sessionFactory) {
		super(sessionFactory);
	}

	@Override
	public UserGroupFilterRule findById(Long id) {
		UserGroupFilterRule result = null;
		Session session = sessionFactory.openSession();
		try {
			result = session.get(UserGroupFilterRule.class, id);
		} catch (Exception e) {
			logger.error(e.getMessage());
		} finally {
			session.close();
		}
		return result;
	}

}
