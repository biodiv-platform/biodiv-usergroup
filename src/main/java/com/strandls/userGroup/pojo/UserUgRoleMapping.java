package com.strandls.userGroup.pojo;

import java.util.List;

public class UserUgRoleMapping {
	private Long userid;
	private Long roleid;
	private String role;
	private List<Long> usergroupids;

	/**
	 * @param userid
	 * @param roleid
	 * @param role
	 * @param usergroupids
	 */
	public UserUgRoleMapping(Long userid, Long roleid, String role, List<Long> usergroupids) {
		super();
		this.userid = userid;
		this.roleid = roleid;
		this.role = role;
		this.usergroupids = usergroupids;
	}

	public UserUgRoleMapping() {
		super();
	}

	public Long getUserid() {
		return userid;
	}

	public void setUserid(Long userid) {
		this.userid = userid;
	}

	public Long getRoleid() {
		return roleid;
	}

	public void setRoleid(Long roleid) {
		this.roleid = roleid;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public List<Long> getUsergroupids() {
		return usergroupids;
	}

	public void setUsergroupids(List<Long> usergroupids) {
		this.usergroupids = usergroupids;
	}
}
