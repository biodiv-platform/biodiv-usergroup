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
public class UserGroupHomePageEditData {

	private Boolean showGallery;
	private Boolean showStats;
	private Boolean showRecentObservation;
	private Boolean showGridMap;
	private Boolean showPartners;
	private Boolean showDesc;
	private String description;
	private List<Map<Long, List<GroupGallerySlider>>> gallerySlider;
	private Map<String, Map<Long, List<GroupGalleryConfig>>> miniGallery;
	private List<Map<String, Map<Long, List<MiniGroupGallerySlider>>>> miniGallerySlider;

	/**
	 * 
	 */
	public UserGroupHomePageEditData() {
		super();
	}

	/**
	 * @param showGallery
	 * @param showStats
	 * @param showRecentObservation
	 * @param showGridMap
	 * @param showPartners
	 * @param showDesc
	 * @param description
	 * @param gallerySlider
	 */
	public UserGroupHomePageEditData(Boolean showGallery, Boolean showStats, Boolean showRecentObservation,
			Boolean showGridMap, Boolean showPartners, Boolean showDesc, String description,
			List<Map<Long, List<GroupGallerySlider>>> gallerySlider,
			Map<String, Map<Long, List<GroupGalleryConfig>>> miniGallery,
			List<Map<String, Map<Long, List<MiniGroupGallerySlider>>>> miniGallerySlider) {
		super();
		this.showGallery = showGallery;
		this.showStats = showStats;
		this.showRecentObservation = showRecentObservation;
		this.showGridMap = showGridMap;
		this.showPartners = showPartners;
		this.showDesc = showDesc;
		this.description = description;
		this.gallerySlider = gallerySlider;
		this.miniGallery = miniGallery;
		this.miniGallerySlider = miniGallerySlider;
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

	public Boolean getShowDesc() {
		return showDesc;
	}

	public void setShowDesc(Boolean showDesc) {
		this.showDesc = showDesc;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<Map<Long, List<GroupGallerySlider>>> getGallerySlider() {
		return gallerySlider;
	}

	public void setGallerySlider(List<Map<Long, List<GroupGallerySlider>>> gallerySlider) {
		this.gallerySlider = gallerySlider;
	}
	
	public Map<String, Map<Long, List<GroupGalleryConfig>>> getMiniGallery() {
		return miniGallery;
	}

	public void setMiniGallery(Map<String, Map<Long, List<GroupGalleryConfig>>> miniGallery) {
		this.miniGallery = miniGallery;
	}

	public List<Map<String, Map<Long, List<MiniGroupGallerySlider>>>> getMiniGallerySlider() {
		return miniGallerySlider;
	}

	public void setMiniGallerySlider(List<Map<String, Map<Long, List<MiniGroupGallerySlider>>>> miniGallerySlider) {
		this.miniGallerySlider = miniGallerySlider;
	}

}
