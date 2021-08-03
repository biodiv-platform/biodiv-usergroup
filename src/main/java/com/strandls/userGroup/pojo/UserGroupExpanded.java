package com.strandls.userGroup.pojo;

import java.util.Date;
import java.util.List;

/**
 * @author Harsh Zalavadiya
 *
 */
public class UserGroupExpanded {

	private Long id;
	private String name;
	private String icon;
	private String webAddress;
	private Boolean isParticipatory;
	private Long memberCount;
	private Date foundedOn;
	private Date startDate;
	private List<Long> speciesGroupIds;
	private List<Long> habitatIds;

	/**
	 *
	 */
	public UserGroupExpanded() {
		super();
	}

	/**
	 * @param id
	 * @param name
	 * @param icon
	 * @param webAddress
	 * @param isParticipatory
	 * @param memberCount
	 * @param foundedOn
	 * @param startDate
	 * @param speciesGroupIds
	 * @param habitatIds
	 */
	public UserGroupExpanded(Long id, String name, String icon, String webAddress, Boolean isParticipatory, Long memberCount, Date foundedOn, Date startDate, List<Long> speciesGroupIds, List<Long> habitatIds) {
		super();
		this.id = id;
		this.name = name;
		this.icon = icon;
		this.webAddress = webAddress;
		this.isParticipatory = isParticipatory;
		this.memberCount = memberCount;
		this.foundedOn = foundedOn;
		this.startDate = startDate;
		this.speciesGroupIds = speciesGroupIds;
		this.habitatIds = habitatIds;
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

	public Long getMemberCount() {
		return memberCount;
	}

	public void setMemberCount(Long memberCount) {
		this.memberCount = memberCount;
	}

	public Date getFoundedOn() {
		return foundedOn;
	}

	public void setFoundedOn(Date foundedOn) {
		this.foundedOn = foundedOn;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public List<Long> getSpeciesGroupIds() {
		return speciesGroupIds;
	}

	public void setSpeciesGroupIds(List<Long> speciesGroupIds) {
		this.speciesGroupIds = speciesGroupIds;
	}

	public List<Long> getHabitatIds() {
		return habitatIds;
	}

	public void setHabitatIds(List<Long> habitatIds) {
		this.habitatIds = habitatIds;
	}

}
