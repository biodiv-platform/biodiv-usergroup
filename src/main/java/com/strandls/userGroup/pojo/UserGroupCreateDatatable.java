package com.strandls.userGroup.pojo;

import java.util.Date;
import java.util.List;

public class UserGroupCreateDatatable extends UserGroupSpeciesCreateData {

	/** */
	private String title;

	private Date createdOn;
	private String location;
	private String contributor;

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

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getContributor() {
		return contributor;
	}

	public void setContributor(String contributor) {
		this.contributor = contributor;
	}
}
