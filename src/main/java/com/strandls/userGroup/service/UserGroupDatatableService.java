package com.strandls.userGroup.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.strandls.userGroup.pojo.UserGroupCreateDatatable;
import com.strandls.userGroup.pojo.UserGroupDatatableFetch;
import com.strandls.userGroup.pojo.UserGroupDatatableMapping;
import com.strandls.userGroup.pojo.UserGroupIbp;

public interface UserGroupDatatableService {

	public List<UserGroupIbp> fetchByDataTableId(Long id);

	public UserGroupDatatableMapping fetchDataTableByUserGroup(UserGroupDatatableFetch groupDatatableFetch);

	public List<Long> createUserGroupDatatableMapping(HttpServletRequest request, Long datatableId,
			List<Long> userGroups);

	public List<UserGroupIbp> updateUserGroupDatatableMapping(HttpServletRequest request, Long datatableId,
			List<Long> userGroups ,UserGroupCreateDatatable dataTableData);


}
