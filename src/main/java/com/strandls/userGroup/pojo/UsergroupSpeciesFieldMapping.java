package com.strandls.userGroup.pojo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

@Entity
@Table(name = "user_group_species_fields")
public class UsergroupSpeciesFieldMapping {

	private Long id;
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
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "usergroup_id")
	public Long getUsergroupId() {
		return usergroupId;
	}

	public void setUsergroupId(Long usergroupId) {
		this.usergroupId = usergroupId;
	}

	@Column(name = "species_field_id")
	public Long getSpeciesFieldId() {
		return speciesFieldId;
	}

	public void setSpeciesFieldId(Long speciesFieldId) {
		this.speciesFieldId = speciesFieldId;
	}

}
