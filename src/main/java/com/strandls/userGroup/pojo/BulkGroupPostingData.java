/**
 * 
 */
package com.strandls.userGroup.pojo;

import java.util.List;

/**
 * @author Abhishek Rudra
 *
 */
public class BulkGroupPostingData {

	private List<Long> userGroupList;
	private List<UserGroupObvFilterData> ugFilterDataList;
	private String recordType;

	public BulkGroupPostingData() {
		super();
	}

	public BulkGroupPostingData(List<Long> userGroupList, List<UserGroupObvFilterData> ugFilterDataList,
			String recordType) {
		super();
		this.userGroupList = userGroupList;
		this.ugFilterDataList = ugFilterDataList;
		this.recordType = recordType;
	}

	public List<Long> getUserGroupList() {
		return userGroupList;
	}

	public void setUserGroupList(List<Long> userGroupList) {
		this.userGroupList = userGroupList;
	}

	public List<UserGroupObvFilterData> getUgObvFilterDataList() {
		return ugFilterDataList;
	}

	public void setUgObvFilterDataList(List<UserGroupObvFilterData> ugFilterDataList) {
		this.ugFilterDataList = ugFilterDataList;
	}

	public String getRecordType() {
		return recordType;
	}

	public void setRecordType(String recordType) {
		this.recordType = recordType;
	}

}
