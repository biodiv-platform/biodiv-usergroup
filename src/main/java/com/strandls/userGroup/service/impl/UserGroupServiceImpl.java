/**
 * 
 */
package com.strandls.userGroup.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.strandls.userGroup.dao.FeaturedDao;
import com.strandls.userGroup.dao.UserGroupDao;
import com.strandls.userGroup.dao.UserGroupMemberRoleDao;
import com.strandls.userGroup.dao.UserGroupObservationDao;
import com.strandls.userGroup.pojo.Featured;
import com.strandls.userGroup.pojo.FeaturedCreate;
import com.strandls.userGroup.pojo.UserGroup;
import com.strandls.userGroup.pojo.UserGroupIbp;
import com.strandls.userGroup.pojo.UserGroupMemberRole;
import com.strandls.userGroup.pojo.UserGroupObservation;
import com.strandls.userGroup.service.UserGroupSerivce;

/**
 * @author Abhishek Rudra
 *
 */
public class UserGroupServiceImpl implements UserGroupSerivce {

	private final Logger logger = LoggerFactory.getLogger(UserGroupServiceImpl.class);

	@Inject
	private UserGroupDao userGroupDao;

	@Inject
	private UserGroupObservationDao userGroupObvDao;

	@Inject
	private UserGroupMemberRoleDao userGroupMemberRoleDao;

	@Inject
	private FeaturedDao featuredDao;

	@Override
	public UserGroup fetchByGroupId(Long id) {
		UserGroup userGroup = userGroupDao.findById(id);
		return userGroup;
	}

	@Override
	public UserGroupIbp fetchByGroupIdIbp(Long id) {
		UserGroup ug = userGroupDao.findById(id);
		UserGroupIbp ibp;
		if (ug.getDomianName() != null)
			ibp = new UserGroupIbp(ug.getId(), ug.getName(), ug.getIcon(), ug.getDomianName());
		else {
			String webAddress = "/group/" + ug.getWebAddress();
			ibp = new UserGroupIbp(ug.getId(), ug.getName(), ug.getIcon(), webAddress);
		}
		return ibp;
	}

	@Override
	public List<UserGroupIbp> fetchByObservationId(Long id) {
		List<UserGroupObservation> userGroupObv = userGroupObvDao.findByObservationId(id);
		List<UserGroupIbp> userGroup = new ArrayList<UserGroupIbp>();
		for (UserGroupObservation ugObv : userGroupObv) {
			userGroup.add(fetchByGroupIdIbp(ugObv.getUserGroupId()));
		}
		return userGroup;
	}

	@Override
	public List<UserGroupIbp> fetchByUserId(Long sUserId) {
		List<UserGroupMemberRole> result = userGroupMemberRoleDao.getUserGroup(sUserId);

		List<UserGroupIbp> userGroupList = new ArrayList<UserGroupIbp>();
		for (UserGroupMemberRole userGroup : result) {
			userGroupList.add(fetchByGroupIdIbp(userGroup.getUserGroupId()));
		}
		return userGroupList;
	}

	@Override
	public List<Long> createUserGroupObservationMapping(Long observatoinId, List<Long> userGroups) {

		List<Long> resultList = new ArrayList<Long>();

		for (Long userGroup : userGroups) {
			UserGroupObservation userGroupObs = new UserGroupObservation(userGroup, observatoinId);
			UserGroupObservation result = userGroupObvDao.save(userGroupObs);
			if (result != null)
				resultList.add(result.getUserGroupId());
		}
		return resultList;
	}

	@Override
	public List<UserGroupIbp> updateUserGroupObservationMapping(Long observationId, List<Long> userGorups) {

		List<Long> previousUserGroup = new ArrayList<Long>();
		List<UserGroupObservation> previousMapping = userGroupObvDao.findByObservationId(observationId);
		for (UserGroupObservation ug : previousMapping) {
			if (!(userGorups.contains(ug.getUserGroupId()))) {
				userGroupObvDao.delete(ug);
			}
			previousUserGroup.add(ug.getUserGroupId());
		}

		for (Long userGroupId : userGorups) {
			if (!(previousUserGroup.contains(userGroupId))) {
				UserGroupObservation userGroupMapping = new UserGroupObservation(userGroupId, observationId);
				userGroupObvDao.save(userGroupMapping);
			}
		}

		List<UserGroupIbp> result = fetchByObservationId(observationId);

		return result;
	}

