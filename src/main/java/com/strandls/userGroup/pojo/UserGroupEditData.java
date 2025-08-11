/**
 * 
 */
package com.strandls.userGroup.pojo;

import java.util.List;
import java.util.Map;

/**
 * @author Abhishek Rudra
 *
 */
public class UserGroupEditData {

	private Boolean allowUserToJoin;
	private String homePage;
	private String icon;
	private String domainName;
	private List<Map<String,Object>> translation;
	private String theme;
	private Long languageId;
	private List<Long> speciesGroupId;
	private List<Long> habitatId;
	private String webAddress;
	private String spatialData;

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
	public UserGroupEditData(Boolean allowUserToJoin, String homePage, String icon,
			String domainName, List<Map<String,Object>> translation, String theme, Long languageId, List<Long> speciesGroupId, List<Long> habitatId, String webAddress, String spatialData) {
		super();
		this.allowUserToJoin = allowUserToJoin;
		this.homePage = homePage;
		this.icon = icon;
		this.domainName = domainName;
		this.translation = translation;
		this.theme = theme;
		this.languageId = languageId;
		this.speciesGroupId = speciesGroupId;
		this.habitatId = habitatId;
		this.webAddress = webAddress;
		this.spatialData = spatialData;
	}

	public Boolean getAllowUserToJoin() {
		return allowUserToJoin;
	}

	public void setAllowUserToJoin(Boolean allowUserToJoin) {
		this.allowUserToJoin = allowUserToJoin;
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

	public String getWebAddress() {
		return webAddress;
	}

	public void setWebAddress(String webAddress) {
		this.webAddress = webAddress;
	}
	
	public List<Map<String,Object>> getTranslation() {
		return translation;
	}

	public void setTranslation(List<Map<String,Object>> translation) {
		this.translation = translation;
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
	
	public String getSpatialData() {
		return spatialData;
	}

	public void setSpatialData(String spatialData) {
		this.spatialData = spatialData;
	}

}
