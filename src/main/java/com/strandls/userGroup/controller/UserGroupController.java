/** */
package com.strandls.userGroup.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.pac4j.core.profile.CommonProfile;

import com.strandls.authentication_utility.filter.ValidateUser;
import com.strandls.authentication_utility.util.AuthUtil;
import com.strandls.userGroup.ApiConstants;
import com.strandls.userGroup.dto.AuthenticationDTO;
import com.strandls.userGroup.pojo.AdministrationList;
import com.strandls.userGroup.pojo.BulkGroupPostingData;
import com.strandls.userGroup.pojo.BulkGroupUnPostingData;
import com.strandls.userGroup.pojo.EncryptionKey;
import com.strandls.userGroup.pojo.Featured;
import com.strandls.userGroup.pojo.FeaturedCreateData;
import com.strandls.userGroup.pojo.GroupGalleryConfig;
import com.strandls.userGroup.pojo.GroupGallerySlider;
import com.strandls.userGroup.pojo.GroupHomePageData;
import com.strandls.userGroup.pojo.MiniGroupGallerySlider;
import com.strandls.userGroup.pojo.ObservationCustomisations;
import com.strandls.userGroup.pojo.ReorderingHomePage;
import com.strandls.userGroup.pojo.SField;
import com.strandls.userGroup.pojo.SpeciesFieldMetadata;
import com.strandls.userGroup.pojo.SpeciesFieldValuesDTO;
import com.strandls.userGroup.pojo.UserGroup;
import com.strandls.userGroup.pojo.UserGroupAddMemebr;
import com.strandls.userGroup.pojo.UserGroupAdminList;
import com.strandls.userGroup.pojo.UserGroupCreateData;
import com.strandls.userGroup.pojo.UserGroupCreateDatatable;
import com.strandls.userGroup.pojo.UserGroupDatatableFetch;
import com.strandls.userGroup.pojo.UserGroupDatatableMapping;
import com.strandls.userGroup.pojo.UserGroupDocCreateData;
import com.strandls.userGroup.pojo.UserGroupEditData;
import com.strandls.userGroup.pojo.UserGroupExpanded;
import com.strandls.userGroup.pojo.UserGroupHomePageEditData;
import com.strandls.userGroup.pojo.UserGroupIbp;
import com.strandls.userGroup.pojo.UserGroupInvitationData;
import com.strandls.userGroup.pojo.UserGroupMappingCreateData;
import com.strandls.userGroup.pojo.UserGroupObservation;
import com.strandls.userGroup.pojo.UserGroupPermissions;
import com.strandls.userGroup.pojo.UserGroupSpeciesCreateData;
import com.strandls.userGroup.pojo.UserGroupSpeciesFieldMeta;
import com.strandls.userGroup.pojo.UserGroupSpeciesGroup;
import com.strandls.userGroup.pojo.UsergroupSpeciesFieldMapping;
import com.strandls.userGroup.service.UserGroupDatatableService;
import com.strandls.userGroup.service.UserGroupMemberService;
import com.strandls.userGroup.service.UserGroupSerivce;
import com.strandls.userGroup.util.AppUtil;
import com.strandls.userGroup.util.PropertyFileUtil;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.NewCookie;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.ResponseBuilder;
import jakarta.ws.rs.core.Response.Status;
import net.minidev.json.JSONArray;

/**
 * @author Abhishek Rudra
 */
@Tag(name = "UserGroup Service", description = "APIs for user group operations")
@Path(ApiConstants.V1 + ApiConstants.GROUP)
@Produces(MediaType.APPLICATION_JSON)
public class UserGroupController {

	@Inject
	private UserGroupSerivce ugServices;

	@Inject
	private UserGroupDatatableService udDatatableService;

	@Inject
	private UserGroupMemberService ugMemberService;
	private static final String ERROR_OCCURED_IN_TRANSACTION = "Error occured in transaction";

	@GET
	@Path("/ping")
	@Produces(MediaType.TEXT_PLAIN)
	@Operation(summary = "Ping endpoint for the user group service", description = "Returns 'PONG' if the service is running", responses = @ApiResponse(responseCode = "200", description = "pong", content = @Content(schema = @Schema(implementation = String.class))))
	public Response pong() {
		return Response.status(Status.OK).entity("PONG").build();
	}

	@GET
	@Path("/{objectId}/{languageId}")
	@Consumes(MediaType.TEXT_PLAIN)
	@Operation(summary = "Find UserGroup by ID", description = "Returns UserGroup details", responses = {
			@ApiResponse(responseCode = "200", description = "UserGroup details", content = @Content(schema = @Schema(implementation = UserGroup.class))),
			@ApiResponse(responseCode = "404", description = "UserGroup not found", content = @Content(schema = @Schema(implementation = String.class))) })
	public Response getUserGroup(
			@Parameter(description = "UserGroup object ID", required = true) @PathParam("objectId") String objectId,
			@PathParam("languageId") String languageId) {
		try {
			Long id = Long.parseLong(objectId);
			Long langId = Long.parseLong(languageId);
			UserGroup userGroup = ugServices.fetchByGroupId(id, langId);
			return Response.status(Status.OK).entity(userGroup).build();
		} catch (Exception e) {
			return Response.status(Status.BAD_REQUEST).build();
		}
	}

	@GET
	@Path(ApiConstants.MEDIATOGGLE + "/{ugId}")
	@Consumes(MediaType.TEXT_PLAIN)
	@Operation(summary = "Find media toggle value of a UserGroup by ID", description = "Returns all observations customisation for ug as response", responses = {
			@ApiResponse(responseCode = "200", description = "Observation customisations", content = @Content(schema = @Schema(implementation = ObservationCustomisations.class))),
			@ApiResponse(responseCode = "404", description = "UserGroup not found", content = @Content(schema = @Schema(implementation = String.class))) })
	public Response getUserGroupMediaToggle(
			@Parameter(description = "UserGroup ID", required = true) @PathParam("ugId") String ugId) {
		try {
			ObservationCustomisations ugObsCustomisations = ugServices.fetchMediaToggle(Long.parseLong(ugId));
			return Response.status(Status.OK).entity(ugObsCustomisations).build();
		} catch (Exception e) {
			return Response.status(Status.BAD_REQUEST).build();
		}
	}

	@PUT
	@Path(ApiConstants.OBSERVATIONCUSTOMISATIONS + ApiConstants.UPDATE)
	@Consumes(MediaType.APPLICATION_JSON)
	@Operation(summary = "Update media toggle value of a UserGroup", description = "Update media toggle value of a UserGroup", responses = {
			@ApiResponse(responseCode = "200", description = "UserGroup updated", content = @Content(schema = @Schema(implementation = UserGroup.class))),
			@ApiResponse(responseCode = "404", description = "UserGroup not found", content = @Content(schema = @Schema(implementation = String.class))) })
	public Response updateGroupObservationCustomisations(@Context HttpServletRequest request,
			@Parameter(description = "Observation customisations") ObservationCustomisations observationCustomisations) {
		try {
			CommonProfile profile = AuthUtil.getProfileFromRequest(request);
			JSONArray roles = (JSONArray) profile.getAttribute("roles");
			if (roles.contains("ROLE_ADMIN")) {
				UserGroup ug = ugServices.updateObservationCustomisations(observationCustomisations);
				return Response.status(Status.OK).entity(ug).build();
			} else {
				return Response.status(Status.FORBIDDEN).build();
			}

		} catch (Exception e) {
			return Response.status(Status.BAD_REQUEST).build();
		}
	}

	@GET
	@Path(ApiConstants.IBP + "/{objectId}")
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces(MediaType.APPLICATION_JSON)
	@Operation(summary = "Find UserGroup by ID", description = "Returns UserGroup details for IBP", responses = {
			@ApiResponse(responseCode = "200", description = "UserGroup details for IBP", content = @Content(schema = @Schema(implementation = UserGroupIbp.class))),
			@ApiResponse(responseCode = "404", description = "UserGroup not found", content = @Content(schema = @Schema(implementation = String.class))) })
	public Response getIbpData(@PathParam("objectId") String objectId) {
		try {
			Long id = Long.parseLong(objectId);
			UserGroupIbp ibp = ugServices.fetchByGroupIdIbp(id);
			return Response.status(Status.OK).entity(ibp).build();
		} catch (Exception e) {
			return Response.status(Status.BAD_REQUEST).build();
		}
	}

	@GET
	@Path(ApiConstants.OBSERVATION + "/{observationId}")
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces(MediaType.APPLICATION_JSON)
	@Operation(summary = "Find UserGroup by observation ID", description = "Returns UserGroup Details", responses = {
			@ApiResponse(responseCode = "200", description = "UserGroup Details", content = @Content(array = @ArraySchema(schema = @Schema(implementation = UserGroupIbp.class)))),
			@ApiResponse(responseCode = "404", description = "UserGroup not found", content = @Content(schema = @Schema(implementation = String.class))) })
	public Response getObservationUserGroup(@PathParam("observationId") String observationId) {
		try {
			Long id = Long.parseLong(observationId);
			List<UserGroupIbp> userGroup = ugServices.fetchByObservationId(id);
			return Response.status(Status.OK).entity(userGroup).build();
		} catch (Exception e) {
			return Response.status(Status.BAD_REQUEST).build();
		}
	}

	@GET
	@Path(ApiConstants.ALL + ApiConstants.OBSERVATION + "/{userGroupId}")
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces(MediaType.APPLICATION_JSON)
	@Operation(summary = "Find all observation related to a userGroup", description = "Return list of observation associated with a userGroup", responses = {
			@ApiResponse(responseCode = "200", description = "Observation list", content = @Content(array = @ArraySchema(schema = @Schema(implementation = Long.class)))),
			@ApiResponse(responseCode = "400", description = "Observation list not found", content = @Content(schema = @Schema(implementation = String.class))) })
	public Response getAllObservation(@PathParam("userGroupId") String groupId) {
		try {
			Long userGroupId = Long.parseLong(groupId);
			List<Long> result = ugServices.findAllObservation(userGroupId);
			return Response.status(Status.OK).entity(result).build();

		} catch (Exception e) {
			return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
		}
	}