	@Override
	public List<UserGroupIbp> findFeaturableGroups(Long objectId, Long userId) {
		List<UserGroupIbp> groupList = fetchByObservationId(objectId);
		List<UserGroupMemberRole> userMemberrole = userGroupMemberRoleDao.findUserGroupbyUserIdRole(userId);
		List<Long> userMemberGroup = new ArrayList<Long>();
		List<UserGroupIbp> accessibleGroup = new ArrayList<UserGroupIbp>();
		for (UserGroupMemberRole memberRole : userMemberrole) {
			userMemberGroup.add(memberRole.getUserGroupId());
		}
		for (UserGroupIbp ug : groupList) {
			if (userMemberGroup.contains(ug.getId())) {
				accessibleGroup.add(ug);
			}
		}
		return accessibleGroup;
	}

	@Override
	public List<UserGroupIbp> fetchAllUserGroup() {
		List<UserGroup> userGroupList = userGroupDao.findAll();
		List<UserGroupIbp> result = new ArrayList<UserGroupIbp>();
		UserGroupIbp ibp = null;
		for (UserGroup userGroup : userGroupList) {

			if (userGroup.getDomianName() != null)
				ibp = new UserGroupIbp(userGroup.getId(), userGroup.getName(), userGroup.getIcon(),
						userGroup.getDomianName());
			else {
				String webAddress = "/group/" + userGroup.getWebAddress();
				ibp = new UserGroupIbp(userGroup.getId(), userGroup.getName(), userGroup.getIcon(), webAddress);
			}
			result.add(ibp);
		}
		return result;
	}

	@Override
	public List<Long> fetchUserAllowedGroupId(Long userId) {
		List<UserGroupMemberRole> userMomberRole = userGroupMemberRoleDao.findUserGroupbyUserIdRole(userId);
		List<Long> userGroupId = new ArrayList<Long>();
		for (UserGroupMemberRole userGroupMemberRole : userMomberRole) {
			userGroupId.add(userGroupMemberRole.getUserGroupId());
		}
		return userGroupId;
	}

	@Override
	public List<Featured> fetchFeatured(String objectType, Long id) {
		List<Featured> featuredList = featuredDao.fetchAllFeatured(objectType, id);
		return featuredList;
	}

	@Override
	public List<Featured> createFeatured(Long userId, FeaturedCreate featuredCreate) {

		List<Featured> result = new ArrayList<Featured>();
		try {
			List<Long> userGroupIds = fetchUserAllowedGroupId(userId);

			Featured featured;
			if (featuredCreate.getObjectType().equalsIgnoreCase("observation"))
				featuredCreate.setObjectType("species.participation.Observation");

			List<Featured> featuredList = featuredDao.fetchAllFeatured(featuredCreate.getObjectType(),
					featuredCreate.getObjectId());

			for (Long userGroupId : featuredCreate.getUserGroup()) {
				if (userGroupIds.contains(userGroupId)) {

					int flag = 0;
					for (Featured alreadyFeatured : featuredList) {
						if (alreadyFeatured.getUserGroup() == userGroupId) {
							alreadyFeatured.setCreatedOn(new Date());
							alreadyFeatured.setNotes(featuredCreate.getNotes());
							alreadyFeatured.setAuthorId(userId);
							featuredDao.update(alreadyFeatured);
							flag = 1;
						}
					}

					if (flag == 0) {
						featured = new Featured(null, 0L, userId, new Date(), featuredCreate.getNotes(),
								featuredCreate.getObjectId(), featuredCreate.getObjectType(), userGroupId, 205L, null);
						featured = featuredDao.save(featured);

					}

				}
			}
			result = featuredDao.fetchAllFeatured(featuredCreate.getObjectType(), featuredCreate.getObjectId());
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return result;
	}

	@Override
	public List<Featured> removeFeatured(Long userId, String objectType, Long objectId, List<Long> userGroupList) {

		List<Featured> resultList = null;
		try {
			if (objectType.equalsIgnoreCase("observation"))
				objectType = "species.participation.Observation";
			List<Featured> featuredList = featuredDao.fetchAllFeatured(objectType, objectId);
			List<Long> userGroupIds = fetchUserAllowedGroupId(userId);

			for (Long userGroupId : userGroupList) {
				if (userGroupIds.contains(userGroupId)) {
					for (Featured featured : featuredList) {
						if (featured.getUserGroup() == userGroupId) {
							featuredDao.delete(featured);
							break;
						}
					}
				}
			}

			resultList = featuredDao.fetchAllFeatured(objectType, objectId);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return resultList;
	}

}
