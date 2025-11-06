package com.strandls.userGroup.controller;

import java.util.List;

import org.pac4j.core.profile.CommonProfile;

import com.strandls.authentication_utility.filter.ValidateUser;
import com.strandls.authentication_utility.util.AuthUtil;
import com.strandls.userGroup.ApiConstants;
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
import com.strandls.userGroup.service.CustomFieldServices;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

@Tag(name = "CustomField Service", description = "APIs for managing custom fields associated with user groups and observations")
@Path(ApiConstants.V1 + ApiConstants.CUSTOMFIELD)
@Produces(MediaType.APPLICATION_JSON)
public class CustomFieldController {

	@Inject
	private CustomFieldServices cfService;

	@GET
	@Path(ApiConstants.OBSERVATION + "/{observationId}")
	@Consumes(MediaType.TEXT_PLAIN)
	@Operation(summary = "Finds the Custom fields for the specified Observation", description = "Return all the Custom fields associated with an observation", responses = {
			@ApiResponse(responseCode = "200", description = "List of custom field observation data", content = @Content(array = @ArraySchema(schema = @Schema(implementation = CustomFieldObservationData.class)))),
			@ApiResponse(responseCode = "404", description = "Unable to retrieve the data", content = @Content(schema = @Schema(implementation = String.class))) })
	public Response getObservationCustomFields(
			@Parameter(description = "Observation ID", required = true) @PathParam("observationId") String observationId) {
		try {
			Long obvId = Long.parseLong(observationId);
			List<CustomFieldObservationData> result = cfService.getObservationCustomFields(obvId);
			return Response.status(Status.OK).entity(result).build();
		} catch (Exception e) {
			return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
		}
	}

	@GET
	@Path(ApiConstants.OPTIONS + "/{observationId}/{userGroupId}/{cfId}")
	@Consumes(MediaType.APPLICATION_JSON)
	@ValidateUser
	@Operation(summary = "Finds the set of Values for a Custom Field", responses = {
			@ApiResponse(responseCode = "200", description = "Set of custom field values", content = @Content(array = @ArraySchema(schema = @Schema(implementation = CustomFieldValues.class)))),
			@ApiResponse(responseCode = "400", description = "Unable to get the value list", content = @Content(schema = @Schema(implementation = String.class))) })
	public Response getCustomFieldOptions(@Context HttpServletRequest request,
			@Parameter(description = "Observation ID", required = true) @PathParam("observationId") String observationId,
			@Parameter(description = "UserGroup ID", required = true) @PathParam("userGroupId") String userGroupId,
			@Parameter(description = "Custom Field ID", required = true) @PathParam("cfId") String cfId) {
		try {
			CommonProfile profile = AuthUtil.getProfileFromRequest(request);
			Long customFieldId = Long.parseLong(cfId);
			Long ugId = Long.parseLong(userGroupId);
			List<CustomFieldValues> result = cfService.getCustomFieldOptions(request, profile, observationId, ugId,
					customFieldId);
			return Response.status(Status.OK).entity(result).build();
		} catch (Exception e) {
			return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
		}
	}

	@POST
	@Path(ApiConstants.INSERT)
	@Consumes(MediaType.APPLICATION_JSON)
	@ValidateUser
	@Operation(summary = "Insert/Update custom field Data", requestBody = @RequestBody(required = true, content = @Content(schema = @Schema(implementation = CustomFieldFactsInsertData.class))), responses = {
			@ApiResponse(responseCode = "200", description = "CustomField data for the Observation", content = @Content(array = @ArraySchema(schema = @Schema(implementation = CustomFieldObservationData.class)))),
			@ApiResponse(responseCode = "400", description = "Unable to add/Update the data", content = @Content(schema = @Schema(implementation = String.class))) })
	public Response addUpdateCustomFieldData(@Context HttpServletRequest request,
			CustomFieldFactsInsertData factsCreateData) {
		try {
			CommonProfile profile = AuthUtil.getProfileFromRequest(request);
			List<CustomFieldObservationData> result = cfService.insertUpdateCustomFieldData(request, profile,
					factsCreateData);
			return Response.status(Status.OK).entity(result).build();
		} catch (Exception e) {
			return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
		}
	}

