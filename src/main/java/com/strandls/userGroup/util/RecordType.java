package com.strandls.userGroup.util;

public enum RecordType {

	DOCUMENT("document"), OBSERVATION("observation"), DATATABLE("datatable"), SPECIES("species");

	private String field;

	private RecordType(String field) {
		this.field = field;
	}

	public String getValue() {
		return field;
	}
}
