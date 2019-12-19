/**
 * 
 */
package com.strandls.userGroup.service;

import java.util.List;

import com.strandls.userGroup.pojo.UserGroup;
import com.strandls.userGroup.pojo.UserGroupIbp;

/**
 * @author Abhishek Rudra
 *
 */
public interface UserGroupSerivce {

	public UserGroup fetchByGroupId(Long id);

	public UserGroupIbp fetchByGroupIdIbp(Long id);

	public List<UserGroupIbp> fetchByObservationId(Long id);

	public List<UserGroupIbp> fetchByUserId(Long sUserId);

	public List<Long> createUserGroupObservationMapping(Long observationId, List<Long> userGroups);

	public List<UserGroupIbp> updateUserGroupObservationMapping(Long observationId, List<Long> userGorups);

	public List<UserGroupIbp> findFeaturableGroups(Long objectId, Long userId);

	public List<UserGroupIbp> fetchAllUserGroup();

	public List<Long> fetchUserAllowedGroupId(Long userId);

}
