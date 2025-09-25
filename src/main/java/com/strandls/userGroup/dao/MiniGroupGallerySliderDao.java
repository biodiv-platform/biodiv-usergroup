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

import com.google.inject.Inject;
import com.strandls.userGroup.pojo.MiniGroupGallerySlider;
import com.strandls.userGroup.util.AbstractDAO;

/**
 * @author Mekala Rishitha Ravi
 *
 */
public class MiniGroupGallerySliderDao extends AbstractDAO<MiniGroupGallerySlider, Long> {

	private final Logger logger = LoggerFactory.getLogger(MiniGroupGallerySliderDao.class);

	/**
	 * @param sessionFactory
	 */
	@Inject
	protected MiniGroupGallerySliderDao(SessionFactory sessionFactory) {
		super(sessionFactory);
	}

	@Override
	public MiniGroupGallerySlider findById(Long id) {
		MiniGroupGallerySlider result = null;
		Session session = sessionFactory.openSession();
		try {
			result = session.get(MiniGroupGallerySlider.class, id);
		} catch (Exception e) {
			logger.error(e.getMessage());
		} finally {
			session.close();
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	public List<MiniGroupGallerySlider> getAllGallerySliderInfoByGroupId(Long groupId, Long galleryId) {
		List<MiniGroupGallerySlider> result = null;
		String qry = 
				"from  MiniGroupGallerySlider where galleryId = :galleryId and ugId=:groupId";
		Session session = sessionFactory.openSession();
		try {
			Query<MiniGroupGallerySlider> query = session.createQuery(qry);
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

	@SuppressWarnings("unchecked")
	public List<MiniGroupGallerySlider> findBySliderIdByGroupId(Long groupId,Long sId) {
		String qry = "from MiniGroupGallerySlider where sliderId = :sId and ugId = :groupId";
		Session session = sessionFactory.openSession();
		List<MiniGroupGallerySlider> result = null;
		try {
			Query<MiniGroupGallerySlider> query = session.createQuery(qry);
			query.setParameter("sId", sId);
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