	@GET
	@Path(ApiConstants.GROUPLIST)
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces(MediaType.APPLICATION_JSON)
	@Operation(summary = "Find list of UserGroup based on List of UserGroupId", description = "Return UserGroup Details", responses = {
			@ApiResponse(responseCode = "200", description = "UserGroup list", content = @Content(array = @ArraySchema(schema = @Schema(implementation = UserGroupIbp.class)))),
			@ApiResponse(responseCode = "404", description = "UserGroup Not Found ", content = @Content(schema = @Schema(implementation = String.class))) })
	public Response getUserGroupList(
			@Parameter(description = "Comma separated list of UserGroup member IDs") @QueryParam("userGroupMember") String userGroupMember) {
		try {
			String[] userGroupRole = userGroupMember.split(",");
			List<Long> memberList = new ArrayList<Long>();
			for (String s : userGroupRole)
				memberList.add(Long.parseLong(s.trim()));

			List<UserGroupIbp> userGroupList = ugServices.fetchByUserGroupDetails(memberList);
			return Response.status(Status.OK).entity(userGroupList).build();
		} catch (Exception e) {
			return Response.status(Status.BAD_REQUEST).build();
		}
	}

	@POST
	@Path(ApiConstants.CREATE + "/{obsId}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@ValidateUser
	@Operation(summary = "Create Observation UserGroup Mapping", description = "Returns List of UserGroup", responses = {
			@ApiResponse(responseCode = "201", description = "UserGroup mapping created", content = @Content(array = @ArraySchema(schema = @Schema(implementation = Long.class)))),
			@ApiResponse(responseCode = "404", description = "UserGroup Not Found ", content = @Content(schema = @Schema(implementation = String.class))),
			@ApiResponse(responseCode = "409", description = "UserGroup-Observation Mapping Cannot be Created", content = @Content(schema = @Schema(implementation = String.class))) })
	public Response createObservationUserGroupMapping(@Context HttpServletRequest request,
			@PathParam("obsId") String obsId,
			@Parameter(description = "UserGroup data") UserGroupMappingCreateData userGroupData) {
		try {

			Long observationId = Long.parseLong(obsId);
			List<Long> result = ugServices.createUserGroupObservationMapping(request, observationId, userGroupData,
					true, userGroupData.getHasActivity() != null ? userGroupData.getHasActivity() : true);
			if (result == null)
				return Response.status(Status.CONFLICT).entity(ERROR_OCCURED_IN_TRANSACTION).build();
			return Response.status(Status.CREATED).entity(result).build();

		} catch (Exception e) {
			return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
		}
	}

	@POST
	@Path(ApiConstants.CREATE + "/datatable" + "/{obsId}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@ValidateUser
	@Operation(summary = "Create Observation UserGroup Mapping", description = "Returns List of UserGroup", responses = {
			@ApiResponse(responseCode = "201", description = "UserGroup mapping created", content = @Content(array = @ArraySchema(schema = @Schema(implementation = Long.class)))),
			@ApiResponse(responseCode = "404", description = "UserGroup Not Found ", content = @Content(schema = @Schema(implementation = String.class))),
			@ApiResponse(responseCode = "409", description = "UserGroup-Observation Mapping Cannot be Created", content = @Content(schema = @Schema(implementation = String.class))) })
	public Response createObservationUserGroupMappingDatatable(@Context HttpServletRequest request,
			@PathParam("obsId") String obsId,
			@Parameter(description = "UserGroup data") UserGroupMappingCreateData userGroupData) {
		try {

			Long observationId = Long.parseLong(obsId);
			List<Long> result = ugServices.createUserGroupObservationMapping(request, observationId, userGroupData,
					false, userGroupData.getHasActivity() != null ? userGroupData.getHasActivity() : true);
			if (result == null)
				return Response.status(Status.CONFLICT).entity(ERROR_OCCURED_IN_TRANSACTION).build();
			return Response.status(Status.CREATED).entity(result).build();

		} catch (Exception e) {
			return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
		}
	}

	@PUT
	@Path(ApiConstants.UPDATE + ApiConstants.OBSERVATION + "/{observationId}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@ValidateUser
	@Operation(summary = "Update the UserGroup Observation Mapping", description = "Returns the List of UserGroup Linked", responses = {
			@ApiResponse(responseCode = "200", description = "UserGroup mapping updated", content = @Content(array = @ArraySchema(schema = @Schema(implementation = UserGroupIbp.class)))),
			@ApiResponse(responseCode = "400", description = "Unable to Update the UserGroup Observation Mapping", content = @Content(schema = @Schema(implementation = String.class))) })
	public Response updateUserGroupMapping(@Context HttpServletRequest request,
			@PathParam("observationId") String observationId,
			@Parameter(description = "UserGroup data") UserGroupMappingCreateData userGroup) {
		try {
			Long obvId = Long.parseLong(observationId);

			List<UserGroupIbp> result = ugServices.updateUserGroupObservationMapping(request, obvId, userGroup);
			return Response.status(Status.OK).entity(result).build();
		} catch (Exception e) {
			return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
		}
	}

	@GET
	@Path(ApiConstants.ALL)
	@Produces(MediaType.APPLICATION_JSON)
	@Operation(summary = "Find all the UserGroups", description = "Returns all the UserGroups", responses = {
			@ApiResponse(responseCode = "200", description = "UserGroup list", content = @Content(array = @ArraySchema(schema = @Schema(implementation = UserGroupIbp.class)))),
			@ApiResponse(responseCode = "404", description = "Unable to fetch the UserGroups", content = @Content(schema = @Schema(implementation = String.class))) })
	public Response getAllUserGroup(@QueryParam("languageId") String languageId) {
		try {
			if (languageId == null || languageId.isEmpty()) {
				languageId = PropertyFileUtil.fetchProperty("config.properties", "defaultLanguageId");
			}
			Long langId = Long.parseLong(languageId);
			List<UserGroupIbp> result = ugServices.fetchAllUserGroup(langId);
			return Response.status(Status.OK).entity(result).build();
		} catch (Exception e) {
			return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
		}
	}

	@GET
	@Path(ApiConstants.LIST + "/{languageId}")
	@Produces(MediaType.APPLICATION_JSON)
	@Operation(summary = "Find all the UserGroups for list page", description = "Returns all the UserGroups for list page", responses = {
			@ApiResponse(responseCode = "200", description = "UserGroupExpanded list", content = @Content(array = @ArraySchema(schema = @Schema(implementation = UserGroupExpanded.class)))),
			@ApiResponse(responseCode = "404", description = "Unable to fetch the UserGroups list", content = @Content(schema = @Schema(implementation = String.class))) })
	public Response getAllUserGroupList(@PathParam("languageId") String languageId) {
		try {
			Long langId = Long.parseLong(languageId);
			List<UserGroupExpanded> result = ugServices.fetchAllUserGroupExpanded(langId);
			return Response.status(Status.OK).entity(result).build();
		} catch (Exception e) {
			return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
		}
	}

	@GET
	@Path(ApiConstants.FEATURED + "/{objectType}/{objectId}")
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces(MediaType.APPLICATION_JSON)
	@Operation(summary = "Find Featured", description = "Return list Featured", responses = {
			@ApiResponse(responseCode = "200", description = "Featured list", content = @Content(array = @ArraySchema(schema = @Schema(implementation = Featured.class)))),
			@ApiResponse(responseCode = "400", description = "Featured not Found", content = @Content(schema = @Schema(implementation = String.class))) })
	public Response getAllFeatured(@PathParam("objectType") String objectType, @PathParam("objectId") String objectId) {

		try {
			Long id = Long.parseLong(objectId);
			List<Featured> featuredList = ugServices.fetchFeatured(objectType, id);
			return Response.status(Status.OK).entity(featuredList).build();
		} catch (Exception e) {
			return Response.status(Status.BAD_REQUEST).build();
		}
	}

	@POST
	@Path(ApiConstants.FEATURED)
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@ValidateUser
	@Operation(summary = "Posting of Featured to a Group", description = "Returns the Details of Featured", responses = {
			@ApiResponse(responseCode = "200", description = "Featured posted", content = @Content(array = @ArraySchema(schema = @Schema(implementation = Featured.class)))),
			@ApiResponse(responseCode = "404", description = "Unable to Feature in a Group", content = @Content(schema = @Schema(implementation = String.class))) })
	public Response createFeatured(@Context HttpServletRequest request,
			@Parameter(description = "Featured create data") FeaturedCreateData featuredCreate) {

		try {
			CommonProfile profile = AuthUtil.getProfileFromRequest(request);
			Long userId = Long.parseLong(profile.getId());
			List<Featured> result = ugServices.createFeatured(request, userId, featuredCreate);
			return Response.status(Status.OK).entity(result).build();
		} catch (Exception e) {
			return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
		}
	}

	@PUT
	@Path(ApiConstants.UNFEATURED + "/{objectType}/{objectId}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@ValidateUser
	@Operation(summary = "UnFeatures a Object from a UserGroup", description = "Returns the Current Featured", responses = {
			@ApiResponse(responseCode = "200", description = "Featured unfeatured", content = @Content(array = @ArraySchema(schema = @Schema(implementation = Featured.class)))),
			@ApiResponse(responseCode = "404", description = "Unable to Unfeature", content = @Content(schema = @Schema(implementation = String.class))) })
	public Response unFeatured(@Context HttpServletRequest request, @PathParam("objectType") String objectType,
			@PathParam("objectId") String objectId,
			@Parameter(description = "UserGroup list") UserGroupMappingCreateData userGroupList) {
		try {

			CommonProfile profile = AuthUtil.getProfileFromRequest(request);
			Long userId = Long.parseLong(profile.getId());
			Long objId = Long.parseLong(objectId);
			List<Featured> result = ugServices.removeFeatured(request, userId, objectType, objId, userGroupList);
			return Response.status(Status.OK).entity(result).build();
		} catch (Exception e) {
			return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
		}
	}

	@GET
	@Path(ApiConstants.SPECIESGROUP + "/{userGroupId}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Operation(summary = "Get the species Group for a userGroup", description = "Returns the species Group for a userGroup", responses = {
			@ApiResponse(responseCode = "200", description = "Species group list", content = @Content(array = @ArraySchema(schema = @Schema(implementation = UserGroupSpeciesGroup.class)))),
			@ApiResponse(responseCode = "400", description = "Unable to retireve the data", content = @Content(schema = @Schema(implementation = String.class))) })
	public Response getUserGroupSGroup(@PathParam("userGroupId") String userGroupId) {
		try {
			Long ugId = Long.parseLong(userGroupId);
			List<UserGroupSpeciesGroup> result = ugServices.getUserGroupSpeciesGroup(ugId);
			return Response.status(Status.OK).entity(result).build();
		} catch (Exception e) {
			return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
		}
	}

	@POST
	@Path(ApiConstants.ADD + ApiConstants.MEMBERS)
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@ValidateUser
	@Operation(summary = "Sends out invitaions for founder and moedrators", description = "Returns the success and failur", responses = {
			@ApiResponse(responseCode = "200", description = "Invitations sent", content = @Content(schema = @Schema(implementation = String.class))),
			@ApiResponse(responseCode = "400", description = "Unable to send the invitaions", content = @Content(schema = @Schema(implementation = String.class))) })
	public Response addUserGroupMember(@Context HttpServletRequest request,
			@Parameter(description = "UserGroup invitations data") UserGroupInvitationData userGroupInvitations) {
		try {
			CommonProfile profile = AuthUtil.getProfileFromRequest(request);
			Boolean result = ugServices.addMemberRoleInvitaions(request, profile, userGroupInvitations);
			if (result)
				return Response.status(Status.OK).entity("Sent out Invitations to all").build();
			return Response.status(Status.NOT_ACCEPTABLE).entity("User not allowed to send invitations").build();
		} catch (Exception e) {
			return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
		}
	}

	@GET
	@Path(ApiConstants.ADMINSTRATION + ApiConstants.MEMBERS + "/{userGroupId}")
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces(MediaType.APPLICATION_JSON)
	@Operation(summary = "find the founder and moderator list", description = "Return the founder and moderator list", responses = {
			@ApiResponse(responseCode = "200", description = "Administration member list", content = @Content(schema = @Schema(implementation = AdministrationList.class))),
			@ApiResponse(responseCode = "400", description = "unable to find the data", content = @Content(schema = @Schema(implementation = String.class))) })
	public Response getAdminstrationMember(@PathParam("userGroupId") String groupId) {
		try {
			AdministrationList result = ugServices.getAdminMembers(groupId);
			if (result != null)
				return Response.status(Status.OK).entity(result).build();
			return Response.status(Status.NOT_FOUND).build();
		} catch (Exception e) {
			return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
		}
	}

	@POST
	@Path(ApiConstants.VALIDATE + ApiConstants.REQUEST)
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@ValidateUser
	@Operation(summary = "validate the join request for closed groups", description = "In success returns the usergroup data", responses = {
			@ApiResponse(responseCode = "200", description = "Join request validated", content = @Content(schema = @Schema(implementation = UserGroupIbp.class))),
			@ApiResponse(responseCode = "400", description = "unable to validate the request", content = @Content(schema = @Schema(implementation = String.class))) })
	public Response validateJoinRequest(@Context HttpServletRequest request,
			@Parameter(description = "Encryption key data") EncryptionKey encryptionKey) {
		try {
			UserGroupIbp result = ugServices.validateJoinRequest(request, encryptionKey.getToken());
			if (result != null)
				return Response.status(Status.OK).entity(result).build();
			return Response.status(Status.METHOD_NOT_ALLOWED).build();

		} catch (Exception e) {
			return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
		}
	}

	@GET
	@Path(ApiConstants.REMOVE + ApiConstants.MEMBERS)
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces(MediaType.TEXT_PLAIN)
	@ValidateUser
	@Operation(summary = "remove a existing user from the group", description = "remove existing user", responses = {
			@ApiResponse(responseCode = "200", description = "User removed", content = @Content(schema = @Schema(implementation = String.class))),
			@ApiResponse(responseCode = "400", description = "unable to remove the user", content = @Content(schema = @Schema(implementation = String.class))) })
	public Response removeUserUG(@Context HttpServletRequest request, @QueryParam("userId") String userId,
			@QueryParam("userGroupId") String userGroupId) {
		try {
			Boolean result = ugServices.removeUser(request, userGroupId, userId);
			if (result)
				return Response.status(Status.OK).entity("Removed user").build();
			return Response.status(Status.NOT_ACCEPTABLE).entity("User Not removed").build();
		} catch (Exception e) {
			return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
		}
	}

	@PUT
	@Path(ApiConstants.REMOVE + ApiConstants.BULK + ApiConstants.MEMBERS)
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@ValidateUser
	@Operation(summary = "remove list of existing users from the group", description = "remove existing users", responses = {
			@ApiResponse(responseCode = "200", description = "Users removed", content = @Content(schema = @Schema(implementation = String.class))),
			@ApiResponse(responseCode = "400", description = "unable to remove the users", content = @Content(schema = @Schema(implementation = String.class))) })
	public Response removeBulkUserUG(@Context HttpServletRequest request,
			@DefaultValue("false") @QueryParam("selectAll") Boolean selectAll, @QueryParam("userIds") String userIds,
			@QueryParam("userGroupId") String userGroupId) {
		try {

			Boolean result = ugServices.removeBulkUser(request, userGroupId, userIds);
			if (Boolean.TRUE.equals(result))
				return Response.status(Status.OK).entity("Removed users").build();
			return Response.status(Status.NOT_ACCEPTABLE).entity("User Not removed").build();
		} catch (Exception e) {
			return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
		}
	}

	@DELETE
	@Path(ApiConstants.LEAVE + "/{userGroupId}")
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces(MediaType.TEXT_PLAIN)
	@ValidateUser
	@Operation(summary = "endpoint to leave a group", description = "leave group", responses = {
			@ApiResponse(responseCode = "200", description = "User left the group", content = @Content(schema = @Schema(implementation = String.class))),
			@ApiResponse(responseCode = "400", description = "unable to leave the group", content = @Content(schema = @Schema(implementation = String.class))) })
	public Response leaveUserGroup(@Context HttpServletRequest request, @PathParam("userGroupId") String userGroupId) {
		try {
			CommonProfile profile = AuthUtil.getProfileFromRequest(request);
			Long userId = Long.parseLong(profile.getId());
			Boolean result = ugServices.leaveGroup(request, userId, userGroupId);
			if (result)
				return Response.status(Status.OK).entity("User left the group").build();
			return Response.status(Status.NOT_ACCEPTABLE).entity("NOt able to leave the group").build();
		} catch (Exception e) {
			return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
		}
	}

	@GET
	@Path(ApiConstants.JOIN + "/{userGroupId}")
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces(MediaType.TEXT_PLAIN)
	@ValidateUser
	@Operation(summary = "endpoint to join open group", description = "User can join open group without invitation", responses = {
			@ApiResponse(responseCode = "200", description = "User joined the Group", content = @Content(schema = @Schema(implementation = String.class))),
			@ApiResponse(responseCode = "400", description = "unable to join the userGroup", content = @Content(schema = @Schema(implementation = String.class))) })
	public Response joinUserGroup(@Context HttpServletRequest request, @PathParam("userGroupId") String userGroupId) {
		try {
			CommonProfile profile = AuthUtil.getProfileFromRequest(request);
			Long userId = Long.parseLong(profile.getId());
			Boolean result = ugServices.joinGroup(request, userId, userGroupId);
			if (result)
				return Response.status(Status.OK).entity("User joined the Group").build();
			return Response.status(Status.NOT_ACCEPTABLE).entity("User not able to join the Group").build();

		} catch (Exception e) {
			return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
		}
	}

	@POST
	@Path(ApiConstants.SEND + ApiConstants.INVITES + "/{userGroupId}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@ValidateUser
	@io.swagger.v3.oas.annotations.Operation(summary = "Send invites for Role Member in UserGroup", description = "Sends invitation mails for joining group as Member role", requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(required = true, description = "List of user IDs to invite", content = @io.swagger.v3.oas.annotations.media.Content(array = @io.swagger.v3.oas.annotations.media.ArraySchema(schema = @io.swagger.v3.oas.annotations.media.Schema(type = "integer", format = "int64")))), responses = {
			@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Invitations sent", content = @io.swagger.v3.oas.annotations.media.Content(schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = String.class))),
			@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "406", description = "Invitation sending caused problem", content = @io.swagger.v3.oas.annotations.media.Content(schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = String.class))),
			@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Unable to send invites (bad request or error)", content = @io.swagger.v3.oas.annotations.media.Content(schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = String.class))) })
	public Response sendInvitesForMemberRole(@Context HttpServletRequest request,
			@io.swagger.v3.oas.annotations.Parameter(description = "User Group ID", required = true) @PathParam("userGroupId") String userGroupId,
			List<Long> userList) {
		try {
			CommonProfile profile = AuthUtil.getProfileFromRequest(request);
			Long ugId = Long.parseLong(userGroupId);
			Boolean result = ugServices.sendInvitesForMemberRole(request, profile, ugId, userList);
			if (result != null)
				return Response.status(Response.Status.OK).entity("Invitation Sent out").build();
			return Response.status(Response.Status.NOT_ACCEPTABLE).entity("Invitation Sending caused Problem").build();
		} catch (Exception e) {
			return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
		}
	}

	@POST
	@Path(ApiConstants.BULK + ApiConstants.POSTING)
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@ValidateUser
	@Operation(summary = "Bulk Posting of observation in a UserGroup", description = "Returns the success failuer result", responses = {
			@ApiResponse(responseCode = "200", description = "Bulk Posting completed", content = @Content(schema = @Schema(implementation = String.class))),
			@ApiResponse(responseCode = "400", description = "Unable to do Bulk Posting", content = @Content(schema = @Schema(implementation = String.class))) })
	public Response bulkPostingObservationUG(@Context HttpServletRequest request,
			@Parameter(name = "bulkGroupPosting") BulkGroupPostingData bulkGroupPostingData) {

		try {

			CommonProfile profile = AuthUtil.getProfileFromRequest(request);
			Boolean result = ugServices.bulkPosting(request, profile, bulkGroupPostingData);
			if (result)
				return Response.status(Status.OK).entity("Bulk Posting completed").build();
			return Response.status(Status.NOT_ACCEPTABLE).entity("Bulk posting failed").build();

		} catch (Exception e) {
			return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
		}
	}

	@POST
	@Path(ApiConstants.BULK + ApiConstants.REMOVING)
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@ValidateUser
	@Operation(summary = "Bulk removing of observation in a UserGroup", description = "Returns the success failuer result", responses = {
			@ApiResponse(responseCode = "200", description = "Bulk Removing Completed", content = @Content(schema = @Schema(implementation = String.class))),
			@ApiResponse(responseCode = "400", description = "Unable to do Bulk removing", content = @Content(schema = @Schema(implementation = String.class))) })
	public Response bulkRemovingObservation(@Context HttpServletRequest request,
			@Parameter(name = "bulkgroupUnPosting") BulkGroupUnPostingData bulkGroupUnPostingData) {
		try {
			CommonProfile profile = AuthUtil.getProfileFromRequest(request);
			Boolean result = ugServices.bulkRemoving(request, profile, bulkGroupUnPostingData);
			if (result)
				return Response.status(Status.OK).entity("Bulk Removing Completed").build();
			return Response.status(Status.NOT_ACCEPTABLE).entity("Bulking Removing Failed").build();
		} catch (Exception e) {
			return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
		}
	}

	@POST
	@Path(ApiConstants.ADD + ApiConstants.DIRECT + "/{userGroupId}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@ValidateUser
	@Operation(summary = "Adds the user directly to usergroup", description = "Add all the user", responses = {
			@ApiResponse(responseCode = "200", description = "Users added", content = @Content(schema = @Schema(implementation = String.class))),
			@ApiResponse(responseCode = "400", description = "unable to add the user", content = @Content(schema = @Schema(implementation = String.class))) })
	public Response addMembersDirectly(@Context HttpServletRequest request,
			@PathParam("userGroupId") String userGroupId,
			@Parameter(name = "memberList") UserGroupAddMemebr memberList) {
		try {

			CommonProfile profile = AuthUtil.getProfileFromRequest(request);
			JSONArray roles = (JSONArray) profile.getAttribute("roles");
			if (roles.contains("ROLE_ADMIN")) {
				Long ugId = Long.parseLong(userGroupId);
				Boolean result = ugServices.addMemberDirectly(request, ugId, memberList);
				if (result)
					return Response.status(Status.OK).entity("Added all user").build();
				return Response.status(Status.NOT_ACCEPTABLE).entity("Not Able to add the user").build();
			}
			return Response.status(Status.FORBIDDEN).entity("User not allowed to do the operation").build();

		} catch (Exception e) {
			return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
		}
	}

	@POST
	@Path(ApiConstants.CREATE)
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@ValidateUser
	@Operation(summary = "Create the userGroup", description = "Returns the userGroupIBP data", responses = {
			@ApiResponse(responseCode = "200", description = "UserGroup created", content = @Content(schema = @Schema(implementation = UserGroupIbp.class))),
			@ApiResponse(responseCode = "400", description = "unable to create the group", content = @Content(schema = @Schema(implementation = String.class))) })
	public Response createUserGroup(@Context HttpServletRequest request,
			@Parameter(description = "UserGroup create data") UserGroupCreateData ugCreateDate) {
		try {
			CommonProfile profile = AuthUtil.getProfileFromRequest(request);
			JSONArray roles = (JSONArray) profile.getAttribute("roles");
			if (roles.contains("ROLE_ADMIN")) {
				UserGroupIbp result = ugServices.createUserGroup(request, profile, ugCreateDate);
				if (result != null)
					return Response.status(Status.OK).entity(result).build();
				return Response.status(Status.NOT_ACCEPTABLE).entity("Wrong set of data").build();
			}
			return Response.status(Status.FORBIDDEN).entity("User not allowed to create User group").build();

		} catch (Exception e) {
			return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
		}
	}

	@GET
	@Path(ApiConstants.EDIT + "/{userGroupId}")
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces(MediaType.APPLICATION_JSON)
	@ValidateUser
	@Operation(summary = "find the userGroup edit data", description = "Returns the edit data of userGroup", responses = {
			@ApiResponse(responseCode = "200", description = "UserGroup edit data", content = @Content(schema = @Schema(implementation = UserGroupEditData.class))),
			@ApiResponse(responseCode = "400", description = "unable to read the data", content = @Content(schema = @Schema(implementation = String.class))) })
	public Response getEditData(@Context HttpServletRequest request, @PathParam("userGroupId") String userGroupId) {
		try {
			CommonProfile profile = AuthUtil.getProfileFromRequest(request);
			Long ugId = Long.parseLong(userGroupId);
			UserGroupEditData result = ugServices.getUGEditData(request, profile, ugId);
			if (result != null)
				return Response.status(Status.OK).entity(result).build();
			return Response.status(Status.FORBIDDEN).entity("User Not allowed to Edit the page").build();
		} catch (Exception e) {
			return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
		}
	}

	@PUT
	@Path(ApiConstants.EDIT + ApiConstants.SAVE + "/{userGroupId}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@ValidateUser
	@Operation(summary = "Save the editied data of UserGroup", description = "Saves the edit of UserGroup", responses = {
			@ApiResponse(responseCode = "200", description = "UserGroup updated", content = @Content(schema = @Schema(implementation = UserGroupIbp.class))),
			@ApiResponse(responseCode = "400", description = "Unable to edit the userGroup", content = @Content(schema = @Schema(implementation = String.class))) })
	public Response saveEdit(@Context HttpServletRequest request, @PathParam("userGroupId") String userGroupId,
			@Parameter(description = "UserGroup edit data") UserGroupEditData ugEditData) {
		try {
			CommonProfile profile = AuthUtil.getProfileFromRequest(request);
			Long ugId = Long.parseLong(userGroupId);
			UserGroupIbp result = ugServices.saveUGEdit(request, profile, ugId, ugEditData);
			if (result != null)
				return Response.status(Status.OK).entity(result).build();
			return Response.status(Status.FORBIDDEN).entity("User not allowed to edit").build();
		} catch (Exception e) {
			return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
		}
	}

	@POST
	@Path(ApiConstants.REGISTER)
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response registerUser(@Context HttpServletRequest request, AuthenticationDTO authDTO) {
		try {
			Map<String, Object> data = ugServices.registerUserProxy(request, authDTO);
			ResponseBuilder response = Response.ok().entity(data);
			if (Boolean.parseBoolean(data.get("status").toString())
					&& !Boolean.parseBoolean(data.get("verificationRequired").toString())) {
				NewCookie accessToken = new NewCookie("BAToken", data.get("access_token").toString(), "/",
						AppUtil.getDomain(request), "", 10 * 24 * 60 * 60, false);
				NewCookie refreshToken = new NewCookie("BRToken", data.get("refresh_token").toString(), "/",
						AppUtil.getDomain(request), "", 10 * 24 * 60 * 60, false);
				response.cookie(accessToken).cookie(refreshToken);
			}
			return response.build();
		} catch (Exception e) {
			return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
		}
	}

	@POST
	@Path(ApiConstants.LOGIN)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	public Response loginProxy(@Context HttpServletRequest request, @FormParam("username") String userEmail,
			@FormParam("password") String password, @FormParam("mode") String mode) {
		try {
			Map<String, Object> data = ugServices.signupProxy(request, userEmail, password, mode);
			ResponseBuilder response = Response.ok().entity(data);
			if (Boolean.parseBoolean(data.get("status").toString())
					&& !Boolean.parseBoolean(data.get("verificationRequired").toString())) {
				NewCookie accessToken = new NewCookie("BAToken", data.get("access_token").toString(), "/",
						AppUtil.getDomain(request), "", 10 * 24 * 60 * 60, false);
				NewCookie refreshToken = new NewCookie("BRToken", data.get("refresh_token").toString(), "/",
						AppUtil.getDomain(request), "", 10 * 24 * 60 * 60, false);
				response.cookie(accessToken).cookie(refreshToken);
			}
			return response.build();
		} catch (Exception e) {
			return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
		}
	}

	@POST
	@Path(ApiConstants.VERIFY_USER)
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.APPLICATION_JSON)
	public Response verifyUserOTPProxy(@Context HttpServletRequest request, @FormParam("id") Long id,
			@FormParam("otp") String otp) {
		try {
			Map<String, Object> data = ugServices.verifyOTPProxy(request, id, otp);
			ResponseBuilder response = Response.ok();
			if (Boolean.parseBoolean(data.get("status").toString())) {
				NewCookie accessToken = new NewCookie("BAToken", data.get("access_token").toString(), "/",
						AppUtil.getDomain(request), "", 10 * 24 * 60 * 60, false);
				NewCookie refreshToken = new NewCookie("BRToken", data.get("refresh_token").toString(), "/",
						AppUtil.getDomain(request), "", 10 * 24 * 60 * 60, false);
				response.cookie(accessToken).cookie(refreshToken);
			}
			return response.entity(data).build();
		} catch (Exception e) {
			return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
		}
	}

	@GET
	@Path(ApiConstants.HOMEPAGE + "/{userGroupId}" + "/{languageId}")
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces(MediaType.APPLICATION_JSON)
	@Operation(summary = "find group homepage data", description = "return group home page data", responses = {
			@ApiResponse(responseCode = "200", description = "Group home page data", content = @Content(schema = @Schema(implementation = GroupHomePageData.class))),
			@ApiResponse(responseCode = "400", description = "unable to retrieve the data", content = @Content(schema = @Schema(implementation = String.class))) })
	public Response getGroupHomePage(@PathParam("userGroupId") String ugId,
			@PathParam("languageId") String languageId) {
		try {
			Long userGroupId = Long.parseLong(ugId);
			Long langId = Long.parseLong(languageId);
			GroupHomePageData result = ugServices.getGroupHomePageData(userGroupId, langId);
			if (result != null)
				return Response.status(Status.OK).entity(result).build();
			return Response.status(Status.NOT_FOUND).build();

		} catch (Exception e) {
			return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
		}
	}

// CREATE mini gallery in group
	@POST
	@Path(ApiConstants.HOMEPAGE + ApiConstants.MINI_GALLERY + ApiConstants.CREATE + "/{groupId}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@ValidateUser
	@Operation(summary = "Creates a new mini gallery", description = "Return created mini gallery")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "Created", content = @Content(schema = @Schema(implementation = GroupGalleryConfig.class))),
			@ApiResponse(responseCode = "400", description = "Unable to create mini gallery", content = @Content(schema = @Schema(implementation = String.class))),
			@ApiResponse(responseCode = "404", description = "Not found") })
	public Response createMiniGallery(@Context HttpServletRequest request,
			@RequestBody(description = "Mini gallery payload", required = true, content = @Content(schema = @Schema(implementation = GroupGalleryConfig.class))) GroupGalleryConfig miniGalleryData,
			@Parameter(description = "Group ID") @PathParam("groupId") String groupId) {
		try {
			Long ugId = Long.parseLong(groupId);
			GroupGalleryConfig result = ugServices.createMiniGallery(request, miniGalleryData, ugId);
			if (result != null)
				return Response.status(Response.Status.OK).entity(result).build();
			return Response.status(Response.Status.NOT_FOUND).build();
		} catch (Exception e) {
			return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
		}
	}

// EDIT mini gallery in group
	@PUT
	@Path(ApiConstants.HOMEPAGE + ApiConstants.MINI_GALLERY + ApiConstants.EDIT + "/{groupId}/{galleryId}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@ValidateUser
	@Operation(summary = "Edit mini gallery data", description = "Return mini gallery data")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = GroupGalleryConfig.class))),
			@ApiResponse(responseCode = "400", description = "Unable to retrieve the data", content = @Content(schema = @Schema(implementation = String.class))),
			@ApiResponse(responseCode = "404", description = "Not found") })
	public Response editMiniGallery(@Context HttpServletRequest request,
			@Parameter(description = "Gallery ID") @PathParam("galleryId") String galleryId,
			@Parameter(description = "Group ID") @PathParam("groupId") String groupId,
			@RequestBody(description = "Edit payload", required = true, content = @Content(schema = @Schema(implementation = GroupGalleryConfig.class))) GroupGalleryConfig editData) {
		try {
			if (galleryId == null) {
				return Response.status(Response.Status.BAD_REQUEST).entity("Gallery Id cannot be null").build();
			}
			Long gId = Long.parseLong(galleryId);
			Long ugId = Long.parseLong(groupId);
			GroupGalleryConfig result = ugServices.editMiniGallery(request, ugId, gId, editData);
			if (result != null)
				return Response.status(Response.Status.OK).entity(result).build();
			return Response.status(Response.Status.NOT_FOUND).build();
		} catch (Exception e) {
			return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
		}
	}