	@POST
	@Path(ApiConstants.CREATE)
	@Consumes(MediaType.APPLICATION_JSON)
	@ValidateUser
	@Operation(summary = "Adds a new Custom Field", requestBody = @RequestBody(required = true, content = @Content(schema = @Schema(implementation = CustomFieldCreateData.class))), responses = {
			@ApiResponse(responseCode = "200", description = "Custom field details created", content = @Content(array = @ArraySchema(schema = @Schema(implementation = CustomFieldDetails.class)))),
			@ApiResponse(responseCode = "406", description = "Could not create the CustomField", content = @Content(schema = @Schema(implementation = String.class))),
			@ApiResponse(responseCode = "400", description = "Unable to create a new CustomField", content = @Content(schema = @Schema(implementation = String.class))) })
	public Response addNewCustomField(@Context HttpServletRequest request,
			CustomFieldCreateData customFieldCreateData) {
		try {
			CommonProfile profile = AuthUtil.getProfileFromRequest(request);
			List<CustomFieldDetails> result = cfService.createCustomFields(request, profile, customFieldCreateData);
			if (result != null)
				return Response.status(Status.OK).entity(result).build();
			return Response.status(Status.NOT_ACCEPTABLE).entity("Could not create the custom Field").build();
		} catch (Exception e) {
			return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
		}
	}

	@GET
	@Path(ApiConstants.PERMISSION + "/{observationId}")
	@Consumes(MediaType.APPLICATION_JSON)
	@ValidateUser
	@Operation(summary = "Checks the current user permission for custom Field", responses = {
			@ApiResponse(responseCode = "200", description = "List of custom field permission objects", content = @Content(array = @ArraySchema(schema = @Schema(implementation = CustomFieldPermission.class)))),
			@ApiResponse(responseCode = "400", description = "Unable to get the permission", content = @Content(schema = @Schema(implementation = String.class))) })
	public Response getCustomFieldPermission(@Context HttpServletRequest request,
			@Parameter(description = "Observation ID", required = true) @PathParam("observationId") String observationId) {
		try {
			CommonProfile profile = AuthUtil.getProfileFromRequest(request);
			List<CustomFieldPermission> result = cfService.getCustomFieldPermisison(request, profile, observationId);
			return Response.status(Status.OK).entity(result).build();
		} catch (Exception e) {
			return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
		}
	}

	@PUT
	@Path(ApiConstants.ADD + ApiConstants.VALUES + "/{ugId}/{cfId}")
	@Consumes(MediaType.APPLICATION_JSON)
	@ValidateUser
	@Operation(summary = "Add custom field values for categorical field type", requestBody = @RequestBody(required = true, content = @Content(schema = @Schema(implementation = CustomFieldValuesCreateData.class))), responses = {
			@ApiResponse(responseCode = "200", description = "List of custom field details for the user group", content = @Content(array = @ArraySchema(schema = @Schema(implementation = CustomFieldDetails.class)))),
			@ApiResponse(responseCode = "406", description = "Values not accepted", content = @Content(schema = @Schema(implementation = String.class))),
			@ApiResponse(responseCode = "400", description = "Unable to retrieve the data", content = @Content(schema = @Schema(implementation = String.class))) })
	public Response addCFValues(@Context HttpServletRequest request,
			@Parameter(description = "UserGroup ID", required = true) @PathParam("ugId") String ugId,
			@Parameter(description = "CustomField ID", required = true) @PathParam("cfId") String cfId,
			CustomFieldValuesCreateData cfVCreateData) {
		try {
			Long customFieldId = Long.parseLong(cfId);
			Long userGroupId = Long.parseLong(ugId);
			List<CustomFieldDetails> customField = cfService.addCustomFieldValues(request, customFieldId, userGroupId,
					cfVCreateData);
			if (customField != null)
				return Response.status(Status.OK).entity(customField).build();
			return Response.status(Status.NOT_ACCEPTABLE).build();
		} catch (Exception e) {
			return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
		}
	}

	@GET
	@Path(ApiConstants.GROUP + "/{userGroupId}")
	@Consumes(MediaType.APPLICATION_JSON)
	
