/** */
package com.strandls.userGroup.pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;

/**
 * @author Abhishek Rudra
 */
@Entity
@Table(name = "user_group_species_group")
@JsonIgnoreProperties(ignoreUnknown = true)
@IdClass(UserGroupSpeciesGroupCompositeKey.class)
public class UserGroupSpeciesGroup {
	private Long userGroupId;
	private Long speciesGroupId;

	/** */
	public UserGroupSpeciesGroup() {
		super();
	}

	/**
	 * @param userGroupId
	 * @param speciesGroupId
	 */
	public UserGroupSpeciesGroup(Long userGroupId, Long speciesGroupId) {
		super();
		this.userGroupId = userGroupId;
		this.speciesGroupId = speciesGroupId;
	}

	@Id
	@Column(name = "user_group_species_groups_id")
	public Long getUserGroupId() {
		return userGroupId;
	}

	public void setUserGroupId(Long userGroupId) {
		this.userGroupId = userGroupId;
	}

	@Id
	@Column(name = "species_group_id")
	public Long getSpeciesGroupId() {
		return speciesGroupId;
	}

	public void setSpeciesGroupId(Long speciesGroupId) {
		this.speciesGroupId = speciesGroupId;
	}
}
