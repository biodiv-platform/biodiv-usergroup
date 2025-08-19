package com.strandls.userGroup.pojo;

public class UserGroupDatatableFetch {

	private Long userGroupId;
	private Integer limit;
	private Integer offset;

	public UserGroupDatatableFetch() {
		super();
	}

	public UserGroupDatatableFetch(Long userGroupId, Integer limit, Integer offset) {
		this.userGroupId = userGroupId;
		this.limit = limit;
		this.offset = offset;
	}

	public Long getUserGroupId() {
		return userGroupId;
	}

	public void setUserGroupId(Long userGroupId) {
		this.userGroupId = userGroupId;
	}

	public Integer getLimit() {
		return limit;
	}

	public void setLimit(Integer limit) {
		this.limit = limit;
	}

	public Integer getOffset() {
		return offset;
	}

	public void setOffset(Integer offset) {
		this.offset = offset;
	}
}