	@Operation(summary = "Find all the custom field related with a userGroup", responses = {
			@ApiResponse(responseCode = "200", description = "All custom fields for a user group", content = @Content(array = @ArraySchema(schema = @Schema(implementation = CustomFieldDetails.class)))),
			@ApiResponse(responseCode = "406", description = "No custom fields for user group", content = @Content(schema = @Schema(implementation = String.class))),
			@ApiResponse(responseCode = "400", description = "Unable to retrieve the data", content = @Content(schema = @Schema(implementation = String.class))) })
	public Response getUserGroupCustomFields(@Context HttpServletRequest request,
			@Parameter(description = "UserGroup ID", required = true) @PathParam("userGroupId") String userGroupId) {
		try {
			CommonProfile profile = AuthUtil.getProfileFromRequest(request);
			Long ugId = Long.parseLong(userGroupId);
			List<CustomFieldDetails> customField = cfService.getCustomField(request, profile, ugId);
			if (customField != null)
				return Response.status(Status.OK).entity(customField).build();
			return Response.status(Status.NOT_ACCEPTABLE).build();
		} catch (Exception e) {
			return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
		}
	}

	@GET
	@Path(ApiConstants.ALL)
	@Consumes(MediaType.APPLICATION_JSON)
	@ValidateUser
	@Operation(summary = "Find all the custom fields", responses = {
			@ApiResponse(responseCode = "200", description = "All custom fields", content = @Content(array = @ArraySchema(schema = @Schema(implementation = CustomFieldDetails.class)))),
			@ApiResponse(responseCode = "400", description = "Unable to retrieve the data", content = @Content(schema = @Schema(implementation = String.class))) })
	public Response getAllCustomField(@Context HttpServletRequest request) {
		try {
			List<CustomFieldDetails> result = cfService.getAllCustomField();
			return Response.status(Status.OK).entity(result).build();
		} catch (Exception e) {
			return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
		}
	}

	@POST
	@Path(ApiConstants.ADD + "/{userGroupId}")
	@Consumes(MediaType.APPLICATION_JSON)
	@ValidateUser
	@Operation(summary = "Add an already existing customField to a UserGroup", requestBody = @RequestBody(required = true, content = @Content(array = @ArraySchema(schema = @Schema(implementation = CustomFieldUGData.class)))), responses = {
			@ApiResponse(responseCode = "200", description = "Updated custom field list for the userGroup", content = @Content(array = @ArraySchema(schema = @Schema(implementation = CustomFieldDetails.class)))),
			@ApiResponse(responseCode = "406", description = "User not allowed to add custom field", content = @Content(schema = @Schema(implementation = String.class))),
			@ApiResponse(responseCode = "400", description = "Unable to retrieve the data", content = @Content(schema = @Schema(implementation = String.class))) })
	public Response addCustomField(@Context HttpServletRequest request,
			@Parameter(description = "UserGroup ID", required = true) @PathParam("userGroupId") String userGroupId,
			List<CustomFieldUGData> customFieldUGDataList) {
		try {
			CommonProfile profile = AuthUtil.getProfileFromRequest(request);
			Long userId = Long.parseLong(profile.getId());
			Long ugId = Long.parseLong(userGroupId);
			List<CustomFieldDetails> result = cfService.addCustomFieldUG(request, profile, userId, ugId,
					customFieldUGDataList);
			if (result != null)
				return Response.status(Status.OK).entity(result).build();
			return Response.status(Status.NOT_ACCEPTABLE).entity("user not allowed to add custom field").build();
		} catch (Exception e) {
			return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
		}
	}

