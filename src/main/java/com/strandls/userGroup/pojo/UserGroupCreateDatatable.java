package com.strandls.userGroup.pojo;

import java.util.Date;
import java.util.List;


public class UserGroupCreateDatatable extends UserGroupSpeciesCreateData{
	
	/**
	 * 
	 */
	private String title;
	private Date createdOn;
	private String location;
	public UserGroupCreateDatatable(String title, Date createdOn, String location) {
		super();
		this.title = title;
		this.createdOn = createdOn;
		this.location = location;
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

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

}
