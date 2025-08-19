/** */
package com.strandls.userGroup.pojo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;

/**
 * @author Abhishek Rudra
 */
@Entity
@Table(name = "user_group_species")
@IdClass(UserGroupSpeciesCompositeKey.class)
public class UserGroupSpecies {

	private Long userGroupId;
	private Long speciesId;

	/** */
	public UserGroupSpecies() {
		super();
	}

	/**
	 * @param userGroupId
	 * @param speciesId
	 */
	public UserGroupSpecies(Long userGroupId, Long speciesId) {
		super();
		this.userGroupId = userGroupId;
		this.speciesId = speciesId;
	}

	@Id
	@Column(name = "user_group_id")
	public Long getUserGroupId() {
		return userGroupId;
	}

	public void setUserGroupId(Long userGroupId) {
		this.userGroupId = userGroupId;
	}

	@Id
	@Column(name = "species_id")
	public Long getSpeciesId() {
		return speciesId;
	}

	public void setSpeciesId(Long speciesId) {
		this.speciesId = speciesId;
	}
}
