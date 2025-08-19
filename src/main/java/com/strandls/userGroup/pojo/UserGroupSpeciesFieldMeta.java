package com.strandls.userGroup.pojo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "user_group_species_field_meta")
public class UserGroupSpeciesFieldMeta {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Long id;

	@Column(name = "usergroup_id", nullable = false)
	private Long userGroupId;

	@Column(name = "value_type")
	private String valueType;

	@Column(name = "value_id")
	private Long valueId;

	// Default constructor
	public UserGroupSpeciesFieldMeta() {
	}

	// Constructor with fields
	public UserGroupSpeciesFieldMeta(Long userGroupId, String valueType, Long valueId) {
		this.userGroupId = userGroupId;
		this.valueType = valueType;
		this.valueId = valueId;
	}

	// Getters and Setters
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getUserGroupId() {
		return userGroupId;
	}

	public void setUserGroupId(Long userGroupId) {
		this.userGroupId = userGroupId;
	}

	public String getValueType() {
		return valueType;
	}

	public void setValueType(String valueType) {
		this.valueType = valueType;
	}

	public Long getValueId() {
		return valueId;
	}

	public void setValueId(Long valueId) {
		this.valueId = valueId;
	}
}
