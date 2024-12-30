package com.strandls.userGroup.pojo;

import java.io.Serializable;

public class UserGroupSpeciesFieldCompositKey implements Serializable {

	private static final long serialVersionUID = 3084394785780435304L;
	private Long usergroupId;
	private Long speciesFieldId;

	public UserGroupSpeciesFieldCompositKey() {
		super();
		// TODO Auto-generated constructor stub
	}

	public UserGroupSpeciesFieldCompositKey(Long usergroupId, Long speciesFieldId) {
		super();
		this.usergroupId = usergroupId;
		this.speciesFieldId = speciesFieldId;
	}

	public Long getUsergroupId() {
		return usergroupId;
	}

	public void setUsergroupId(Long usergroupId) {
		this.usergroupId = usergroupId;
	}

	public Long getSpeciesFieldId() {
		return speciesFieldId;
	}

	public void setSpeciesFieldId(Long speciesFieldId) {
		this.speciesFieldId = speciesFieldId;
	}

}
