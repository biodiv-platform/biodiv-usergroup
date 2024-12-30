package com.strandls.userGroup.pojo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

@Entity
@Table(name = "user_group_species_fields")
@IdClass(UserGroupSpeciesFieldCompositKey.class)
public class UsergroupSpeciesFieldMapping {

	private Long usergroupId;
	private Long speciesFieldId;

	public UsergroupSpeciesFieldMapping() {
		super();
		// TODO Auto-generated constructor stub
	}

	public UsergroupSpeciesFieldMapping(Long usergroupId, Long speciesFieldId) {
		super();
		this.usergroupId = usergroupId;
		this.speciesFieldId = speciesFieldId;
	}

	@Id
	@Column(name = "usergroup_id")
	public Long getUsergroupId() {
		return usergroupId;
	}

	public void setUsergroupId(Long usergroupId) {
		this.usergroupId = usergroupId;
	}

	@Id
	@Column(name = "species_field_id")
	public Long getSpeciesFieldId() {
		return speciesFieldId;
	}

	public void setSpeciesFieldId(Long speciesFieldId) {
		this.speciesFieldId = speciesFieldId;
	}

}
