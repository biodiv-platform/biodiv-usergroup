package com.strandls.userGroup.pojo;

import java.util.List;

public class CustomFieldEditData extends CustomFieldDetails {

	private Long userGroupId;

	/** */
	public CustomFieldEditData() {
		super();
	}

	/**
	 * @param customFields
	 * @param cfValues
	 * @param defaultValue
	 * @param displayOrder
	 * @param isMandatory
	 * @param allowedParticipation
	 */
	public CustomFieldEditData(CustomFields customFields, List<CustomFieldValues> cfValues, String defaultValue,
			Integer displayOrder, Boolean isMandatory, Boolean allowedParticipation, Long userGroupId) {
		super(customFields, cfValues, defaultValue, displayOrder, isMandatory, allowedParticipation);
		this.userGroupId = userGroupId;
	}

	public Long getUserGroupId() {
		return userGroupId;
	}

	public void setUserGroupId(Long userGroupId) {
		this.userGroupId = userGroupId;
	}
}
