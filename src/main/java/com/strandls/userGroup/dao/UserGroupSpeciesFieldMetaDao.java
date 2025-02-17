 package com.strandls.userGroup.dao;

import java.util.List;

import javax.inject.Inject;
import javax.persistence.NoResultException;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.strandls.userGroup.pojo.UserGroupSpeciesFieldMeta;
import com.strandls.userGroup.util.AbstractDAO;

public class UserGroupSpeciesFieldMetaDao extends AbstractDAO<UserGroupSpeciesFieldMeta, Long> {

    private static final Logger logger = LoggerFactory.getLogger(UserGroupSpeciesFieldMetaDao.class);

    @Inject
    public UserGroupSpeciesFieldMetaDao(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    @SuppressWarnings("unchecked")
    public UserGroupSpeciesFieldMeta findByUserGroupAndValueId(Long userGroupId, Long valueId) {
        Session session = sessionFactory.openSession();
        UserGroupSpeciesFieldMeta result = null;
        try {
            String qry = "from UserGroupSpeciesFieldMeta where userGroupId = :userGroupId and valueId = :valueId";
            Query<UserGroupSpeciesFieldMeta> query = session.createQuery(qry);
            query.setParameter("userGroupId", userGroupId);
            query.setParameter("valueId", valueId);
            result = query.getSingleResult();
        } catch (NoResultException e) {
            logger.error(e.getMessage());
        } finally {
            session.close();
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    public List<UserGroupSpeciesFieldMeta> findByUserGroupId(Long userGroupId) {
        Session session = sessionFactory.openSession();
        List<UserGroupSpeciesFieldMeta> result = null;
        try {
            String qry = "from UserGroupSpeciesFieldMeta where userGroupId = :userGroupId";
            Query<UserGroupSpeciesFieldMeta> query = session.createQuery(qry);
            query.setParameter("userGroupId", userGroupId);
            result = query.getResultList();
        } catch (Exception e) {
            logger.error(e.getMessage());
        } finally {
            session.close();
        }
        return result;
    }

    public UserGroupSpeciesFieldMeta save(UserGroupSpeciesFieldMeta metadata) {
        Session session = sessionFactory.openSession();
        try {
            session.beginTransaction();
            session.save(metadata);
            session.getTransaction().commit();
        } catch (Exception e) {
            logger.error(e.getMessage());
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
        return metadata;
    }

    public UserGroupSpeciesFieldMeta delete(UserGroupSpeciesFieldMeta metadata) {
        Session session = sessionFactory.openSession();
        try {
            session.beginTransaction();
            session.delete(metadata);
            session.getTransaction().commit();
        } catch (Exception e) {
            logger.error(e.getMessage());
            session.getTransaction().rollback();
        } finally {
            session.close();
        }
		return metadata;
    }

	@Override
	public UserGroupSpeciesFieldMeta findById(Long id) {
		// TODO Auto-generated method stub
		return null;
	}
}