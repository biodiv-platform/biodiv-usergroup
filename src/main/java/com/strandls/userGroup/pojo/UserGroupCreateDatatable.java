package com.strandls.userGroup.pojo;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;

public class UserGroupCreateDatatable extends UserGroupSpeciesCreateData{
	
	/**
	 * 
	 */
	private String title;
	 private Date createdOn;
	public UserGroupCreateDatatable() {
		super();
	}

	/**
	 * @param userGroupIds
	 */
	public UserGroupCreateDatatable(List<Long> userGroupIds) {
		super(userGroupIds);
	}


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }


}
