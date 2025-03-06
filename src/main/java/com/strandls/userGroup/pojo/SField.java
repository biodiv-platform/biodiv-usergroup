package com.strandls.userGroup.pojo;

public class SField {

	private Long id;
	private String header;
	private String path;
	private String label;

	public SField() {
		super();
		// TODO Auto-generated constructor stub
	}

	public SField(Long id, String header, String path, String label) {
		super();
		this.id = id;
		this.header = header;
		this.path = path;
		this.label = label;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getHeader() {
		return header;
	}

	public void setHeader(String header) {
		this.header = header;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

}
