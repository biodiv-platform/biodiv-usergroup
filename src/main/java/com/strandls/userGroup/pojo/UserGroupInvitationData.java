package com.strandls.userGroup.pojo;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * @author Abhishek Rudra
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserGroupInvitationData {

	private Long userGroupId;
	private List<Long> founderIds;
	private List<Long> moderatorsIds;
	private List<Long> memberIds;
	private List<String> founderEmail;
	private List<String> moderatorsEmail;

	/** */
	public UserGroupInvitationData() {
		super();
	}

	/**
	 * @param userGroupId
	 * @param founderIds
	 * @param moderatorsIds
	 * @param memberIds
	 * @param founderEmail
	 * @param moderatorsEmail
	 */
	public UserGroupInvitationData(Long userGroupId, List<Long> founderIds, List<Long> moderatorsIds,
			List<Long> memberIds, List<String> founderEmail, List<String> moderatorsEmail) {
		super();
		this.userGroupId = userGroupId;
		this.founderIds = founderIds;
		this.moderatorsIds = moderatorsIds;
		this.memberIds = memberIds;
		this.founderEmail = founderEmail;
		this.moderatorsEmail = moderatorsEmail;
	}

	public Long getUserGroupId() {
		return userGroupId;
	}

	public void setUserGroupId(Long userGroupId) {
		this.userGroupId = userGroupId;
	}

	public List<Long> getFounderIds() {
		return founderIds;
	}

	public void setFounderIds(List<Long> founderIds) {
		this.founderIds = founderIds;
	}

	public List<Long> getModeratorsIds() {
		return moderatorsIds;
	}

	public void setModeratorsIds(List<Long> moderatorsIds) {
		this.moderatorsIds = moderatorsIds;
	}

	public List<Long> getMemberIds() {
		return memberIds;
	}

	public void setMemberIds(List<Long> memberIds) {
		this.memberIds = memberIds;
	}

	public List<String> getFounderEmail() {
		return founderEmail;
	}

	public void setFounderEmail(List<String> founderEmail) {
		this.founderEmail = founderEmail;
	}

	public List<String> getModeratorsEmail() {
		return moderatorsEmail;
	}

	public void setModeratorsEmail(List<String> moderatorsEmail) {
		this.moderatorsEmail = moderatorsEmail;
	}
}
