/**
 * 
 */
package com.strandls.userGroup.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.pac4j.core.profile.CommonProfile;

import com.strandls.userGroup.pojo.CustomFieldCreateData;
import com.strandls.userGroup.pojo.CustomFieldDetails;
import com.strandls.userGroup.pojo.CustomFieldEditData;
import com.strandls.userGroup.pojo.CustomFieldFactsInsertData;
import com.strandls.userGroup.pojo.CustomFieldObservationData;
import com.strandls.userGroup.pojo.CustomFieldPermission;
import com.strandls.userGroup.pojo.CustomFieldReordering;
import com.strandls.userGroup.pojo.CustomFieldUGData;
import com.strandls.userGroup.pojo.CustomFieldValues;
import com.strandls.userGroup.pojo.CustomFieldValuesCreateData;

/**
 * @author Abhishek Rudra
 *
 */
public interface CustomFieldServices {

	public List<CustomFieldObservationData> getObservationCustomFields(Long observationId);

	public List<CustomFieldValues> getCustomFieldOptions(HttpServletRequest request, CommonProfile profile,
			String observationId, Long userGroupId, Long customFieldId) throws Exception;

	public List<CustomFieldObservationData> insertUpdateCustomFieldData(HttpServletRequest request,
			CommonProfile profile, CustomFieldFactsInsertData factsCreateData) throws Exception;

	public List<CustomFieldDetails> createCustomFields(HttpServletRequest request, CommonProfile profile,
			CustomFieldCreateData customFieldCreateData);

	public List<CustomFieldPermission> getCustomFieldPermisison(HttpServletRequest request, CommonProfile profile,
			String observationId) throws Exception;

	public List<CustomFieldDetails> getAllCustomField();

	public List<CustomFieldDetails> getCustomField(HttpServletRequest request, CommonProfile profile, Long userGroupId);

	public List<CustomFieldDetails> addCustomFieldUG(HttpServletRequest request, CommonProfile profile, Long userId,
			Long userGroupId, List<CustomFieldUGData> customFieldUGDataList);

	public List<CustomFieldDetails> removeCustomField(HttpServletRequest request, CommonProfile profile,
			Long userGroupId, Long customFieldId);

	public List<CustomFieldDetails> reorderingCustomFields(HttpServletRequest request, Long userGroupId,
			List<CustomFieldReordering> customFieldReorderings);

	public List<CustomFieldDetails> addCustomFieldValues(HttpServletRequest request, Long customFieldId,
			Long userGroupId, CustomFieldValuesCreateData cfVCreateData);

	public List<CustomFieldDetails> editCustomFieldById(HttpServletRequest request, CommonProfile profile,Long ugID,
			Long cfId,CustomFieldEditData customFieldEditData);

}
