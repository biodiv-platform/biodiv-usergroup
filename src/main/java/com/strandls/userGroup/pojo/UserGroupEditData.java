/**
 * 
 */
package com.strandls.userGroup.pojo;

import java.util.List;

/**
 * @author Abhishek Rudra
 *
 */
public class UserGroupEditData {

	private Boolean allowUserToJoin;
	private String description;
	private String homePage;
	private String icon;
	private String domainName;
	private String name;
	private Double neLatitude;
	private Double neLongitude;
	private Double swLatitude;
	private Double swLongitude;
	private String theme;
	private Long languageId;
	private List<Long> speciesGroupId;
	private List<Long> habitatId;

	/**
	 * 
	 */
	public UserGroupEditData() {
		super();
	}

	/**
	 * @param allowUserToJoin
	 * @param description
	 * @param homePage
	 * @param icon
	 * @param domainName
	 * @param name
	 * @param neLatitude
	 * @param neLongitude
	 * @param swLatitude
	 * @param swLongitude
	 * @param theme
	 * @param languageId
	 * @param speciesGroupId
	 * @param habitatId
	 */
	public UserGroupEditData(Boolean allowUserToJoin, String description, String homePage, String icon,
			String domainName, String name, Double neLatitude, Double neLongitude, Double swLatitude,
			Double swLongitude, String theme, Long languageId, List<Long> speciesGroupId, List<Long> habitatId) {
		super();
		this.allowUserToJoin = allowUserToJoin;
		this.description = description;
		this.homePage = homePage;
		this.icon = icon;
		this.domainName = domainName;
		this.name = name;
		this.neLatitude = neLatitude;
		this.neLongitude = neLongitude;
		this.swLatitude = swLatitude;
		this.swLongitude = swLongitude;
		this.theme = theme;
		this.languageId = languageId;
		this.speciesGroupId = speciesGroupId;
		this.habitatId = habitatId;
	}

	public Boolean getAllowUserToJoin() {
		return allowUserToJoin;
	}

	public void setAllowUserToJoin(Boolean allowUserToJoin) {
		this.allowUserToJoin = allowUserToJoin;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getHomePage() {
		return homePage;
	}

	public void setHomePage(String homePage) {
		this.homePage = homePage;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getDomainName() {
		return domainName;
	}

	public void setDomainName(String domainName) {
		this.domainName = domainName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Double getNeLatitude() {
		return neLatitude;
	}

	public void setNeLatitude(Double neLatitude) {
		this.neLatitude = neLatitude;
	}

	public Double getNeLongitude() {
		return neLongitude;
	}

	public void setNeLongitude(Double neLongitude) {
		this.neLongitude = neLongitude;
	}

	public Double getSwLatitude() {
		return swLatitude;
	}

	public void setSwLatitude(Double swLatitude) {
		this.swLatitude = swLatitude;
	}

	public Double getSwLongitude() {
		return swLongitude;
	}

	public void setSwLongitude(Double swLongitude) {
		this.swLongitude = swLongitude;
	}

	public String getTheme() {
		return theme;
	}

	public void setTheme(String theme) {
		this.theme = theme;
	}

	public Long getLanguageId() {
		return languageId;
	}

	public void setLanguageId(Long languageId) {
		this.languageId = languageId;
	}

	public List<Long> getSpeciesGroupId() {
		return speciesGroupId;
	}

	public void setSpeciesGroupId(List<Long> speciesGroupId) {
		this.speciesGroupId = speciesGroupId;
	}

	public List<Long> getHabitatId() {
		return habitatId;
	}

	public void setHabitatId(List<Long> habitatId) {
		this.habitatId = habitatId;
	}

}
