package com.strandls.userGroup.pojo;

import java.util.List;

public class CustomFieldEditData extends CustomFieldDetails  {

	private CustomFields customFields;
	private List<CustomFieldValues> cfValues;
	private String defaultValue;
	private Integer displayOrder;
	private Boolean isMandatory;
	private Boolean allowedParticipation;
	private Long userGroupId;
	
	
	/**
	 * 
	 */
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
			Integer displayOrder, Boolean isMandatory, Boolean allowedParticipation,Long userGroupId) {
		super(customFields,  cfValues,  defaultValue,
				 displayOrder,  isMandatory,  allowedParticipation);
		this.userGroupId = userGroupId;
		
	}

	public CustomFields getCustomFields() {
		return customFields;
	}

	public void setCustomFields(CustomFields customFields) {
		this.customFields = customFields;
	}

	public List<CustomFieldValues> getCfValues() {
		return cfValues;
	}

	public void setCfValues(List<CustomFieldValues> cfValues) {
		this.cfValues = cfValues;
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	public Integer getDisplayOrder() {
		return displayOrder;
	}

	public void setDisplayOrder(Integer displayOrder) {
		this.displayOrder = displayOrder;
	}

	public Boolean getIsMandatory() {
		return isMandatory;
	}

	public void setIsMandatory(Boolean isMandatory) {
		this.isMandatory = isMandatory;
	}

	public Boolean getAllowedParticipation() {
		return allowedParticipation;
	}

	public void setAllowedParticipation(Boolean allowedParticipation) {
		this.allowedParticipation = allowedParticipation;
	}

	public Long getUserGroupId() {
		return userGroupId;
	}

	public void setUserGroupId(Long userGroupId) {
		this.userGroupId = userGroupId;
	}


}