	@PUT
	@Path(ApiConstants.REMOVE + "/{userGroupId}/{customFieldId}")
	@Consumes(MediaType.APPLICATION_JSON)
	@ValidateUser
	@Operation(summary = "Remove a custom field related with a userGroup", responses = {
			@ApiResponse(responseCode = "200", description = "Custom fields after removal", content = @Content(array = @ArraySchema(schema = @Schema(implementation = CustomFieldDetails.class)))),
			@ApiResponse(responseCode = "406", description = "User not allowed to remove custom field", content = @Content(schema = @Schema(implementation = String.class))),
			@ApiResponse(responseCode = "400", description = "Unable to retrieve the data", content = @Content(schema = @Schema(implementation = String.class))) })
	public Response removeCustomField(@Context HttpServletRequest request,
			@Parameter(description = "UserGroup ID", required = true) @PathParam("userGroupId") String userGroupId,
			@Parameter(description = "CustomField ID", required = true) @PathParam("customFieldId") String customFieldId) {
		try {
			CommonProfile profile = AuthUtil.getProfileFromRequest(request);
			Long cfId = Long.parseLong(customFieldId);
			Long ugId = Long.parseLong(userGroupId);
			List<CustomFieldDetails> result = cfService.removeCustomField(request, profile, ugId, cfId);
			if (result != null)
				return Response.status(Status.OK).entity(result).build();
			return Response.status(Status.NOT_ACCEPTABLE).entity("USER NOT ALLOWED TO REMOVE CUSTOM FIELD").build();
		} catch (Exception e) {
			return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
		}
	}

	@PUT
	@Path(ApiConstants.REORDERING + "/{userGroupId}")
	@Consumes(MediaType.APPLICATION_JSON)
	@ValidateUser
	@Operation(summary = "Reorder custom fields for a user group", requestBody = @RequestBody(required = true, content = @Content(array = @ArraySchema(schema = @Schema(implementation = CustomFieldReordering.class)))), responses = {
			@ApiResponse(responseCode = "200", description = "Custom fields after reordering", content = @Content(array = @ArraySchema(schema = @Schema(implementation = CustomFieldDetails.class)))),
			@ApiResponse(responseCode = "406", description = "Not acceptable", content = @Content(schema = @Schema(implementation = String.class))),
			@ApiResponse(responseCode = "400", description = "Unable to retrieve the data", content = @Content(schema = @Schema(implementation = String.class))) })
	public Response reorderingCustomFields(@Context HttpServletRequest request,
			@Parameter(description = "UserGroup ID", required = true) @PathParam("userGroupId") String userGroupId,
			List<CustomFieldReordering> customFieldReorderings) {
		try {
			Long groupId = Long.parseLong(userGroupId);
			List<CustomFieldDetails> result = cfService.reorderingCustomFields(request, groupId,
					customFieldReorderings);
			if (result != null)
				return Response.status(Status.OK).entity(result).build();
			return Response.status(Status.NOT_ACCEPTABLE).build();
		} catch (Exception e) {
			return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
		}
	}

	@PUT
	@Path(ApiConstants.EDIT + "/{ugId}/{cfId}")
	@Consumes(MediaType.APPLICATION_JSON)
	@ValidateUser
	@Operation(summary = "Edit custom field data", requestBody = @RequestBody(required = true, content = @Content(schema = @Schema(implementation = CustomFieldEditData.class))), responses = {
			@ApiResponse(responseCode = "200", description = "Custom field data after edit", content = @Content(array = @ArraySchema(schema = @Schema(implementation = CustomFieldDetails.class)))),
			@ApiResponse(responseCode = "404", description = "Not found", content = @Content(schema = @Schema(implementation = String.class))),
			@ApiResponse(responseCode = "400", description = "Unable to retrieve the data", content = @Content(schema = @Schema(implementation = String.class))) })
	public Response editCustomFieldById(@Context HttpServletRequest request,
			@Parameter(description = "UserGroup ID", required = true) @PathParam("ugId") String userGroupId,
			@Parameter(description = "CustomField ID", required = true) @PathParam("cfId") String customFieldId,
			CustomFieldEditData editData) {
		try {
			CommonProfile profile = AuthUtil.getProfileFromRequest(request);
			Long ugId = Long.parseLong(userGroupId);
			Long cfId = Long.parseLong(customFieldId);
			List<CustomFieldDetails> result = cfService.editCustomFieldById(request, profile, ugId, cfId, editData);
			if (result != null)
				return Response.status(Status.OK).entity(result).build();
			return Response.status(Status.NOT_FOUND).build();
		} catch (Exception e) {
			return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
		}
	}
}
