/**
 * 
 */
package com.strandls.userGroup.pojo;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * @author Abhishek Rudra
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserGroupCreateData {

//	core userGroup data
	private Boolean allowUserToJoin;
	private String description;
	private String homePage;
	private String icon;
	private String domainName;
	private String name;
	private List<Long> speciesGroup;
	private List<Long> habitatId;
	private Double neLatitude;
	private Double neLongitude;
	private Double swLatitude;
	private Double swLongitude;
	private String theme;
	private Long languageId;
	private Boolean showGallery;
	private Boolean showStats;
	private Boolean showDesc;
	private Boolean showRecentObservation;
	private Boolean showGridMap;
	private Boolean showPartners;
	private String mediaToggle;

//	Founder and Moderator Invitation List
	private UserGroupInvitationData invitationData;

	/**
	 * 
	 */
	public UserGroupCreateData() {
		super();
	}

	/**
	 * @param allowUserToJoin
	 * @param description
	 * @param homePage
	 * @param icon
	 * @param domainName
	 * @param name
	 * @param speciesGroup
	 * @param habitatId
	 * @param neLatitude
	 * @param neLongitude
	 * @param swLatitude
	 * @param swLongitude
	 * @param theme
	 * @param languageId
	 * @param invitationData
	 */
	public UserGroupCreateData(Boolean allowUserToJoin, String description, String homePage, String icon,
			String domainName, String name, List<Long> speciesGroup, List<Long> habitatId, Double neLatitude,
			Double neLongitude, Double swLatitude, Double swLongitude, String theme, Long languageId,
			UserGroupInvitationData invitationData, Boolean showGallery, Boolean showStats, Boolean showDesc,
			Boolean showRecentObservation, Boolean showGridMap, Boolean showPartners, String mediaToggle) {
		super();
		this.allowUserToJoin = allowUserToJoin;
		this.description = description;
		this.homePage = homePage;
		this.icon = icon;
		this.domainName = domainName;
		this.name = name;
		this.speciesGroup = speciesGroup;
		this.habitatId = habitatId;
		this.neLatitude = neLatitude;
		this.neLongitude = neLongitude;
		this.swLatitude = swLatitude;
		this.swLongitude = swLongitude;
		this.theme = theme;
		this.languageId = languageId;
		this.invitationData = invitationData;
		this.showDesc = showDesc;
		this.showGridMap = showGridMap;
		this.showGallery = showGallery;
		this.showPartners = showPartners;
		this.showRecentObservation = showRecentObservation;
		this.showStats = showStats;
		this.mediaToggle = mediaToggle;
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

	public List<Long> getSpeciesGroup() {
		return speciesGroup;
	}

	public void setSpeciesGroup(List<Long> speciesGroup) {
		this.speciesGroup = speciesGroup;
	}

	public List<Long> getHabitatId() {
		return habitatId;
	}

	public void setHabitatId(List<Long> habitatId) {
		this.habitatId = habitatId;
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

	public UserGroupInvitationData getInvitationData() {
		return invitationData;
	}

	public void setInvitationData(UserGroupInvitationData invitationData) {
		this.invitationData = invitationData;
	}

	public Boolean getShowGallery() {
		return showGallery;
	}

	public void setShowGallery(Boolean showGallery) {
		this.showGallery = showGallery;
	}
	
	public Boolean getShowStats() {
		return showStats;
	}

	public void setShowStats(Boolean showStats) {
		this.showStats = showStats;
	}
	
	public Boolean getShowDesc() {
		return showDesc;
	}

	public void setShowDesc(Boolean showDesc) {
		this.showDesc = showDesc;
	}
	
	public Boolean getShowRecentObservation() {
		return showRecentObservation;
	}

	public void setShowRecentObservation(Boolean showRecentObservation) {
		this.showRecentObservation = showRecentObservation;
	}
	
	public Boolean getShowGridMap() {
		return showGridMap;
	}

	public void setShowGridMap(Boolean showGridMap) {
		this.showGridMap = showGridMap;
	}
	
	public Boolean getShowPartners() {
		return showPartners;
	}

	public void setShowPartners(Boolean showPartners) {
		this.showPartners = showPartners;
	}
	
	public String getMediaToggle() {
		return mediaToggle;
	}
	
	public void setMediaToggle(String mediaToggle) {
		this.mediaToggle = mediaToggle;
	}
}
