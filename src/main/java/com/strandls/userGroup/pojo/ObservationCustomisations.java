package com.strandls.userGroup.pojo;

public class ObservationCustomisations {
	private Long userGroupId;
	private String mediaToggle;

	public ObservationCustomisations() {
		super();
	}

	public ObservationCustomisations(Long userGroupId, String mediaToggle) {
		super();
		this.userGroupId = userGroupId;
		this.mediaToggle = mediaToggle;
	}

	public Long getUserGroupId() {
		return userGroupId;
	}

	public void setUserGroupId(Long userGroupId) {
		this.userGroupId = userGroupId;
	}

	public String getMediaToggle() {
		return mediaToggle;
	}

	public void setMediaToggle(String mediaToggle) {
		this.mediaToggle = mediaToggle;
	}

}
