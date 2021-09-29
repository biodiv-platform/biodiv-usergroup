/**
 * 
 */
package com.strandls.userGroup.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.strandls.esmodule.controllers.EsServicesApi;
import com.strandls.user.ApiException;
import com.strandls.user.controller.RoleServiceApi;
import com.strandls.user.controller.UserServiceApi;
import com.strandls.user.pojo.Role;
import com.strandls.user.pojo.User;
import com.strandls.user.pojo.UserIbp;
import com.strandls.userGroup.dao.UserGroupMemberRoleDao;
import com.strandls.userGroup.pojo.GroupAddMember;
import com.strandls.userGroup.pojo.UserGroupIbp;
import com.strandls.userGroup.pojo.UserGroupMemberRole;
import com.strandls.userGroup.pojo.UserGroupMembersCount;
import com.strandls.userGroup.pojo.UserGroupPermissions;
import com.strandls.userGroup.pojo.UserUgRoleMapping;
import com.strandls.userGroup.service.UserGroupMemberService;
import com.strandls.userGroup.service.UserGroupSerivce;

/**
 * @author Abhishek Rudra
 *
 */
public class UserGroupMemberServiceImpl implements UserGroupMemberService {

	private final Logger logger = LoggerFactory.getLogger(UserGroupMemberServiceImpl.class);
	
	private static final String INDEX = "extended_user";
	private static final String TYPE = "_doc";

	@Inject
	private UserGroupMemberRoleDao userGroupMemberDao;

	@Inject
	private UserServiceApi userService;

	@Inject
	private UserGroupSerivce ugServices;

	@Inject
	private RoleServiceApi roleService;

	@Inject
	private EsServicesApi esService;

	@Override
	public Boolean checkUserGroupMember(Long userId, Long userGroupId) {
		UserGroupMemberRole result = userGroupMemberDao.findByUserGroupIdUserId(userGroupId, userId);
		if (result != null)
			return true;
		return false;
	}

	@Override
	public List<UserGroupMembersCount> getUserGroupMemberCount() {
		List<UserGroupMembersCount> result = userGroupMemberDao.fetchMemberCountUserGroup();
		return result;
	}

	@Override
	public Boolean checkFounderRole(Long userId, Long userGroupId) {
		try {
			InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream("config.properties");
			Properties properties = new Properties();
			try {
				properties.load(in);
			} catch (IOException e) {
				logger.error(e.getMessage());
			}
			String founder = properties.getProperty("userGroupFounder");
			in.close();
			UserGroupMemberRole result = userGroupMemberDao.findByUserGroupIdUserId(userGroupId, userId);
			if (result != null && result.getRoleId().equals(Long.parseLong(founder)))
				return true;
			return false;

		} catch (Exception e) {
			logger.error(e.getMessage());
		}

		return null;
	}

	@Override
	public Boolean checkModeratorRole(Long userId, Long userGroupId) {
		try {
			InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream("config.properties");
			Properties properties = new Properties();
			try {
				properties.load(in);
			} catch (IOException e) {
				logger.error(e.getMessage());
			}
			String founder = properties.getProperty("userGroupExpert");
			in.close();
			UserGroupMemberRole result = userGroupMemberDao.findByUserGroupIdUserId(userGroupId, userId);
			if (result != null && result.getRoleId().equals(Long.parseLong(founder)))
				return true;
			return false;

		} catch (Exception e) {
			logger.error(e.getMessage());
		}

		return null;

	}

