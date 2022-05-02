/**
 * 
 */
package com.strandls.userGroup.pojo;

import java.util.List;

/**
 * @author Abhishek Rudra
 *
 */
public class BulkGroupUnPostingData {

	private List<Long> userGroupList;
	private List<UserGroupObvFilterData> ugFilterDataList;
	private String recordType;

	public BulkGroupUnPostingData() {
		super();
	}

	public BulkGroupUnPostingData(List<Long> userGroupList, List<UserGroupObvFilterData> ugFilterDataList,String recordType) {
		super();
		this.userGroupList = userGroupList;
		this.ugFilterDataList  = ugFilterDataList;
		this.recordType  = recordType;
	}

	public List<Long> getUserGroupList() {
		return userGroupList;
	}

	public void setUserGroupList(List<Long> userGroupList) {
		this.userGroupList = userGroupList;
	}

	public String getRecordType() {
		return recordType;
	}

	public void setRecordType(String recordType) {
		this.recordType = recordType;
	}

	public List<UserGroupObvFilterData> getUgFilterDataList() {
		return ugFilterDataList;
	}

	public void setUgFilterDataList(List<UserGroupObvFilterData> ugFilterDataList) {
		this.ugFilterDataList = ugFilterDataList;
	}

}
