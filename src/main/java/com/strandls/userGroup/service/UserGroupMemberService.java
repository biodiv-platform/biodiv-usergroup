/** */
package com.strandls.userGroup.service;

import java.util.List;
import java.util.Map;

import com.strandls.user.pojo.User;
import com.strandls.user.pojo.UserIbp;
import com.strandls.userGroup.pojo.GroupAddMember;
import com.strandls.userGroup.pojo.UserGroupMemberRole;
import com.strandls.userGroup.pojo.UserGroupMembersCount;
import com.strandls.userGroup.pojo.UserGroupPermissions;

import jakarta.servlet.http.HttpServletRequest;

/**
 * @author Abhishek Rudra
 */
public interface UserGroupMemberService {

	public Boolean checkUserGroupMember(Long userId, Long userGroupId);

	public List<UserGroupMembersCount> getUserGroupMemberCount();

	public Boolean checkFounderRole(Long userId, Long userGroupId);

	public Boolean checkModeratorRole(Long userId, Long userGroupId);

	public UserGroupMemberRole addMemberUG(Long userId, Long roleId, Long userGroupId);

	public Boolean removeGroupMember(Long userId, Long userGroupId);

	public Boolean removeBulkGroupMember(HttpServletRequest request, List<Long> userId, Long userGroupId);

	public Boolean joinGroup(Long userId, Long userGroupId);

	public List<Long> addMemberDirectly(GroupAddMember addMember);

	public List<User> getFounderModerator(Long userGroupId);

	public List<UserIbp> getFounderList(Long userGroupId);

	public List<UserIbp> getModeratorList(Long userGroupId);

	public UserGroupPermissions getUserGroupObservationPermissions(Long userId);

	public Map<Long, Boolean> groupListByUserId(Long userId);
}
