/** */
package com.strandls.userGroup.pojo;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * @author Abhishek Rudra
 */
@Entity
@Table(name = "featured")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Featured implements Serializable {

	/** */
	private static final long serialVersionUID = -3585218408042488760L;

	private Long id;
	private Long authorId;
	private Date createdOn;
	private String notes;
	private Long objectId;
	private String objectType;
	private Long userGroup;
	private Long languageId;

	/** */
	public Featured() {
		super();
	}

	/**
	 * @param id
	 * @param authorId
	 * @param createdOn
	 * @param notes
	 * @param objectId
	 * @param objectType
	 * @param userGroup
	 * @param languageId
	 */
	public Featured(Long id, Long authorId, Date createdOn, String notes, Long objectId, String objectType,
			Long userGroup, Long languageId) {
		super();
		this.id = id;
		this.authorId = authorId;
		this.createdOn = createdOn;
		this.notes = notes;
		this.objectId = objectId;
		this.objectType = objectType;
		this.userGroup = userGroup;
		this.languageId = languageId;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "id")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "author_id")
	public Long getAuthorId() {
		return authorId;
	}

	public void setAuthorId(Long authorId) {
		this.authorId = authorId;
	}

	@Column(name = "created_on", columnDefinition = "TIMESTAMP(6) WITHOUT TIME ZONE")
	public Date getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

	@Column(name = "notes")
	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	@Column(name = "object_id")
	public Long getObjectId() {
		return objectId;
	}

	public void setObjectId(Long objectId) {
		this.objectId = objectId;
	}

	@Column(name = "object_type")
	public String getObjectType() {
		return objectType;
	}

	public void setObjectType(String objectType) {
		this.objectType = objectType;
	}

	@Column(name = "user_group_id")
	public Long getUserGroup() {
		return userGroup;
	}

	public void setUserGroup(Long userGroup) {
		this.userGroup = userGroup;
	}

	@Column(name = "language_id")
	public Long getLanguageId() {
		return languageId;
	}

	public void setLanguageId(Long languageId) {
		this.languageId = languageId;
	}
}
