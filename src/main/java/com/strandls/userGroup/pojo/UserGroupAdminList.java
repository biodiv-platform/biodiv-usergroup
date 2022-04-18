package com.strandls.userGroup.pojo;

import java.util.List;

public class UserGroupAdminList {
	private Boolean isAdmin;
	private List<UserGroupIbp> ugList;

	public UserGroupAdminList() {
		super();
	}

	public UserGroupAdminList(Boolean isAdmin, List<UserGroupIbp> ugList) {
		super();
		this.isAdmin = isAdmin;
		this.ugList = ugList;
	}

	public Boolean getIsAdmin() {
		return isAdmin;
	}

	public void setIsAdmin(Boolean isAdmin) {
		this.isAdmin = isAdmin;
	}

	public List<UserGroupIbp> getUgList() {
		return ugList;
	}

	public void setUgList(List<UserGroupIbp> ugList) {
		this.ugList = ugList;
	}

}