// DELETE mini gallery in group
	@DELETE
	@Path(ApiConstants.HOMEPAGE + ApiConstants.MINI_GALLERY + ApiConstants.REMOVE + "/{groupId}/{galleryId}")
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces(MediaType.APPLICATION_JSON)
	@ValidateUser
	@Operation(summary = "Delete mini gallery data", description = "Returns no content on success")
	@ApiResponses({ @ApiResponse(responseCode = "200", description = "Deleted"),
			@ApiResponse(responseCode = "400", description = "Unable to delete the data", content = @Content(schema = @Schema(implementation = String.class))),
			@ApiResponse(responseCode = "404", description = "Not found") })
	public Response removeMiniGalleryData(@Context HttpServletRequest request,
			@Parameter(description = "Group ID") @PathParam("groupId") String groupId,
			@Parameter(description = "Gallery ID") @PathParam("galleryId") String galleryId) {
		try {
			if (galleryId == null) {
				return Response.status(Response.Status.BAD_REQUEST).entity("Gallery Id cannot be null").build();
			}
			Long gId = Long.parseLong(galleryId);
			Long ugId = Long.parseLong(groupId);
			Boolean result = ugServices.removeMiniGallery(request, ugId, gId);
			if (Boolean.TRUE.equals(result))
				return Response.status(Response.Status.OK).build();
			return Response.status(Response.Status.NOT_FOUND).build();
		} catch (Exception e) {
			return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
		}
	}

	@GET
	@Path(ApiConstants.PERMISSION + ApiConstants.OBSERVATION)
	@Produces(MediaType.APPLICATION_JSON)
	@ValidateUser
	@Operation(summary = "get usergroup observation permission", description = "returns the usergroup for each user", responses = {
			@ApiResponse(responseCode = "200", description = "UserGroup observation permissions", content = @Content(schema = @Schema(implementation = UserGroupPermissions.class))),
			@ApiResponse(responseCode = "400", description = "unable to get the usergroup", content = @Content(schema = @Schema(implementation = String.class))) })
	public Response getUserGroupObservationPermission(@Context HttpServletRequest request) {
		try {
			CommonProfile profile = AuthUtil.getProfileFromRequest(request);
			Long userId = Long.parseLong(profile.getId());
			UserGroupPermissions result = ugMemberService.getUserGroupObservationPermissions(userId);
			return Response.status(Status.OK).entity(result).build();

		} catch (Exception e) {
			return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
		}
	}

	@GET
	@Path(ApiConstants.PERMISSION + ApiConstants.OBSERVATION + "/{userGroupId}/{observationId}")
	@Produces(MediaType.APPLICATION_JSON)
	@Operation(summary = "get usergroup observation permission", description = "returns the usergroup for each user", responses = {
			@ApiResponse(responseCode = "200", description = "UserGroup observation permission", content = @Content(schema = @Schema(implementation = UserGroupObservation.class))),
			@ApiResponse(responseCode = "400", description = "unable to get the usergroup", content = @Content(schema = @Schema(implementation = String.class))) })
	public Response checkUserGroupObservationPermission(@PathParam("userGroupId") String userGroupId,
			@PathParam("observationId") String observationId) {
		try {

			Long ugId = Long.parseLong(userGroupId);
			Long obvId = Long.parseLong(observationId);
			UserGroupObservation result = ugServices.checkObservationUGMApping(ugId, obvId);
			return Response.status(Status.OK).entity(result).build();

		} catch (Exception e) {
			return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
		}
	}

	@PUT
	@Path(ApiConstants.HOMEPAGE + ApiConstants.REMOVE + "/{userGroupId}/{galleryId}")
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces(MediaType.APPLICATION_JSON)
	@ValidateUser
	@Operation(summary = "Delete group homepage gallery data", description = "return group home page data", responses = {
			@ApiResponse(responseCode = "200", description = "Gallery data removed", content = @Content(schema = @Schema(implementation = GroupHomePageData.class))),
			@ApiResponse(responseCode = "400", description = "unable to retrieve the data", content = @Content(schema = @Schema(implementation = String.class))) })
	public Response removeGalleryData(@Context HttpServletRequest request, @PathParam("userGroupId") String ugId,
			@PathParam("galleryId") String galleryId) {
		try {
			Long userGroupId = Long.parseLong(ugId);
			Long groupGalleryId = Long.parseLong(galleryId);
			GroupHomePageData result = ugServices.removeHomePage(request, userGroupId, groupGalleryId);
			if (result != null)
				return Response.status(Status.OK).entity(result).build();
			return Response.status(Status.NOT_FOUND).build();

		} catch (Exception e) {
			return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
		}
	}

