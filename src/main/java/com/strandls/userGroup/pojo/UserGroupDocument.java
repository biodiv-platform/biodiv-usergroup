/** */
package com.strandls.userGroup.pojo;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;

/**
 * @author Abhishek Rudra
 */
@Entity
@Table(name = "user_group_documents")
@JsonIgnoreProperties(ignoreUnknown = true)
@IdClass(UserGroupDocumentCompositeKey.class)
public class UserGroupDocument implements Serializable {

	/** */
	private static final long serialVersionUID = 2436948398388938905L;

	private Long userGroupId;
	private Long documentId;

	/** */
	public UserGroupDocument() {
		super();
	}

	/**
	 * @param userGroupId
	 * @param documentId
	 */
	public UserGroupDocument(Long userGroupId, Long documentId) {
		super();
		this.userGroupId = userGroupId;
		this.documentId = documentId;
	}

	@Id
	@Column(name = "user_group_id")
	public Long getUserGroupId() {
		return userGroupId;
	}

	public void setUserGroupId(Long userGroupId) {
		this.userGroupId = userGroupId;
	}

	@Id
	@Column(name = "document_id")
	public Long getDocumentId() {
		return documentId;
	}

	public void setDocumentId(Long documentId) {
		this.documentId = documentId;
	}
}
