package com.strandls.userGroup.pojo;

import java.util.List;
import java.util.Map;

public class SpeciesFieldValuesDTO {

	private Long userGroupId;
	private Long speciesFieldId;
	private Map<String, List<Long>> values;

	public SpeciesFieldValuesDTO() {
		super();
	}

	public SpeciesFieldValuesDTO(Long userGroupId, Long speciesFieldId, Map<String, List<Long>> values) {
		super();
		this.userGroupId = userGroupId;
		this.speciesFieldId = speciesFieldId;
		this.values = values;
	}

	public Long getUserGroupId() {
		return userGroupId;
	}

	public void setUserGroupId(Long userGroupId) {
		this.userGroupId = userGroupId;
	}

	public Long getSpeciesFieldId() {
		return speciesFieldId;
	}

	public void setSpeciesFieldId(Long speciesFieldId) {
		this.speciesFieldId = speciesFieldId;
	}

	public Map<String, List<Long>> getValues() {
		return values;
	}

	public void setValues(Map<String, List<Long>> values) {
		this.values = values;
	}
}