// 1) Remove group homepage mini-slider gallery data (PUT)
	@PUT
	@Path(ApiConstants.HOMEPAGE + ApiConstants.MINI_SLIDER + ApiConstants.REMOVE + "/{userGroupId}/{galleryId}")
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces(MediaType.APPLICATION_JSON)
	@ValidateUser
	@Operation(summary = "Delete group homepage gallery data", description = "Return group home page data")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = GroupHomePageData.class))),
			@ApiResponse(responseCode = "400", description = "Unable to retrieve the data", content = @Content(schema = @Schema(implementation = String.class))),
			@ApiResponse(responseCode = "404", description = "Not found") })
	public Response removeMiniSliderGalleryData(@Context HttpServletRequest request,
			@Parameter(description = "User group ID") @PathParam("userGroupId") String ugId,
			@Parameter(description = "Gallery ID") @PathParam("galleryId") String galleryId) {
		try {
			Long userGroupId = Long.parseLong(ugId);
			Long groupGalleryId = Long.parseLong(galleryId);
			GroupHomePageData result = ugServices.removeMiniHomePage(request, userGroupId, groupGalleryId);
			if (result != null)
				return Response.status(Response.Status.OK).entity(result).build();
			return Response.status(Response.Status.NOT_FOUND).build();
		} catch (Exception e) {
			return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
		}
	}

