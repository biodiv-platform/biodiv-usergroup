/** */
package com.strandls.userGroup.pojo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.bedatadriven.jackson.datatype.jts.serialization.GeometryDeserializer;
import com.bedatadriven.jackson.datatype.jts.serialization.GeometrySerializer;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.vividsolutions.jts.geom.Geometry;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

/**
 * @author Abhishek Rudra
 */
@Entity
@Table(name = "user_group")
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserGroup implements Serializable {

	/** */
	private static final long serialVersionUID = 9177200176991398786L;

	private Long id;
	private Boolean allow_members_to_make_species_call;
	private Boolean allow_non_members_to_comment;
	private Boolean allow_obv_cross_posting;
	private Boolean allowUserToJoin;
	private String description;
	private String domianName;
	private Date foundedOn;
	private String homePage;
	private String icon;
	private Boolean isDeleted;
	private String name;
	private String theme;
	private Long visitCount;
	private String webAddress;
	private Long languageId;
	private Date startDate;
	private Boolean showGallery;
	private Boolean showStats;
	private Boolean showRecentObservations;
	private Boolean showGridMap;
	private Boolean showPartners;
	private Boolean showDesc;
	private List<Long> habitatIds;
	private List<Long> speciesGroupIds;
	private String mediaToggle;
	private Long groupId;
	private Geometry spatialCoverage;

	/** */
	public UserGroup() {
		super();
	}

	/**
	 * @param id
	 * @param allow_members_to_make_species_call
	 * @param allow_non_members_to_comment
	 * @param allow_obv_cross_posting
	 * @param allowUserToJoin
	 * @param description
	 * @param domianName
	 * @param foundedOn
	 * @param homePage
	 * @param icon
	 * @param isDeleted
	 * @param name
	 * @param neLatitude
	 * @param neLongitude
	 * @param swLatitude
	 * @param swLongitude
	 * @param theme
	 * @param visitCount
	 * @param webAddress
	 * @param languageId
	 * @param startDate
	 * @param showGallery
	 * @param showStats
	 * @param showRecentObservations
	 * @param showGridMap
	 * @param showPartners
	 * @param showDesc
	 */
	public UserGroup(Long id, Boolean allow_members_to_make_species_call, Boolean allow_non_members_to_comment,
			Boolean allow_obv_cross_posting, Boolean allowUserToJoin, String description, String domianName,
			Date foundedOn, String homePage, String icon, Boolean isDeleted, String name, String theme, Long visitCount,
			String webAddress, Long languageId, Date startDate, Boolean showGallery, Boolean showStats,
			Boolean showRecentObservations, Boolean showGridMap, Boolean showPartners, Boolean showDesc,
			String mediaToggle, Long groupId, Geometry spatialCoverage) {
		super();
		this.id = id;
		this.allow_members_to_make_species_call = allow_members_to_make_species_call;
		this.allow_non_members_to_comment = allow_non_members_to_comment;
		this.allow_obv_cross_posting = allow_obv_cross_posting;
		this.allowUserToJoin = allowUserToJoin;
		this.description = description;
		this.domianName = domianName;
		this.foundedOn = foundedOn;
		this.homePage = homePage;
		this.icon = icon;
		this.isDeleted = isDeleted;
		this.name = name;
		this.theme = theme;
		this.visitCount = visitCount;
		this.webAddress = webAddress;
		this.languageId = languageId;
		this.startDate = startDate;
		this.showGallery = showGallery;
		this.showStats = showStats;
		this.showRecentObservations = showRecentObservations;
		this.showGridMap = showGridMap;
		this.showPartners = showPartners;
		this.showDesc = showDesc;
		this.mediaToggle = mediaToggle;
		this.groupId = groupId;
		this.spatialCoverage = spatialCoverage;
	}

	@Id
	@GeneratedValue
	@Column(name = "id")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "allow_members_to_make_species_call", columnDefinition = "boolean default true")
	public Boolean getAllow_members_to_make_species_call() {
		return allow_members_to_make_species_call;
	}

	public void setAllow_members_to_make_species_call(Boolean allow_members_to_make_species_call) {
		this.allow_members_to_make_species_call = allow_members_to_make_species_call;
	}

	@Column(name = "allow_non_members_to_comment", columnDefinition = "boolean default true")
	public Boolean getAllow_non_members_to_comment() {
		return allow_non_members_to_comment;
	}

	public void setAllow_non_members_to_comment(Boolean allow_non_members_to_comment) {
		this.allow_non_members_to_comment = allow_non_members_to_comment;
	}

	@Column(name = "allow_obv_cross_posting", columnDefinition = "boolean default true")
	public Boolean getAllow_obv_cross_posting() {
		return allow_obv_cross_posting;
	}

	public void setAllow_obv_cross_posting(Boolean allow_obv_cross_posting) {
		this.allow_obv_cross_posting = allow_obv_cross_posting;
	}

	@Column(name = "allow_users_to_join")
	public Boolean getAllowUserToJoin() {
		return allowUserToJoin;
	}

	public void setAllowUserToJoin(Boolean allowUserToJoin) {
		this.allowUserToJoin = allowUserToJoin;
	}

	@Column(name = "description")
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Column(name = "domain_name")
	public String getDomianName() {
		return domianName;
	}

	public void setDomianName(String domianName) {
		this.domianName = domianName;
	}

	@Column(name = "founded_on")
	public Date getFoundedOn() {
		return foundedOn;
	}

	public void setFoundedOn(Date foundedOn) {
		this.foundedOn = foundedOn;
	}

	@Column(name = "home_page")
	public String getHomePage() {
		return homePage;
	}

	public void setHomePage(String homePage) {
		this.homePage = homePage;
	}

	@Column(name = "icon")
	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	@Column(name = "is_deleted")
	public Boolean getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(Boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	@Column(name = "name")
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "theme")
	public String getTheme() {
		return theme;
	}

	public void setTheme(String theme) {
		this.theme = theme;
	}

	@Column(name = "visit_count")
	public Long getVisitCount() {
		return visitCount;
	}

	public void setVisitCount(Long visitCount) {
		this.visitCount = visitCount;
	}

	@Column(name = "webaddress")
	public String getWebAddress() {
		return webAddress;
	}

	public void setWebAddress(String webAddress) {
		this.webAddress = webAddress;
	}

	@Column(name = "language_id")
	public Long getLanguageId() {
		return languageId;
	}

	public void setLanguageId(Long languageId) {
		this.languageId = languageId;
	}

	@Column(name = "stat_start_date")
	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	@Column(name = "show_gallery", columnDefinition = "boolean default true")
	public Boolean getShowGallery() {
		return showGallery;
	}

	public void setShowGallery(Boolean showGallery) {
		this.showGallery = showGallery;
	}

	@Column(name = "show_stats", columnDefinition = "boolean default true")
	public Boolean getShowStats() {
		return showStats;
	}

	public void setShowStats(Boolean showStats) {
		this.showStats = showStats;
	}

	@Column(name = "show_recent_obv", columnDefinition = "boolean default true")
	public Boolean getShowRecentObservations() {
		return showRecentObservations;
	}

	public void setShowRecentObservations(Boolean showRecentObservations) {
		this.showRecentObservations = showRecentObservations;
	}

	@Column(name = "show_grid_map", columnDefinition = "boolean default true")
	public Boolean getShowGridMap() {
		return showGridMap;
	}

	public void setShowGridMap(Boolean showGridMap) {
		this.showGridMap = showGridMap;
	}

	@Column(name = "show_partners", columnDefinition = "boolean default true")
	public Boolean getShowPartners() {
		return showPartners;
	}

	public void setShowPartners(Boolean showPartners) {
		this.showPartners = showPartners;
	}

	@Column(name = "show_desc", columnDefinition = "boolean default false")
	public Boolean getShowDesc() {
		return showDesc;
	}

	public void setShowDesc(Boolean showDesc) {
		this.showDesc = showDesc;
	}

	@Transient
	public List<Long> getHabitatIds() {
		return habitatIds;
	}

	public void setHabitatIds(List<Long> habitatIds) {
		this.habitatIds = habitatIds;
	}

	@Transient
	public List<Long> getSpeciesGroupIds() {
		return speciesGroupIds;
	}

	public void setSpeciesGroupIds(List<Long> speciesGroupIds) {
		this.speciesGroupIds = speciesGroupIds;
	}

	@Column(name = "media_toggle", columnDefinition = "text default 'withMedia'")
	public String getMediaToggle() {
		return mediaToggle;
	}

	public void setMediaToggle(String mediaTogle) {
		this.mediaToggle = mediaTogle;
	}

	@Column(name = "group_id")
	public Long getGroupId() {
		return groupId;
	}

	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}

	@Column(name = "spatial_coverage", columnDefinition = "Geometry")
	@JsonSerialize(using = GeometrySerializer.class)
	@JsonDeserialize(contentUsing = GeometryDeserializer.class)
	public Geometry getSpatialCoverage() {
		return spatialCoverage;
	}

	public void setSpatialCoverage(Geometry spatialCoverage) {
		this.spatialCoverage = spatialCoverage;
	}

}
