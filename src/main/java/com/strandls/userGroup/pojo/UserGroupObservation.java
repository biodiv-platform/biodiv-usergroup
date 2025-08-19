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
@Table(name = "user_group_observations")
@IdClass(UserGroupObservationCompositeKey.class)
public class UserGroupObservation {

	private Long userGroupId;
	private Long observationId;

	/** */
	public UserGroupObservation() {
		super();
	}

	/**
	 * @param userGroupId
	 * @param observationId
	 */
	public UserGroupObservation(Long userGroupId, Long observationId) {
		super();
		this.userGroupId = userGroupId;
		this.observationId = observationId;
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
	@Column(name = "observation_id")
	public Long getObservationId() {
		return observationId;
	}

	public void setObservationId(Long observationId) {
		this.observationId = observationId;
	}
}
