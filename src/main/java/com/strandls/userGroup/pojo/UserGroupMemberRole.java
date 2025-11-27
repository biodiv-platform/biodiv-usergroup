/** */
package com.strandls.userGroup.pojo;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

/**
 * @author Abhishek Rudra
 */
@Entity
@Table(name = "user_group_member_role", uniqueConstraints = @UniqueConstraint(columnNames = { "user_group_id",
		"s_user_id" }))
@IdClass(UserGroupMemberRoleCompositeKey.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserGroupMemberRole implements Serializable {

	/** */
	private static final long serialVersionUID = -5160519423395168068L;

	private Long userGroupId;
	private Long roleId;
	private Long sUserId;

	/** */
	public UserGroupMemberRole() {
		super();
	}

	/**
	 * @param userGroupId
	 * @param roleId
	 * @param sUserId
	 */
	public UserGroupMemberRole(Long userGroupId, Long roleId, Long sUserId) {
		super();
		this.userGroupId = userGroupId;
		this.roleId = roleId;
		this.sUserId = sUserId;
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
	@Column(name = "role_id")
	public Long getRoleId() {
		return roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

	@Id
	@Column(name = "s_user_id")
	@JsonProperty("sUserId")
	public Long getsUserId() {
		return sUserId;
	}

	@JsonProperty("sUserId")
	public void setsUserId(Long sUserId) {
		this.sUserId = sUserId;
	}
}