// 2) Edit group homepage gallery data
	@PUT
	@Path(ApiConstants.HOMEPAGE + ApiConstants.EDIT + "/{userGroupId}/{galleryId}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@ValidateUser
	@Operation(summary = "Edit group homepage gallery data", description = "Return group home page data")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "Gallery data updated", content = @Content(schema = @Schema(implementation = GroupHomePageData.class))),
			@ApiResponse(responseCode = "400", description = "Unable to retrieve the data", content = @Content(schema = @Schema(implementation = String.class))) })
	public Response editHomePage(@Context HttpServletRequest request,
			@Parameter(description = "User group ID") @PathParam("userGroupId") String ugId,
			@Parameter(description = "Gallery ID") @PathParam("galleryId") String galleryId,
			GroupGallerySlider editData) {
		try {
			Long userGroupId = Long.parseLong(ugId);
			Long groupGalleryId = Long.parseLong(galleryId);
			GroupHomePageData result = ugServices.editHomePage(request, userGroupId, groupGalleryId, editData);
			if (result != null)
				return Response.status(Response.Status.OK).entity(result).build();
			return Response.status(Response.Status.NOT_FOUND).build();
		} catch (Exception e) {
			return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
		}
	}

// 3) Edit group mini homepage gallery data
	@PUT
	@Path(ApiConstants.HOMEPAGE + ApiConstants.MINI_SLIDER + ApiConstants.EDIT + "/{userGroupId}/{galleryId}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@ValidateUser
	@Operation(summary = "Edit group homepage gallery data", description = "Return group home page data")
	@ApiResponses({
			@ApiResponse(responseCode = "200", description = "OK", content = @Content(schema = @Schema(implementation = GroupHomePageData.class))),
			@ApiResponse(responseCode = "400", description = "Unable to retrieve the data", content = @Content(schema = @Schema(implementation = String.class))),
			@ApiResponse(responseCode = "404", description = "Not found") })
	public Response editMiniHomePage(@Context HttpServletRequest request,
			@Parameter(description = "User group ID") @PathParam("userGroupId") String ugId,
			@Parameter(description = "Gallery ID") @PathParam("galleryId") String galleryId,
			MiniGroupGallerySlider editData) {
		try {
			Long userGroupId = Long.parseLong(ugId);
			Long groupGalleryId = Long.parseLong(galleryId);
			GroupHomePageData result = ugServices.editMiniHomePage(request, userGroupId, groupGalleryId, editData);
			if (result != null)
				return Response.status(Response.Status.OK).entity(result).build();
			return Response.status(Response.Status.NOT_FOUND).build();
		} catch (Exception e) {
			return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
		}
	}

	@GET
	@Path(ApiConstants.PERMISSION)
	@Produces(MediaType.APPLICATION_JSON)
	@ValidateUser
	@Operation(summary = "get usergroup observation permission", description = "returns the usergroup for each user", responses = {
			@ApiResponse(responseCode = "200", description = "UserGroup observation permissions", content = @Content(schema = @Schema(implementation = UserGroupPermissions.class))),
			@ApiResponse(responseCode = "400", description = "unable to get the usergroup", content = @Content(schema = @Schema(implementation = String.class))) })
	public Response getUserGroupPermission(@Context HttpServletRequest request) {
		try {
			CommonProfile profile = AuthUtil.getProfileFromRequest(request);
			Long userId = Long.parseLong(profile.getId());
			UserGroupPermissions result = ugMemberService.getUserGroupObservationPermissions(userId);
			return Response.status(Status.OK).entity(result).build();

		} catch (Exception e) {
			return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
		}
	}

	@PUT
	@Path(ApiConstants.HOMEPAGE + ApiConstants.UPDATE + "/{userGroupId}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@ValidateUser
	@Operation(summary = "update group homepage gallery data", description = "return group home page data", responses = {
			@ApiResponse(responseCode = "200", description = "Gallery data updated", content = @Content(schema = @Schema(implementation = GroupHomePageData.class))),
			@ApiResponse(responseCode = "400", description = "unable to retrieve the data", content = @Content(schema = @Schema(implementation = String.class))) })
	public Response updateGalleryData(@Context HttpServletRequest request, @PathParam("userGroupId") String ugId,
			@Parameter(name = "editData") UserGroupHomePageEditData editData) {
		try {
			Long userGroupId = Long.parseLong(ugId);
			GroupHomePageData result = ugServices.updateGroupHomePage(request, userGroupId, editData);
			if (result != null)
				return Response.status(Status.OK).entity(result).build();
			return Response.status(Status.NOT_FOUND).build();

		} catch (Exception e) {
			return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
		}
	}

	@GET
	@Path(ApiConstants.MEMBER + ApiConstants.LIST)
	@Produces(MediaType.APPLICATION_JSON)
	@ValidateUser
	@Operation(summary = "list all groups by membership status", description = "returns true and false", responses = {
			@ApiResponse(responseCode = "200", description = "Group list by membership status", content = @Content(schema = @Schema(implementation = Boolean.class))),
			@ApiResponse(responseCode = "400", description = "unable to fetch the data", content = @Content(schema = @Schema(implementation = String.class))) })
	public Response userGroupMemberList(@Context HttpServletRequest request) {
		try {
			CommonProfile profile = AuthUtil.getProfileFromRequest(request);
			Long userId = Long.parseLong(profile.getId());

			return Response.status(Status.OK).entity(ugMemberService.groupListByUserId(userId)).build();
		} catch (Exception e) {

			return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
		}
	}

	@GET
	@Path(ApiConstants.MEMBER + ApiConstants.LIST + "/{userId}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Operation(summary = "list all groups by membership status", description = "returns true and false", responses = {
			@ApiResponse(responseCode = "200", description = "Group list by membership status", content = @Content(schema = @Schema(implementation = Boolean.class))),
			@ApiResponse(responseCode = "400", description = "unable to fetch the data", content = @Content(schema = @Schema(implementation = String.class))) })
	public Response userGroupMemberListByUserId(@PathParam("userId") String userId) {
		try {
			if (userId.isEmpty()) {
				throw new Exception("User Id not provided");
			}
			Long user = Long.parseLong(userId);
			return Response.status(Status.OK).entity(ugMemberService.groupListByUserId(user)).build();
		} catch (Exception e) {

			return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
		}
	}

	@GET
	@Path(ApiConstants.MEMBER + "/{userGroupId}")
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces(MediaType.APPLICATION_JSON)
	@ValidateUser
	@Operation(summary = "check user is a member of the group or not", description = "returns true and false", responses = {
			@ApiResponse(responseCode = "200", description = "Membership status", content = @Content(schema = @Schema(implementation = Boolean.class))),
			@ApiResponse(responseCode = "400", description = "unable to fetch the data", content = @Content(schema = @Schema(implementation = String.class))) })
	public Response checkUserMember(@Context HttpServletRequest request, @PathParam("userGroupId") String ugId) {
		try {

			CommonProfile profile = AuthUtil.getProfileFromRequest(request);
			Long userId = Long.parseLong(profile.getId());
			Long userGroupId = Long.parseLong(ugId);
			Boolean result = ugMemberService.checkUserGroupMember(userId, userGroupId);
			if (result != null)
				return Response.status(Status.OK).entity(result).build();
			return Response.status(Status.NOT_FOUND).build();

		} catch (Exception e) {
			return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
		}
	}

	@PUT
	@Path(ApiConstants.HOMEPAGE + ApiConstants.REORDERING + "/{userGroupId}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@ValidateUser
	@Operation(summary = "Reorder the homepage gallery slider", description = "Reorders the homepage gallery slider for a userGroup", responses = {
			@ApiResponse(responseCode = "200", description = "Homepage gallery slider reordered", content = @Content(schema = @Schema(implementation = GroupHomePageData.class))),
			@ApiResponse(responseCode = "400", description = "unable to reorder the slider", content = @Content(schema = @Schema(implementation = String.class))) })
	public Response reorderingHomePageGallerySlider(@Context HttpServletRequest request,
			@PathParam("userGroupId") String ugId,
			@Parameter(name = "reorderingHomePage") List<ReorderingHomePage> reorderingHomePage) {
		try {
			Long userGroupId = Long.parseLong(ugId);
			GroupHomePageData result = ugServices.reorderingHomePageSlider(request, userGroupId, reorderingHomePage);
			if (result != null)
				return Response.status(Status.OK).entity(result).build();
			return Response.status(Status.NOT_FOUND).build();

		} catch (Exception e) {
			return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
		}
	}

// Reorder mini homepage gallery slider for a group
	@PUT
	@Path(ApiConstants.HOMEPAGE + ApiConstants.MINI_SLIDER + ApiConstants.REORDERING + "/{userGroupId}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@ValidateUser
	@Operation(summary = "Reorder mini homepage gallery slider for a group", description = "Returns updated group home page data after reordering")
	@ApiResponse(responseCode = "200", description = "Reordered successfully", content = @Content(schema = @Schema(implementation = GroupHomePageData.class)))
	public Response reorderingMiniHomePageGallerySlider(@Context HttpServletRequest request,
			@Parameter(description = "User group ID") @PathParam("userGroupId") String ugId,
			@RequestBody(required = true, description = "Array of reordering instructions", content = @Content(array = @ArraySchema(schema = @Schema(implementation = ReorderingHomePage.class)))) List<ReorderingHomePage> reorderingHomePage) {
		try {
			Long userGroupId = Long.parseLong(ugId);
			GroupHomePageData result = ugServices.reorderMiniHomePageSlider(request, userGroupId, reorderingHomePage);
			if (result != null)
				return Response.status(Response.Status.OK).entity(result).build();
			return Response.status(Response.Status.NOT_FOUND).build();
		} catch (Exception e) {
			return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
		}
	}

	@GET
	@Path(ApiConstants.ENABLE + ApiConstants.EDIT + "/{userGroupId}")
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces(MediaType.APPLICATION_JSON)
	@ValidateUser
	@Operation(summary = "check eligiblity for edit button", description = "Returns true and false", responses = {
			@ApiResponse(responseCode = "200", description = "Edit button eligibility", content = @Content(schema = @Schema(implementation = Boolean.class))),
			@ApiResponse(responseCode = "400", description = "unable to find the data", content = @Content(schema = @Schema(implementation = String.class))) })
	public Response enableEdit(@Context HttpServletRequest request, @PathParam("userGroupId") String ugId) {
		try {
			Long userGroupId = Long.parseLong(ugId);
			Boolean result = ugServices.enableEdit(request, userGroupId);
			return Response.status(Status.OK).entity(result).build();

		} catch (Exception e) {
			return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
		}
	}

	@GET
	@Path(ApiConstants.DATATABLE + "/{dataTableId}")
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces(MediaType.APPLICATION_JSON)
	@Operation(summary = "Find UserGroup by dataTable ID", description = "Returns UserGroup Details", responses = {
			@ApiResponse(responseCode = "200", description = "UserGroup details", content = @Content(array = @ArraySchema(schema = @Schema(implementation = UserGroupIbp.class)))),
			@ApiResponse(responseCode = "404", description = "UserGroup not found", content = @Content(schema = @Schema(implementation = String.class))) })
	public Response getDataTableUserGroup(@PathParam("dataTableId") String dataTableId) {
		try {
			Long id = Long.parseLong(dataTableId);
			List<UserGroupIbp> userGroup = udDatatableService.fetchByDataTableId(id);
			return Response.status(Status.OK).entity(userGroup).build();
		} catch (Exception e) {
			return Response.status(Status.BAD_REQUEST).build();
		}
	}

	@POST
	@Path(ApiConstants.USERGROUPDATATABLE)
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Operation(summary = "Find dataTable by UserGroup ID", description = "Returns Datatable list by userGroup", responses = {
			@ApiResponse(responseCode = "200", description = "Datatable list", content = @Content(schema = @Schema(implementation = UserGroupDatatableMapping.class))),
			@ApiResponse(responseCode = "404", description = "Datatable not found", content = @Content(schema = @Schema(implementation = String.class))) })
	public Response getDataTablebyUserGroupId(
			@Parameter(name = "groupDatatableFetch") UserGroupDatatableFetch groupDatatableFetch) {
		try {
			UserGroupDatatableMapping userGroup = udDatatableService.fetchDataTableByUserGroup(groupDatatableFetch);
			return Response.status(Status.OK).entity(userGroup).build();
		} catch (Exception e) {
			return Response.status(Status.BAD_REQUEST).build();
		}
	}

	@GET
	@Path(ApiConstants.DOCUMENT + "/{documentId}")
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces(MediaType.APPLICATION_JSON)
	@Operation(summary = "finds all the usergroup for a document", description = "returns the usergroup in which the document is posted", responses = {
			@ApiResponse(responseCode = "200", description = "UserGroup list by document", content = @Content(array = @ArraySchema(schema = @Schema(implementation = UserGroupIbp.class)))),
			@ApiResponse(responseCode = "404", description = "UserGroup not found", content = @Content(schema = @Schema(implementation = String.class))) })
	public Response getUserGroupByDocId(@PathParam("documentId") String documentId) {
		try {
			Long docId = Long.parseLong(documentId);
			List<UserGroupIbp> result = ugServices.fetchByDocumentId(docId);
			if (result != null)
				return Response.status(Status.OK).entity(result).build();
			return Response.status(Status.NOT_FOUND).build();
		} catch (Exception e) {
			return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
		}
	}

	@POST
	@Path(ApiConstants.DOCUMENT)
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@ValidateUser
	@Operation(summary = "create usegroup to doc mapping", description = "returns all the group in which document is posted", responses = {
			@ApiResponse(responseCode = "200", description = "UserGroup-Document mapping created", content = @Content(array = @ArraySchema(schema = @Schema(implementation = UserGroupIbp.class)))),
			@ApiResponse(responseCode = "400", description = "unable to create the mapping", content = @Content(schema = @Schema(implementation = String.class))) })
	public Response createUGDocMapping(@Context HttpServletRequest request,
			@Parameter(name = "groupDocCreateData") UserGroupDocCreateData groupDocCreateData) {
		try {
			List<UserGroupIbp> result = ugServices.createUGDocMapping(request, groupDocCreateData);
			if (result != null)
				return Response.status(Status.OK).entity(result).build();
			return Response.status(Status.NOT_FOUND).build();
		} catch (Exception e) {
			return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
		}
	}

	@PUT
	@Path(ApiConstants.UPDATE + ApiConstants.DOCUMENT)
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@ValidateUser
	@Operation(summary = "update the usergroup document mapping", description = "returns the udpate set of usergroup", responses = {
			@ApiResponse(responseCode = "200", description = "UserGroup-Document mapping updated", content = @Content(array = @ArraySchema(schema = @Schema(implementation = UserGroupIbp.class)))),
			@ApiResponse(responseCode = "400", description = "unable to update the data", content = @Content(schema = @Schema(implementation = String.class))) })
	public Response updateUGDocMapping(@Context HttpServletRequest request,
			@Parameter(name = "ugDocMapping") UserGroupDocCreateData ugDocMapping) {
		try {

			List<UserGroupIbp> result = ugServices.updateUGDocMapping(request, ugDocMapping);
			if (result != null)
				return Response.status(Status.OK).entity(result).build();
			return Response.status(Status.NOT_ACCEPTABLE).build();
		} catch (Exception e) {
			return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
		}
	}

	@GET
	@Path(ApiConstants.SPECIES + "/{speciesId}")
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces(MediaType.APPLICATION_JSON)
	@Operation(summary = "fetch by speciesId", description = "return the usergroup associated with the species", responses = {
			@ApiResponse(responseCode = "200", description = "UserGroup list by species", content = @Content(array = @ArraySchema(schema = @Schema(implementation = UserGroupIbp.class)))),
			@ApiResponse(responseCode = "400", description = "unable to fetch the user group", content = @Content(schema = @Schema(implementation = String.class))) })
	public Response getSpeciesUserGroup(@PathParam("speciesId") String speciesId) {
		try {
			Long SpeciesId = Long.parseLong(speciesId);
			List<UserGroupIbp> result = ugServices.fetchBySpeciesId(SpeciesId);
			return Response.status(Status.OK).entity(result).build();

		} catch (Exception e) {
			return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
		}
	}

	@POST
	@Path(ApiConstants.SPECIES + ApiConstants.CREATE + "/{speciesId}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@ValidateUser
	@Operation(summary = "post species to usergroup", description = "return the usergroup associated with the species", responses = {
			@ApiResponse(responseCode = "200", description = "UserGroup-species mapping created", content = @Content(array = @ArraySchema(schema = @Schema(implementation = UserGroupIbp.class)))),
			@ApiResponse(responseCode = "400", description = "unable to fetch the user group", content = @Content(schema = @Schema(implementation = String.class))) })
	public Response createUserGroupSpeciesMapping(@Context HttpServletRequest request,
			@PathParam("speciesId") String speciesId,
			@Parameter(description = "UserGroup species create data") UserGroupSpeciesCreateData ugSpeciesCreateData) {
		try {
			Long spId = Long.parseLong(speciesId);
			List<UserGroupIbp> result = ugServices.createUGSpeciesMapping(request, spId, ugSpeciesCreateData);
			return Response.status(Status.OK).entity(result).build();
		} catch (Exception e) {
			return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
		}
	}

	@PUT
	@Path(ApiConstants.SPECIES + ApiConstants.UPDATE + "/{speciesId}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@ValidateUser
	@Operation(summary = "update userGroup in species Page", description = "return the usergroup associated with the species", responses = {
			@ApiResponse(responseCode = "200", description = "UserGroup-species mapping updated", content = @Content(array = @ArraySchema(schema = @Schema(implementation = UserGroupIbp.class)))),
			@ApiResponse(responseCode = "400", description = "unable to fetch the user group", content = @Content(schema = @Schema(implementation = String.class))) })
	public Response updateUserGroupSpeciesMapping(@Context HttpServletRequest request,
			@PathParam("speciesId") String speciesId,
			@Parameter(description = "UserGroup species create data") UserGroupSpeciesCreateData ugSpeciesCreateData) {
		try {
			Long spId = Long.parseLong(speciesId);
			List<UserGroupIbp> result = ugServices.updateUGSpeciesMapping(request, spId, ugSpeciesCreateData);
			return Response.status(Status.OK).entity(result).build();
		} catch (Exception e) {
			return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
		}
	}

	@POST
	@Path(ApiConstants.CREATE + ApiConstants.DATATABLE + "/{datatableId}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@ValidateUser
	@Operation(summary = "Create Datatable UserGroup Mapping", description = "Returns List of UserGroup", responses = {
			@ApiResponse(responseCode = "201", description = "UserGroup datatable mapping created", content = @Content(array = @ArraySchema(schema = @Schema(implementation = Long.class)))),
			@ApiResponse(responseCode = "404", description = "UserGroup Not Found ", content = @Content(schema = @Schema(implementation = String.class))),
			@ApiResponse(responseCode = "409", description = "UserGroup-Observation Mapping Cannot be Created", content = @Content(schema = @Schema(implementation = String.class))) })
	public Response createDatatableUserGroupMapping(@Context HttpServletRequest request,
			@PathParam("datatableId") String dataTableId,
			@Parameter(description = "UserGroup data") UserGroupCreateDatatable userGroupData) {
		try {
			Long datatableId = Long.parseLong(dataTableId);
			List<Long> result = udDatatableService.createUserGroupDatatableMapping(request, datatableId,
					userGroupData.getUserGroupIds());
			if (result == null)
				return Response.status(Status.CONFLICT).entity(ERROR_OCCURED_IN_TRANSACTION).build();
			return Response.status(Status.CREATED).entity(result).build();

		} catch (Exception e) {
			return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
		}
	}

	@PUT
	@Path(ApiConstants.UPDATE + ApiConstants.DATATABLE + "/{datatableId}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@ValidateUser
	@Operation(summary = "Update the UserGroup Datatable Mapping", description = "Returns the List of UserGroup Linked", responses = {
			@ApiResponse(responseCode = "200", description = "UserGroup datatable mapping updated", content = @Content(array = @ArraySchema(schema = @Schema(implementation = UserGroupIbp.class)))),
			@ApiResponse(responseCode = "400", description = "Unable to Update the UserGroup Datatable Mapping", content = @Content(schema = @Schema(implementation = String.class))) })
	public Response updateDatatableUserGroupMapping(@Context HttpServletRequest request,
			@PathParam("datatableId") String dataTableId,
			@Parameter(description = "UserGroup data") UserGroupCreateDatatable userGroupDataTableData) {
		try {
			Long datatableId = Long.parseLong(dataTableId);
			List<UserGroupIbp> result = udDatatableService.updateUserGroupDatatableMapping(request, datatableId,
					userGroupDataTableData);
			return Response.status(Status.OK).entity(result).build();
		} catch (Exception e) {
			return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
		}
	}

	@GET
	@Path(ApiConstants.GROUPLIST_ADMIN)
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@ValidateUser
	@Operation(summary = "fetch by speciesId", description = "return the usergroup associated with the species", responses = {
			@ApiResponse(responseCode = "200", description = "UserGroup admin list", content = @Content(schema = @Schema(implementation = UserGroupAdminList.class))),
			@ApiResponse(responseCode = "400", description = "unable to fetch the user group", content = @Content(schema = @Schema(implementation = String.class))) })
	public Response getUserGroupAdminListByUserId(@Context HttpServletRequest request) {
		try {
			UserGroupAdminList result = ugServices.getUserGroupAdminListByUserId(request);
			return Response.status(Status.OK).entity(result).build();

		} catch (Exception e) {
			return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
		}
	}

	@POST
	@Path(ApiConstants.CREATE + "/{obsId}/{ugId}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@ValidateUser
	@Operation(summary = "Create Observation UserGroup Mapping", description = "Returns UserGroup Observation", responses = {
			@ApiResponse(responseCode = "200", description = "UserGroup observation mapping created", content = @Content(array = @ArraySchema(schema = @Schema(implementation = UserGroupIbp.class)))),
			@ApiResponse(responseCode = "404", description = "UserGroup Not Found ", content = @Content(schema = @Schema(implementation = String.class))),
			@ApiResponse(responseCode = "409", description = "UserGroup-Observation Mapping Cannot be Created", content = @Content(schema = @Schema(implementation = String.class))) })
	public Response createObservationUserGroup(@Context HttpServletRequest request, @PathParam("obsId") String obsId,
			@PathParam("ugId") String ugId) {
		try {

			Long observationId = Long.parseLong(obsId);
			Long userGroupId = Long.parseLong(obsId);

			List<UserGroupIbp> result = ugServices.createUserGroupObervation(request, observationId, userGroupId);
			return Response.status(Status.OK).entity(result).build();

		} catch (Exception e) {
			return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
		}
	}

	@DELETE
	@Path(ApiConstants.REMOVE + "/{obsId}/{ugId}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@ValidateUser
	@Operation(summary = "Create Observation UserGroup Mapping", description = "Returns UserGroup Observation", responses = {
			@ApiResponse(responseCode = "200", description = "UserGroup observation mapping removed", content = @Content(array = @ArraySchema(schema = @Schema(implementation = UserGroupIbp.class)))),
			@ApiResponse(responseCode = "404", description = "UserGroup Not Found ", content = @Content(schema = @Schema(implementation = String.class))),
			@ApiResponse(responseCode = "409", description = "UserGroup-Observation Mapping Cannot be Created", content = @Content(schema = @Schema(implementation = String.class))) })
	public Response removeObservationUserGroup(@Context HttpServletRequest request, @PathParam("obsId") String obsId,
			@PathParam("ugId") String ugId) {
		try {

			Long observationId = Long.parseLong(obsId);
			Long userGroupId = Long.parseLong(ugId);

			List<UserGroupIbp> result = ugServices.removeUserGroupObervation(request, observationId, userGroupId);
			return Response.status(Status.OK).entity(result).build();

		} catch (Exception e) {
			return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
		}
	}

	@DELETE
	@Path(ApiConstants.REMOVE + "/datatable" + "/{obsId}/{ugId}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@ValidateUser
	@Operation(summary = "Create Observation UserGroup Mapping", description = "Returns UserGroup Observation", responses = {
			@ApiResponse(responseCode = "200", description = "UserGroup observation mapping removed", content = @Content(array = @ArraySchema(schema = @Schema(implementation = UserGroupIbp.class)))),
			@ApiResponse(responseCode = "404", description = "UserGroup Not Found ", content = @Content(schema = @Schema(implementation = String.class))),
			@ApiResponse(responseCode = "409", description = "UserGroup-Observation Mapping Cannot be Created", content = @Content(schema = @Schema(implementation = String.class))) })
	public Response removeObservationUserGroupDatatable(@Context HttpServletRequest request,
			@PathParam("obsId") String obsId, @PathParam("ugId") String ugId) {
		try {

			Long observationId = Long.parseLong(obsId);
			Long userGroupId = Long.parseLong(ugId);

			List<UserGroupIbp> result = ugServices.removeUserGroupObervationForDatatable(request, observationId,
					userGroupId);
			return Response.status(Status.OK).entity(result).build();

		} catch (Exception e) {
			return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
		}
	}

	@GET
	@Path("/userGroupSpeciesFields" + "/{userGroupId}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Operation(summary = "Get species fields By user Group id", description = "Returns the List of usergroup to species fields mappings", responses = {
			@ApiResponse(responseCode = "200", description = "UserGroup to species fields mappings", content = @Content(array = @ArraySchema(schema = @Schema(implementation = SpeciesFieldValuesDTO.class)))),
			@ApiResponse(responseCode = "400", description = "Unable to Update the UserGroup Datatable Mapping", content = @Content(schema = @Schema(implementation = String.class))) })
	public Response getSpeciesFieldsByUserGroupId(@PathParam("userGroupId") String userGroupId) {
		try {
			Long ugId = Long.parseLong(userGroupId);
			List<SpeciesFieldValuesDTO> result = ugServices.fetchSpeciesFieldsWithValuesByUgId(ugId);
			return Response.status(Status.OK).entity(result).build();
		} catch (Exception e) {
			return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
		}
	}

	@POST
	@Path(ApiConstants.UPDATE + "/speciesFieldsMapping/{ugId}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	// @ValidateUser
	@io.swagger.v3.oas.annotations.Operation(summary = "Create UserGroup species fields Mapping", description = "Returns List of UserGroup species fields mappings", requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(required = true, description = "List of species fields to map", content = @io.swagger.v3.oas.annotations.media.Content(array = @io.swagger.v3.oas.annotations.media.ArraySchema(schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = SField.class)))), responses = {
			@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "UserGroup species fields mapping created/updated", content = @io.swagger.v3.oas.annotations.media.Content(array = @io.swagger.v3.oas.annotations.media.ArraySchema(schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = UsergroupSpeciesFieldMapping.class)))),
			@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "UserGroup not found", content = @io.swagger.v3.oas.annotations.media.Content(schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = String.class))),
			@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "UserGroup-speciesFields Mapping cannot be created", content = @io.swagger.v3.oas.annotations.media.Content(schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = String.class))),
			@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid request", content = @io.swagger.v3.oas.annotations.media.Content(schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = String.class))) })
	public Response updateUserGroupSpeciesFieldsMapping(@Context HttpServletRequest request,
			@io.swagger.v3.oas.annotations.Parameter(description = "UserGroup ID", required = true) @PathParam("ugId") String ugId,
			List<SField> speciesFields) {
		try {
			List<UsergroupSpeciesFieldMapping> result = ugServices
					.updateSpeciesFieldsMappingByUgId(Long.parseLong(ugId), speciesFields);
			if (result == null)
				return Response.status(Response.Status.CONFLICT).entity(ERROR_OCCURED_IN_TRANSACTION).build();
			return Response.status(Response.Status.CREATED).entity(result).build();
		} catch (Exception e) {
			return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
		}
	}

	@PUT
	@Path("/speciesField/metadata/{userGroupId}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	// @ValidateUser
	@io.swagger.v3.oas.annotations.Operation(summary = "Update Species Field Metadata for User Group", description = "Returns list of updated metadata", requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "List of species field metadata objects", required = true, content = @io.swagger.v3.oas.annotations.media.Content(array = @io.swagger.v3.oas.annotations.media.ArraySchema(schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = SpeciesFieldMetadata.class)))), responses = {
			@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "List of updated species field metadata", content = @io.swagger.v3.oas.annotations.media.Content(array = @io.swagger.v3.oas.annotations.media.ArraySchema(schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = UserGroupSpeciesFieldMeta.class)))),
			@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Could not update species field metadata", content = @io.swagger.v3.oas.annotations.media.Content(schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = String.class))),
			@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid request", content = @io.swagger.v3.oas.annotations.media.Content(schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = String.class))) })
	public Response updateSpeciesFieldMetadata(@Context HttpServletRequest request,
			@io.swagger.v3.oas.annotations.Parameter(description = "User group ID", required = true) @PathParam("userGroupId") Long userGroupId,
			List<SpeciesFieldMetadata> metadata) {
		try {
			List<UserGroupSpeciesFieldMeta> result = ugServices.updateSpeciesFieldMetadata(userGroupId, metadata);
			if (result != null) {
				return Response.ok().entity(result).build();
			} else {
				return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
						.entity("Could not update species field metadata").build();
			}
		} catch (Exception e) {
			return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
		}
	}

	@GET
	@Path("/speciesField/metadata/{userGroupId}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@io.swagger.v3.oas.annotations.Operation(summary = "Get Species Field Metadata for User Group", description = "Returns list of species fields metadata mapped for the usergroup", responses = {
			@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "List of species fields metadata", content = @io.swagger.v3.oas.annotations.media.Content(array = @io.swagger.v3.oas.annotations.media.ArraySchema(schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = UserGroupSpeciesFieldMeta.class)))),
			@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "Could not get species field metadata", content = @io.swagger.v3.oas.annotations.media.Content(schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = String.class))),
			@io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Invalid request", content = @io.swagger.v3.oas.annotations.media.Content(schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = String.class))) })
	public Response getSpeciesFieldMetadata(
			@io.swagger.v3.oas.annotations.Parameter(description = "User group ID", required = true) @PathParam("userGroupId") Long userGroupId) {
		try {
			List<UserGroupSpeciesFieldMeta> result = ugServices.getSpeciesFieldMetaData(userGroupId);
			if (result != null) {
				return Response.ok().entity(result).build();
			} else {
				return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
						.entity("Could not get species field metadata").build();
			}
		} catch (Exception e) {
			return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
		}
	}
}