	@Override
	public UserGroupMemberRole addMemberUG(Long userId, Long roleId, Long userGroupId) {
		UserGroupMemberRole ugMemberRole = new UserGroupMemberRole(userGroupId, roleId, userId);
		try {
			ugMemberRole = userGroupMemberDao.save(ugMemberRole);
			groupUserEsUpdate(userId);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return ugMemberRole;
	}

	@Override
	public Boolean removeGroupMember(Long userId, Long userGroupId) {
		try {
			UserGroupMemberRole ugMember = userGroupMemberDao.findByUserGroupIdUserId(userGroupId, userId);
			if (ugMember != null) {
				userGroupMemberDao.delete(ugMember);
				List<UserGroupMemberRole> members = userGroupMemberDao.fetchByUserGroupIdRole(userGroupId);
				if (members == null || members.isEmpty()) {
					InputStream in = Thread.currentThread().getContextClassLoader()
							.getResourceAsStream("config.properties");
					Properties properties = new Properties();
					try {
						properties.load(in);
					} catch (IOException e) {
						logger.error(e.getMessage());
					}
					Long founderId = Long.parseLong(properties.getProperty("userGroupFounder"));
					Long portalAmdinId = Long.parseLong(properties.getProperty("portalAdminId"));
					in.close();
					ugMember = new UserGroupMemberRole(userGroupId, founderId, portalAmdinId);
					userGroupMemberDao.save(ugMember);
					groupUserEsUpdate(userId);
				}
				return true;
			}

		} catch (Exception e) {
			logger.error(e.getMessage());
		}

		return null;
	}

	@Override
	public Boolean joinGroup(Long userId, Long userGroupId) {
		try {
			Boolean isOpenGroup = userGroupMemberDao.checksGroupType(userGroupId.toString());
			if (isOpenGroup) {
				InputStream in = Thread.currentThread().getContextClassLoader()
						.getResourceAsStream("config.properties");
				Properties properties = new Properties();
				try {
					properties.load(in);
				} catch (IOException e) {
					logger.error(e.getMessage());
				}
				Long memberId = Long.parseLong(properties.getProperty("userGroupMember"));
				in.close();
				Boolean alreadyMember = userGroupMemberDao.checkUserAlreadyMapped(userId, userGroupId, memberId);
				if (!alreadyMember) {
					UserGroupMemberRole ugMember = new UserGroupMemberRole(userGroupId, memberId, userId);
					userGroupMemberDao.save(ugMember);
					groupUserEsUpdate(userId);
					return true;
				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return false;
	}

	@Override
	public List<Long> addMemberDirectly(GroupAddMember addMember) {
		try {
			Long roleId = addMember.getRoleId();
			Long userGroupId = addMember.getRoleId();
			List<Long> mappedUser = new ArrayList<Long>();
			for (Long userId : addMember.getMemberList()) {
				Boolean alreadyMember = userGroupMemberDao.checkUserAlreadyMapped(userId, userGroupId, roleId);
				if (!alreadyMember) {
					UserGroupMemberRole ugMemberRole = new UserGroupMemberRole(addMember.getUserGroupId(),
							addMember.getRoleId(), userId);
					userGroupMemberDao.save(ugMemberRole);
					groupUserEsUpdate(userId);
					mappedUser.add(userId);
				}

			}
			return mappedUser;
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return null;
	}

	private void groupUserEsUpdate(Long userId) throws ApiException {
		List<UserUgRoleMapping> ugRoleMapping = new ArrayList<UserUgRoleMapping>();
		List<Role> roles = roleService.getAllRoles();
		roles.forEach(item -> {
			List<UserGroupMemberRole> list = userGroupMemberDao.findGroupListByRoleAndUser(userId, item.getId());
			List<Long> groupList = list.stream().map(x -> x.getUserGroupId()).collect(Collectors.toList());
			UserUgRoleMapping groupRoleMapping = new UserUgRoleMapping(userId, item.getId(), item.getAuthority(),
					groupList.stream().toArray(Long[]::new));
			if (!groupList.isEmpty()) {
				ugRoleMapping.add(groupRoleMapping);
			}

		});
		Map<String, Object> doc = new HashMap<String, Object>();
		doc.put("userGroup", ugRoleMapping);
		try {
			esService.update(INDEX, TYPE, userId.toString(), doc);
		} catch (com.strandls.esmodule.ApiException e) {
			logger.error("Unable to update Es User Details " + e.getMessage());
		}

	}

	@Override
	public List<User> getFounderModerator(Long userGroupId) {
		try {
			List<UserGroupMemberRole> ugMemberRoleList = userGroupMemberDao.findFounderModerator(userGroupId);
			List<User> userList = new ArrayList<User>();
			for (UserGroupMemberRole ugMemberRole : ugMemberRoleList) {
				userList.add(userService.getUser(ugMemberRole.getsUserId().toString()));
			}
			return userList;
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return null;

	}

	@Override
	public List<UserIbp> getFounderList(Long userGroupId) {
		try {
			InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream("config.properties");
			Properties properties = new Properties();
			try {
				properties.load(in);
			} catch (IOException e) {
				logger.error(e.getMessage());
			}
			String founder = properties.getProperty("userGroupFounder");
			in.close();
			List<UserGroupMemberRole> ugMemberList = userGroupMemberDao.findMemberListByRoleId(userGroupId,
					Long.parseLong(founder));

			List<UserIbp> result = new ArrayList<UserIbp>();
			for (UserGroupMemberRole ugMember : ugMemberList) {
				result.add(userService.getUserIbp(ugMember.getsUserId().toString()));
			}
			return result;
		} catch (Exception e) {
			logger.error(e.getMessage());
		}

		return null;
	}

	@Override
	public List<UserIbp> getModeratorList(Long userGroupId) {
		try {
			InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream("config.properties");
			Properties properties = new Properties();
			try {
				properties.load(in);
			} catch (IOException e) {
				logger.error(e.getMessage());
			}
			String expert = properties.getProperty("userGroupExpert");
			in.close();
			List<UserGroupMemberRole> ugMemberList = userGroupMemberDao.findMemberListByRoleId(userGroupId,
					Long.parseLong(expert));

			List<UserIbp> result = new ArrayList<UserIbp>();
			for (UserGroupMemberRole ugMember : ugMemberList) {
				result.add(userService.getUserIbp(ugMember.getsUserId().toString()));
			}
			return result;
		} catch (Exception e) {
			logger.error(e.getMessage());
		}

		return null;
	}

	@Override
	public UserGroupPermissions getUserGroupObservationPermissions(Long userId) {
		List<UserGroupMemberRole> userMemberRole = userGroupMemberDao.getUserGroup(userId);
		List<UserGroupMemberRole> userFeatureRole = userGroupMemberDao.findUserGroupbyUserIdRole(userId);
		UserGroupPermissions ugPermission = new UserGroupPermissions(userMemberRole, userFeatureRole);
		return ugPermission;
	}

	@Override
	public Map<Long, Boolean> groupListByUserId(Long userId) {
		List<UserGroupIbp> userGroupList = ugServices.fetchAllUserGroup();
		Map<Long, Boolean> result = new HashMap<Long, Boolean>();

		for (UserGroupIbp userGroup : userGroupList) {
			Boolean isMember = checkUserGroupMember(userId, userGroup.getId());
			result.put(userGroup.getId(), isMember);
		}
		return result;
	}
}
