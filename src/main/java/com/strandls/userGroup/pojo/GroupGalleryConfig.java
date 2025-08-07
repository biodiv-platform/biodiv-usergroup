/**
*
*/
package com.strandls.userGroup.pojo;



import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


/**
* @author Mekala Rishitha Ravi
*
*/

@Entity
@Table(name = "group_gallery_config")
@JsonIgnoreProperties(ignoreUnknown = true)
public class GroupGalleryConfig {


   private Long id;
   private Boolean isActive;
   private Long slidesPerView;
   private String title;
   private Boolean isVertical;
   private Long languageId;
   private Long galleryId;
   private Long ugId;



   @Id
   @GeneratedValue(strategy = GenerationType.AUTO)
   @Column(name = "id", columnDefinition = "BIGINT")
   public Long getId() {
       return id;
   }


   public void setId(Long id) {
       this.id = id;
   }


   @Column(name = "is_active", columnDefinition = "BOOLEAN")
   public Boolean getIsActive() {
       return isActive;
   }


   public void setIsActive(Boolean isActive) {
       this.isActive = isActive;
   }

   @Column(name = "is_vertical", columnDefinition = "BOOLEAN")
   public Boolean getIsVertical() {
       return isVertical;
   }


   public void setIsVertical(Boolean isVertical) {
	   this.isVertical = isVertical;
   }

   @Column(name = "slides_per_view", columnDefinition = "BIGINT")
   public Long getSlidesPerView() {
       return slidesPerView;
   }


   public void setSlidesPerView(Long slidesPerView) {
       this.slidesPerView = slidesPerView;
   }

   @Column(name = "title")
   public String getTitle() {
       return title;
   }

   public void setTitle(String title) {
       this.title = title;
   }

   @Column(name = "language_id", columnDefinition = "BIGINT")
   public Long getLanguageId() {
       return languageId;
   }

   public void setLanguageId(Long languageId) {
       this.languageId = languageId;
   }

   @Column(name = "gallery_id", columnDefinition = "BIGINT")
   public Long getGalleryId() {
       return galleryId;
   }

   public void setGalleryId(Long galleryId) {
       this.galleryId = galleryId;
   }
   
   @Column(name = "ug_id", columnDefinition = "BIGINT")
   public Long getUgId() {
       return ugId;
   }

   public void setUgId(Long ugId) {
       this.ugId = ugId;
   }
   
}