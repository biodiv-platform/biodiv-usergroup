package com.strandls.userGroup.service.impl;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.HttpHeaders;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.strandls.activity.pojo.MailData;
import com.strandls.activity.pojo.UserGroupMailData;
import com.strandls.userGroup.dao.UserGroupDataTableDao;
import com.strandls.userGroup.pojo.UserGroupDataTable;
import com.strandls.userGroup.pojo.UserGroupDatatableFetch;
import com.strandls.userGroup.pojo.UserGroupDatatableMapping;
import com.strandls.userGroup.pojo.UserGroupIbp;
import com.strandls.userGroup.service.UserGroupDatatableService;

public class UserGroupDatatableServiceImpl implements UserGroupDatatableService {

	private final Logger logger = LoggerFactory.getLogger(UserGroupDatatableServiceImpl.class);

	@Inject
	private UserGroupDataTableDao userGroupDataTableDao;

	@Inject
	private UserGroupServiceImpl userGroupService;

	@Inject
	private LogActivities logActivity;

	@Override
	public List<UserGroupIbp> fetchByDataTableId(Long id) {
		try {
			List<UserGroupDataTable> userGroupDataTable = userGroupDataTableDao.findByDataTableId(id);
			List<UserGroupIbp> userGroup = new ArrayList<UserGroupIbp>();
			if (userGroupDataTable != null && !userGroupDataTable.isEmpty()) {
				for (UserGroupDataTable ugObv : userGroupDataTable) {
					userGroup.add(userGroupService.fetchByGroupIdIbp(ugObv.getUserGroupId()));
				}
			}
			if (!userGroup.isEmpty())
				return userGroup;

		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return null;
	}
	
	private MailData updateDatatableMailData(Long datatableId , MailData mailData) {
		List<UserGroupMailData> userGroup = new ArrayList<UserGroupMailData>();
		List<UserGroupIbp> updatedUG = fetchByDataTableId(datatableId);
		for (UserGroupIbp ug : updatedUG) {
			UserGroupMailData ugMail = new UserGroupMailData();
			ugMail.setIcon(ug.getIcon());
			ugMail.setId(ug.getId());
			ugMail.setName(ug.getName());
			ugMail.setWebAddress(ug.getWebAddress());

			userGroup.add(ugMail);
		}
		mailData.setUserGroupData(userGroup);
		return mailData;
	}


	@Override
	public List<Long> createUserGroupDatatableMapping(HttpServletRequest request, Long datatableId,
			List<Long> userGroups) {
		List<Long> resultList = new ArrayList<Long>();
		for (Long userGroup : userGroups) {
			UserGroupDataTable userGroupObs = new UserGroupDataTable(userGroup, datatableId);
			UserGroupDataTable result = userGroupDataTableDao.save(userGroupObs);
			if (result != null) {
				resultList.add(result.getUserGroupId());
				UserGroupIbp ugIbp = userGroupService.fetchByGroupIdIbp(userGroup);
				String description = userGroupService.createUgDescription(ugIbp);
				MailData mailData = new MailData();
				if (userGroups != null) {
					mailData = updateDatatableMailData(datatableId , mailData);
				}
				logActivity.logDatatableActivities(request.getHeader(HttpHeaders.AUTHORIZATION), description,
						datatableId, datatableId, "datatable", result.getUserGroupId(), "Posted resource", mailData);
			}
		}
		return resultList;
	}

	@Override
	public List<UserGroupIbp> updateUserGroupDatatableMapping(HttpServletRequest request, Long datatableId,
			List<Long> userGroups) {

		List<Long> previousUserGroup = new ArrayList<Long>();
		List<UserGroupDataTable> previousMapping = userGroupDataTableDao.findByDataTableId(datatableId);
		for (UserGroupDataTable ug : previousMapping) {
			if (!(userGroups.contains(ug.getUserGroupId()))) {
				userGroupDataTableDao.delete(ug);
				UserGroupIbp ugIbp = userGroupService.fetchByGroupIdIbp(ug.getUserGroupId());
				String description = userGroupService.createUgDescription(ugIbp);
				MailData mailData = new MailData();
				mailData = updateDatatableMailData(datatableId , mailData);
				logActivity.logDatatableActivities(request.getHeader(HttpHeaders.AUTHORIZATION), description,
						datatableId, datatableId, "datatable", ug.getUserGroupId(), "Removed resoruce", mailData);
			}
			previousUserGroup.add(ug.getUserGroupId());
		}

		for (Long userGroupId : userGroups) {
			if (!(previousUserGroup.contains(userGroupId))) {
				UserGroupDataTable userGroupMapping = new UserGroupDataTable(userGroupId, datatableId);
				userGroupDataTableDao.save(userGroupMapping);
				UserGroupIbp ugIbp = userGroupService.fetchByGroupIdIbp(userGroupId);
				String description = userGroupService.createUgDescription(ugIbp);
				MailData mailData = new MailData();
				mailData = updateDatatableMailData(datatableId , mailData);
				logActivity.logDatatableActivities(request.getHeader(HttpHeaders.AUTHORIZATION), description,
						datatableId, datatableId, "datatable", userGroupId, "Posted resource", mailData);
			}
		}

		List<UserGroupIbp> result = fetchByDataTableId(datatableId);

		return result;
	}

	@Override
	public UserGroupDatatableMapping fetchDataTableByUserGroup(UserGroupDatatableFetch groupDatatableFetch) {
		UserGroupDatatableMapping result = new UserGroupDatatableMapping();
		if (groupDatatableFetch.getUserGroupId() == null) {
			result.setTotal(0L);
			return result;
		}
		Long totalCount = userGroupDataTableDao.findTotalDatatableByUserGroup(groupDatatableFetch.getUserGroupId());
		if (totalCount > 0) {
			List<UserGroupDataTable> userGroupDataTableList = userGroupDataTableDao.findDatatableByUserGroupId(
					groupDatatableFetch.getUserGroupId(), groupDatatableFetch.getLimit(),
					groupDatatableFetch.getOffset());

			result.setUserGroupDataTableList(userGroupDataTableList);
		}
		result.setTotal(totalCount);

		return result;
	}

}
