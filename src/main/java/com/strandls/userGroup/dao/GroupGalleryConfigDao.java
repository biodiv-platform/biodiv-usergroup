package com.strandls.userGroup.dao;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.strandls.userGroup.pojo.GroupGalleryConfig;
import com.strandls.userGroup.util.AbstractDAO;

public class GroupGalleryConfigDao extends AbstractDAO<GroupGalleryConfig, Long> {
	private final Logger logger = LoggerFactory.getLogger(GroupGalleryConfig.class);

	@Inject
	protected GroupGalleryConfigDao(SessionFactory sessionFactory) {
		super(sessionFactory);
	}

	@Override
	public GroupGalleryConfig findById(Long id) {
		GroupGalleryConfig result = null;
		Session session = sessionFactory.openSession();
		try {
			result = session.get(GroupGalleryConfig.class, id);
		} catch (Exception e) {
			logger.error(e.getMessage());
		} finally {
			session.close();
		}
		return result;

	}

	@SuppressWarnings("unchecked")
	public List<GroupGalleryConfig> getAllMiniSliderByGroup(Long groupId) {
		List<GroupGalleryConfig> result = null;
		String qry = "from  GroupGalleryConfig where ug_id =: groupId";
		Session session = sessionFactory.openSession();
		try {
			Query<GroupGalleryConfig> query = session.createQuery(qry);
			query.setParameter("groupId", groupId);
			result = query.getResultList();
		} catch (Exception e) {
			logger.error(e.getMessage());
		} finally {
			session.close();
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	public List<GroupGalleryConfig> getByGalleryId(Long groupId, Long galleryId) {
		String qry = "from GroupGalleryConfig where gallery_id = :galleryId and ug_id=:groupId";
		Session session = sessionFactory.openSession();
		List<GroupGalleryConfig> result = null;
		try {
			Query<GroupGalleryConfig> query = session.createQuery(qry);
			query.setParameter("galleryId", galleryId);
			query.setParameter("groupId", groupId);
			result = query.getResultList();

		} catch (Exception e) {
			logger.error(e.getMessage());
		} finally {
			session.close();
		}
		return result;
	}

}