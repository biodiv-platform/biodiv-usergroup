package com.strandls.userGroup.pojo;

import java.util.List;

public class UserGroupCreateDatatable extends UserGroupSpeciesCreateData{
	
	/**
	 * 
	 */
	public UserGroupCreateDatatable() {
		super();
	}

	/**
	 * @param userGroupIds
	 */
	public UserGroupCreateDatatable(List<Long> userGroupIds) {
		super(userGroupIds);
	}


}
