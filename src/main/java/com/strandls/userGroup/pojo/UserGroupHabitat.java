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
@Table(name = "user_group_habitat")
@IdClass(UserGroupHabitatCompositeKey.class)
public class UserGroupHabitat {
	private Long habitatId;
	private Long userGroupId;

	/** */
	public UserGroupHabitat() {
		super();
	}

	/**
	 * @param habitatId
	 * @param userGroupId
	 */
	public UserGroupHabitat(Long habitatId, Long userGroupId) {
		super();
		this.habitatId = habitatId;
		this.userGroupId = userGroupId;
	}

	@Id
	@Column(name = "habitat_id")
	public Long getHabitatId() {
		return habitatId;
	}

	public void setHabitatId(Long habitatId) {
		this.habitatId = habitatId;
	}

	@Id
	@Column(name = "user_group_habitats_id")
	public Long getUserGroupId() {
		return userGroupId;
	}

	public void setUserGroupId(Long userGroupId) {
		this.userGroupId = userGroupId;
	}
}
