package com.strandls.userGroup.pojo;

import java.util.List;

public class UserGroupDatatableMapping {

	private Long total;
	private List<UserGroupDataTable> userGroupDataTableList;

	public UserGroupDatatableMapping(Long total, List<UserGroupDataTable> userGroupDataTableList) {
		this.total = total;
		this.userGroupDataTableList = userGroupDataTableList;
	}

	public UserGroupDatatableMapping() {
		super();
	}

	public Long getTotal() {
		return total;
	}

	public void setTotal(Long total) {
		this.total = total;
	}

	public List<UserGroupDataTable> getUserGroupDataTableList() {
		return userGroupDataTableList;
	}

	public void setUserGroupDataTableList(List<UserGroupDataTable> userGroupDataTableList) {
		this.userGroupDataTableList = userGroupDataTableList;
	}

}
