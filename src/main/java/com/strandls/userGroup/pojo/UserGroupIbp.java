/**
 * 
 */
package com.strandls.userGroup.pojo;

/**
 * @author Abhishek Rudra
 *
 */
public class UserGroupIbp {

	private Long id;
	private String name;
	private String icon;
	private String webAddress;
	private Boolean isParticipatory;
	private Long groupId;

	/**
	 * 
	 */
	public UserGroupIbp() {
		super();
	}

	/**
	 * @param id
	 * @param name
	 * @param icon
	 * @param webAddress
	 * @param isParticipatory
	 */
	public UserGroupIbp(Long id, String name, String icon, String webAddress, Boolean isParticipatory, Long groupId) {
		super();
		this.id = id;
		this.name = name;
		this.icon = icon;
		this.webAddress = webAddress;
		this.isParticipatory = isParticipatory;
		this.groupId = groupId;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getWebAddress() {
		return webAddress;
	}

	public void setWebAddress(String webAddress) {
		this.webAddress = webAddress;
	}

	public Boolean getIsParticipatory() {
		return isParticipatory;
	}

	public void setIsParticipatory(Boolean isParticipatory) {
		this.isParticipatory = isParticipatory;
	}
	
	public Long getGroupId() {
		return groupId;
	}

	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}

}
