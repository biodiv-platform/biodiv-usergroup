/**
 *
 */
package com.strandls.userGroup.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;

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

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import net.minidev.json.JSONArray;

/**
 * @author Abhishek Rudra
 *
 */

@Api("UserGroup Serivce")
@Path(ApiConstants.V1 + ApiConstants.GROUP)
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

	public Response pong() {
		return Response.status(Status.OK).entity("PONG").build();
	}

	@GET
	@Path("/{objectId}/{languageId}")
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Find UserGroup by ID", notes = "Returns UserGroup details", response = UserGroup.class)
	@ApiResponses(value = { @ApiResponse(code = 404, message = "UserGroup not found", response = String.class) })
	public Response getUserGroup(@PathParam("objectId") String objectId, @PathParam("languageId") String languageId) {
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
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Find media toggle value of a UserGroup by ID", notes = "Returns all observations customisation for ug as response", response = ObservationCustomisations.class)
	@ApiResponses(value = { @ApiResponse(code = 404, message = "UserGroup not found", response = String.class) })
	public Response getUserGroupMediaToggle(@PathParam("ugId") String ugId) {
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
	@Produces(MediaType.APPLICATION_JSON)

	@ValidateUser
	@ApiOperation(value = "Update media toggle value of a UserGroup", response = UserGroup.class)
	@ApiResponses(value = { @ApiResponse(code = 404, message = "UserGroup not found", response = String.class) })

	public Response updateGroupObservationCustomisations(@Context HttpServletRequest request,
			@ApiParam(name = "observationCustomisations") ObservationCustomisations observationCustomisations) {
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
	@ApiOperation(value = "Find UserGroup by ID", notes = "Returns UserGroup details for IBP", response = UserGroupIbp.class)
	@ApiResponses(value = { @ApiResponse(code = 404, message = "UserGroup not found", response = String.class) })
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

	@ApiOperation(value = "Find UserGroup by observation ID", notes = "Returns UserGroup Details", response = UserGroupIbp.class, responseContainer = "List")
	@ApiResponses(value = { @ApiResponse(code = 404, message = "UserGroup not found", response = String.class) })

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

	@ApiOperation(value = "Find all observation related to a userGroup", notes = "Return list of observation associated with a userGroup", response = Long.class, responseContainer = "List")
	@ApiResponses(value = { @ApiResponse(code = 400, message = "Observation list not found", response = String.class) })
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

	@ApiOperation(value = "Find list of UserGroup based on List of UserGroupId", notes = "Return UserGroup Details", response = UserGroupIbp.class, responseContainer = "List")
	@ApiResponses(value = { @ApiResponse(code = 404, message = "UserGroup Not Found ", response = String.class) })

	public Response getUserGroupList(
			@ApiParam(name = "userGroupMember") @QueryParam("userGroupMember") String userGroupMember) {
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
	@ApiOperation(value = "Create Observation UserGroup Mapping", notes = "Returns List of UserGroup", response = Long.class, responseContainer = "List")
	@ApiResponses(value = { @ApiResponse(code = 404, message = "UserGroup Not Found ", response = String.class),
			@ApiResponse(code = 409, message = "UserGroup-Observation Mapping Cannot be Created", response = String.class) })

	public Response createObservationUserGroupMapping(@Context HttpServletRequest request,
			@PathParam("obsId") String obsId,
			@ApiParam(name = "userGroupData") UserGroupMappingCreateData userGroupData) {
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
	@ApiOperation(value = "Create Observation UserGroup Mapping", notes = "Returns List of UserGroup", response = Long.class, responseContainer = "List")
	@ApiResponses(value = { @ApiResponse(code = 404, message = "UserGroup Not Found ", response = String.class),
			@ApiResponse(code = 409, message = "UserGroup-Observation Mapping Cannot be Created", response = String.class) })

	public Response createObservationUserGroupMappingDatatable(@Context HttpServletRequest request,
			@PathParam("obsId") String obsId,
			@ApiParam(name = "userGroupData") UserGroupMappingCreateData userGroupData) {
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
	@ApiOperation(value = "Update the UserGroup Observation Mapping", notes = "Returns the List of UserGroup Linked", response = UserGroupIbp.class, responseContainer = "List")
	@ApiResponses(value = {
			@ApiResponse(code = 400, message = "Unable to Update the UserGroup Observation Mapping", response = String.class) })

	public Response updateUserGroupMapping(@Context HttpServletRequest request,
			@PathParam("observationId") String observationId,
			@ApiParam(name = "userGroups") UserGroupMappingCreateData userGroup) {
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

	@ApiOperation(value = "Find all the UserGroups", notes = "Returns all the UserGroups", response = UserGroupIbp.class, responseContainer = "List")
	@ApiResponses(value = {
			@ApiResponse(code = 404, message = "Unable to fetch the UserGroups", response = String.class) })

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

	@ApiOperation(value = "Find all the UserGroups for list page", notes = "Returns all the UserGroups for list page", response = UserGroupExpanded.class, responseContainer = "List")
	@ApiResponses(value = {
			@ApiResponse(code = 404, message = "Unable to fetch the UserGroups list", response = String.class) })

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

	@ApiOperation(value = "Find Featured", notes = "Return list Featured", response = Featured.class, responseContainer = "List")
	@ApiResponses(value = { @ApiResponse(code = 400, message = "Featured not Found", response = String.class) })

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

	@ApiOperation(value = "Posting of Featured to a Group", notes = "Returns the Details of Featured", response = Featured.class, responseContainer = "List")
	@ApiResponses(value = {
			@ApiResponse(code = 404, message = "Unable to Feature in a Group", response = String.class) })
	public Response createFeatured(@Context HttpServletRequest request,
			@ApiParam(name = "featuredCreate") FeaturedCreateData featuredCreate) {

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
	@ApiOperation(value = "UnFeatures a Object from a UserGroup", notes = "Returns the Current Featured", response = Featured.class, responseContainer = "List")
	@ApiResponses(value = { @ApiResponse(code = 404, message = "Unable to Unfeature", response = String.class) })
	public Response unFeatured(@Context HttpServletRequest request, @PathParam("objectType") String objectType,
			@PathParam("objectId") String objectId,
			@ApiParam("userGroupList") UserGroupMappingCreateData userGroupList) {
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

	@ApiOperation(value = "Get the species Group for a userGroup", notes = "Returns the species Group for a userGroup", response = UserGroupSpeciesGroup.class, responseContainer = "List")
	@ApiResponses(value = {
			@ApiResponse(code = 400, message = "Unable to retireve the data", response = String.class) })

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

	@ApiOperation(value = "Sends out invitaions for founder and moedrators", notes = "Returns the success and failur", response = String.class)
	@ApiResponses(value = {

			@ApiResponse(code = 400, message = "Unable to send the invitaions", response = String.class) })

	public Response addUserGroupMember(@Context HttpServletRequest request,
			@ApiParam(name = "userGroupInvitations") UserGroupInvitationData userGroupInvitations) {
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

	@ApiOperation(value = "find the founder and moderator list", notes = "Return the founder and moderator list", response = AdministrationList.class)
	@ApiResponses(value = { @ApiResponse(code = 400, message = "unable to find the data", response = String.class) })

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

	@ApiOperation(value = "validate the join request for closed groups", notes = "In success returns the usergroup data", response = UserGroupIbp.class)
	@ApiResponses(value = {
			@ApiResponse(code = 400, message = "unable to validate the request", response = String.class) })

	public Response validateJoinRequest(@Context HttpServletRequest request,
			@ApiParam(name = "encryptionKey") EncryptionKey encryptionKey) {
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

	@ApiOperation(value = "remove a existing user from the group", notes = "remove existing user", response = String.class)
	@ApiResponses(value = { @ApiResponse(code = 400, message = "unable to remove the user", response = String.class) })

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

	@ApiOperation(value = "remove list of existing users from the group", notes = "remove existing users", response = String.class)
	@ApiResponses(value = { @ApiResponse(code = 400, message = "unable to remove the users", response = String.class) })

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

	@ApiOperation(value = "endpoint to leave a group", notes = "leave group", response = String.class)
	@ApiResponses(value = { @ApiResponse(code = 400, message = "unable to leave the group", response = String.class) })

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

	@ApiOperation(value = "endpoint to join open group", notes = "User can join open group without invitation", response = String.class)
	@ApiResponses(value = {
			@ApiResponse(code = 400, message = "unable to join the userGroup", response = String.class) })

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

	@ApiOperation(value = "Send invites for Role Member in UserGroup", notes = "Sends Invitation mails for joining group as Member role", response = String.class)
	@ApiResponses(value = { @ApiResponse(code = 400, message = "unable to send invites", response = String.class) })

	public Response sendInvitesForMemberRole(@Context HttpServletRequest request,
			@PathParam("userGroupId") String userGroupId, @ApiParam(name = "userList") List<Long> userList) {
		try {
			CommonProfile profile = AuthUtil.getProfileFromRequest(request);
			Long ugId = Long.parseLong(userGroupId);
			Boolean result = ugServices.sendInvitesForMemberRole(request, profile, ugId, userList);
			if (result != null)
				return Response.status(Status.OK).entity("Invitaion Sent out").build();
			return Response.status(Status.NOT_ACCEPTABLE).entity("Invitation Sending caused Problem").build();

		} catch (Exception e) {
			return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
		}

	}

	@POST
	@Path(ApiConstants.BULK + ApiConstants.POSTING)
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)

	@ValidateUser

	@ApiOperation(value = "Bulk Posting of observation in a UserGroup", notes = "Returns the success failuer result", response = String.class)
	@ApiResponses(value = { @ApiResponse(code = 400, message = "Unable to do Bulk Posting", response = String.class) })

	public Response bulkPostingObservationUG(@Context HttpServletRequest request,
			@ApiParam(name = "bulkGroupPosting") BulkGroupPostingData bulkGroupPostingData) {

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

	@ApiOperation(value = "Bulk removing of observation in a UserGroup", notes = "Returns the success failuer result", response = String.class)
	@ApiResponses(value = { @ApiResponse(code = 400, message = "Unable to do Bulk removing", response = String.class) })

	public Response bulkRemovingObservation(@Context HttpServletRequest request,
			@ApiParam(name = "bulkgroupUnPosting") BulkGroupUnPostingData bulkGroupUnPostingData) {
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

	@ApiOperation(value = "Adds the user directly to usergroup", notes = "Add all the user", response = String.class)
	@ApiResponses(value = { @ApiResponse(code = 400, message = "unable to add the user", response = String.class) })

	public Response addMembersDirectly(@Context HttpServletRequest request,
			@PathParam("userGroupId") String userGroupId,
			@ApiParam(name = "memberList") UserGroupAddMemebr memberList) {
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

	@ApiOperation(value = "Create the userGroup", notes = "Returns the userGroupIBP data", response = UserGroupIbp.class)
	@ApiResponses(value = { @ApiResponse(code = 400, message = "unable to create the group", response = String.class) })

	public Response createUserGroup(@Context HttpServletRequest request,
			@ApiParam(name = "userGroupCreateData") UserGroupCreateData ugCreateDate) {
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

	@ApiOperation(value = "find the userGroup edit data", notes = "Returns the edit data of userGroup", response = UserGroupEditData.class)
	@ApiResponses(value = { @ApiResponse(code = 400, message = "unable to read the data", response = String.class) })

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

	@ApiOperation(value = "Save the editied data of UserGroup", notes = "Saves the edit of UserGroup", response = UserGroupIbp.class)
	@ApiResponses(value = {
			@ApiResponse(code = 400, message = "Unable to edit the userGroup", response = String.class) })

	public Response saveEdit(@Context HttpServletRequest request, @PathParam("userGroupId") String userGroupId,
			@ApiParam("ugEditData") UserGroupEditData ugEditData) {
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

	@ApiOperation(value = "find group homepage data", notes = "return group home page data", response = GroupHomePageData.class)
	@ApiResponses(value = {
			@ApiResponse(code = 400, message = "unable to retrieve the data", response = String.class) })

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

	@POST
	@Path(ApiConstants.HOMEPAGE + ApiConstants.MINI_GALLERY + ApiConstants.CREATE + "/{groupId}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@ValidateUser

	@ApiOperation(value = "Creates a new mini gallery", notes = "Return created mini gallery", response = Map.class)
	@ApiResponses(value = {
			@ApiResponse(code = 400, message = "Unable to create mini gallery", response = String.class) })

	public Response createMiniGallery(@Context HttpServletRequest request,
			@ApiParam(name = "miniGalleryData") GroupGalleryConfig miniGalleryData,
			@PathParam("groupId") String groupId) {
		try {
			Long ugId = Long.parseLong(groupId);
			GroupGalleryConfig result = ugServices.createMiniGallery(request, miniGalleryData, ugId);
			if (result != null)
				return Response.status(Status.OK).entity(result).build();
			return Response.status(Status.NOT_FOUND).build();

		} catch (Exception e) {
			return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
		}
	}

	@PUT
	@Path(ApiConstants.HOMEPAGE + ApiConstants.MINI_GALLERY + ApiConstants.EDIT + "/{groupId}" + "/{galleryId}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)

	@ValidateUser

	@ApiOperation(value = "Edit mini gallery data", notes = "return mini gallery data", response = Map.class)
	@ApiResponses(value = {
			@ApiResponse(code = 400, message = "unable to retrieve the data", response = String.class) })

	public Response editMiniGallery(@Context HttpServletRequest request, @PathParam("galleryId") String galleryId,
			@PathParam("groupId") String groupId, @ApiParam(name = "editData") GroupGalleryConfig editData) {
		try {
			if (galleryId == null) {
				return Response.status(Status.BAD_REQUEST).entity("Gallery Id cannot be null").build();
			}
			Long gId = Long.parseLong(galleryId);
			Long ugId = Long.parseLong(groupId);
			GroupGalleryConfig result = ugServices.editMiniGallery(request, ugId, gId, editData);
			if (result != null)
				return Response.status(Status.OK).entity(result).build();
			return Response.status(Status.NOT_FOUND).build();

		} catch (Exception e) {
			return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
		}
	}

	@DELETE
	@Path(ApiConstants.HOMEPAGE + ApiConstants.MINI_GALLERY + ApiConstants.REMOVE + "/{groupId}" + "/{galleryId}")
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces(MediaType.APPLICATION_JSON)

	@ValidateUser

	@ApiOperation(value = "Delete mini gallery data", notes = "returns null", response = Void.class)
	@ApiResponses(value = { @ApiResponse(code = 400, message = "unable to delete the data", response = String.class) })

	public Response removeMiniGalleryData(@Context HttpServletRequest request, @PathParam("groupId") String groupId,
			@PathParam("galleryId") String galleryId) {
		try {
			if (galleryId == null) {
				return Response.status(Status.BAD_REQUEST).entity("Gallery Id cannot be null").build();
			}
			Long gId = Long.parseLong(galleryId);
			Long ugId = Long.parseLong(groupId);
			Boolean result = ugServices.removeMiniGallery(request, ugId, gId);
			if (Boolean.TRUE.equals(result))
				return Response.status(Status.OK).build();
			return Response.status(Status.NOT_FOUND).build();

		} catch (Exception e) {
			return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
		}
	}

	@GET
	@Path(ApiConstants.PERMISSION + ApiConstants.OBSERVATION)
	@Produces(MediaType.APPLICATION_JSON)

	@ValidateUser
	@ApiOperation(value = "get usergroup observation permission", notes = "returns the usergroup for each user", response = UserGroupPermissions.class)
	@ApiResponses(value = {
			@ApiResponse(code = 400, message = "unable to get the usergroup", response = String.class) })

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

	@ApiOperation(value = "get usergroup observation permission", notes = "returns the usergroup for each user", response = UserGroupObservation.class)
	@ApiResponses(value = {
			@ApiResponse(code = 400, message = "unable to get the usergroup", response = String.class) })

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

	@ApiOperation(value = "Delete group homepage gallery data", notes = "return group home page data", response = GroupHomePageData.class)
	@ApiResponses(value = {
			@ApiResponse(code = 400, message = "unable to retrieve the data", response = String.class) })

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

	@PUT
	@Path(ApiConstants.HOMEPAGE + ApiConstants.MINI_SLIDER + ApiConstants.REMOVE + "/{userGroupId}/{galleryId}")
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces(MediaType.APPLICATION_JSON)

	@ValidateUser

	@ApiOperation(value = "Delete group homepage gallery data", notes = "return group home page data", response = GroupHomePageData.class)
	@ApiResponses(value = {
			@ApiResponse(code = 400, message = "unable to retrieve the data", response = String.class) })

	public Response removeMiniSliderGalleryData(@Context HttpServletRequest request,
			@PathParam("userGroupId") String ugId, @PathParam("galleryId") String galleryId) {
		try {
			Long userGroupId = Long.parseLong(ugId);
			Long groupGalleryId = Long.parseLong(galleryId);
			GroupHomePageData result = ugServices.removeMiniHomePage(request, userGroupId, groupGalleryId);
			if (result != null)
				return Response.status(Status.OK).entity(result).build();
			return Response.status(Status.NOT_FOUND).build();

		} catch (Exception e) {
			return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
		}
	}

	@PUT
	@Path(ApiConstants.HOMEPAGE + ApiConstants.EDIT + "/{userGroupId}/{galleryId}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)

	@ValidateUser

	@ApiOperation(value = "Edit group homepage gallery data", notes = "return group home page data", response = GroupHomePageData.class)
	@ApiResponses(value = {
			@ApiResponse(code = 400, message = "unable to retrieve the data", response = String.class) })

	public Response editHomePage(@Context HttpServletRequest request, @PathParam("userGroupId") String ugId,
			@PathParam("galleryId") String galleryId, @ApiParam(name = "editData") GroupGallerySlider editData) {
		try {
			Long userGroupId = Long.parseLong(ugId);
			Long groupGalleryId = Long.parseLong(galleryId);
			GroupHomePageData result = ugServices.editHomePage(request, userGroupId, groupGalleryId, editData);
			if (result != null)
				return Response.status(Status.OK).entity(result).build();
			return Response.status(Status.NOT_FOUND).build();

		} catch (Exception e) {
			return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
		}
	}

	@PUT
	@Path(ApiConstants.HOMEPAGE + ApiConstants.INSERT + "/{userGroupId}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)

	@ValidateUser

	@ApiOperation(value = "Create group homepage gallery", notes = "return group home page data", response = GroupHomePageData.class)
	@ApiResponses(value = {
			@ApiResponse(code = 400, message = "unable to retrieve the data", response = String.class) })

	public Response insertHomePage(@Context HttpServletRequest request, @PathParam("userGroupId") String ugId,
			@ApiParam(name = "editData") GroupGallerySlider editData) {
		try {
			Long userGroupId = Long.parseLong(ugId);
			GroupHomePageData result = ugServices.insertHomePage(request, userGroupId, editData);
			if (result != null)
				return Response.status(Status.OK).entity(result).build();
			return Response.status(Status.NOT_FOUND).build();

		} catch (Exception e) {
			return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
		}
	}

	@PUT
	@Path(ApiConstants.HOMEPAGE + ApiConstants.MINI_SLIDER + ApiConstants.EDIT + "/{userGroupId}/{galleryId}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)

	@ValidateUser

	@ApiOperation(value = "Edit group homepage gallery data", notes = "return group home page data", response = GroupHomePageData.class)
	@ApiResponses(value = {
			@ApiResponse(code = 400, message = "unable to retrieve the data", response = String.class) })

	public Response editMiniHomePage(@Context HttpServletRequest request, @PathParam("userGroupId") String ugId,
			@PathParam("galleryId") String galleryId, @ApiParam(name = "editData") MiniGroupGallerySlider editData) {
		try {
			Long userGroupId = Long.parseLong(ugId);
			Long groupGalleryId = Long.parseLong(galleryId);
			GroupHomePageData result = ugServices.editMiniHomePage(request, userGroupId, groupGalleryId, editData);
			if (result != null)
				return Response.status(Status.OK).entity(result).build();
			return Response.status(Status.NOT_FOUND).build();

		} catch (Exception e) {
			return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
		}
	}

	@PUT
	@Path(ApiConstants.HOMEPAGE + ApiConstants.MINI_SLIDER + ApiConstants.INSERT + "/{userGroupId}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)

	@ValidateUser

	@ApiOperation(value = "Create group homepage mini gallery slider data", notes = "return group home page data after creating slider", response = GroupHomePageData.class)
	@ApiResponses(value = {
			@ApiResponse(code = 400, message = "unable to retrieve the data", response = String.class) })

	public Response insertMiniHomePage(@Context HttpServletRequest request, @PathParam("userGroupId") String ugId,
			@ApiParam(name = "editData") MiniGroupGallerySlider editData) {
		try {
			Long userGroupId = Long.parseLong(ugId);
			GroupHomePageData result = ugServices.insertMiniHomePage(request, userGroupId, editData);
			if (result != null)
				return Response.status(Status.OK).entity(result).build();
			return Response.status(Status.NOT_FOUND).build();

		} catch (Exception e) {
			return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
		}
	}

	@GET
	@Path(ApiConstants.PERMISSION)
	@Produces(MediaType.APPLICATION_JSON)

	@ValidateUser
	@ApiOperation(value = "get usergroup observation permission", notes = "returns the usergroup for each user", response = UserGroupPermissions.class)
	@ApiResponses(value = {
			@ApiResponse(code = 400, message = "unable to get the usergroup", response = String.class) })

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

	@ApiOperation(value = "update group homepage gallery data", notes = "return group home page data", response = GroupHomePageData.class)
	@ApiResponses(value = {

			@ApiResponse(code = 400, message = "unable to retrieve the data", response = String.class) })
	public Response updateGalleryData(@Context HttpServletRequest request, @PathParam("userGroupId") String ugId,
			@ApiParam(name = "editData") UserGroupHomePageEditData editData) {
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
	@ApiOperation(value = "list all groups by membership status", notes = "returns true and false", response = Boolean.class)
	@ApiResponses(value = { @ApiResponse(code = 400, message = "unable to fetch the data", response = String.class) })

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

	@ApiOperation(value = "list all groups by membership status", notes = "returns true and false", response = Boolean.class)
	@ApiResponses(value = { @ApiResponse(code = 400, message = "unable to fetch the data", response = String.class) })

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
	@ApiOperation(value = "check user is a member of the group or not", notes = "returns true and false", response = Boolean.class)
	@ApiResponses(value = { @ApiResponse(code = 400, message = "unable to fetch the data", response = String.class) })

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

	public Response reorderingHomePageGallerySlider(@Context HttpServletRequest request,
			@PathParam("userGroupId") String ugId,
			@ApiParam(name = "reorderingHomePage") List<ReorderingHomePage> reorderingHomePage) {
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

	@PUT
	@Path(ApiConstants.HOMEPAGE + ApiConstants.MINI_SLIDER + ApiConstants.REORDERING + "/{userGroupId}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)

	@ValidateUser

	public Response reorderingMiniHomePageGallerySlider(@Context HttpServletRequest request,
			@PathParam("userGroupId") String ugId,
			@ApiParam(name = "reorderingHomePage") List<ReorderingHomePage> reorderingHomePage) {
		try {
			Long userGroupId = Long.parseLong(ugId);
			GroupHomePageData result = ugServices.reorderMiniHomePageSlider(request, userGroupId, reorderingHomePage);
			if (result != null)
				return Response.status(Status.OK).entity(result).build();
			return Response.status(Status.NOT_FOUND).build();

		} catch (Exception e) {
			return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
		}
	}

	@GET
	@Path(ApiConstants.ENABLE + ApiConstants.EDIT + "/{userGroupId}")
	@Consumes(MediaType.TEXT_PLAIN)
	@Produces(MediaType.APPLICATION_JSON)

	@ValidateUser
	@ApiOperation(value = "check eligiblity for edit button", notes = "Returns true and false", response = Boolean.class)
	@ApiResponses(value = { @ApiResponse(code = 400, message = "unable to find the data", response = String.class) })

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

	@ApiOperation(value = "Find UserGroup by dataTable ID", notes = "Returns UserGroup Details", response = UserGroupIbp.class, responseContainer = "List")
	@ApiResponses(value = { @ApiResponse(code = 404, message = "UserGroup not found", response = String.class) })

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

	@ApiOperation(value = "Find dataTable by UserGroup ID", notes = "Returns Datatable list by userGroup", response = UserGroupDatatableMapping.class)
	@ApiResponses(value = { @ApiResponse(code = 404, message = "Datatable not found", response = String.class) })

	public Response getDataTablebyUserGroupId(
			@ApiParam(name = "groupDatatableFetch") UserGroupDatatableFetch groupDatatableFetch) {
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

	@ApiOperation(value = "finds all the usergroup for a document", notes = "returns the usergroup in which the document is posted", response = UserGroupIbp.class, responseContainer = "List")
	@ApiResponses(value = { @ApiResponse(code = 404, message = "UserGroup not found", response = String.class) })

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

	@ApiOperation(value = "create usegroup to doc mapping", notes = "returns all the group in which document is posted", response = UserGroupIbp.class, responseContainer = "List")
	@ApiResponses(value = {
			@ApiResponse(code = 400, message = "unable to create the mapping", response = String.class) })

	public Response createUGDocMapping(@Context HttpServletRequest request,
			@ApiParam(name = "groupDocCreateData") UserGroupDocCreateData groupDocCreateData) {
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

	@ApiOperation(value = "update the usergroup document mapping", notes = "returns the udpate set of usergroup", response = UserGroupIbp.class, responseContainer = "List")
	@ApiResponses(value = { @ApiResponse(code = 400, message = "unable to update the data", response = String.class) })

	public Response updateUGDocMapping(@Context HttpServletRequest request,
			@ApiParam(name = "ugDocMapping") UserGroupDocCreateData ugDocMapping) {
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

	@ApiOperation(value = "fetch by speciesId", notes = "return the usergroup associated with the species", response = UserGroupIbp.class, responseContainer = "List")
	@ApiResponses(value = {
			@ApiResponse(code = 400, message = "unable to fetch the user group", response = String.class) })

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

	@ApiOperation(value = "post species to usergroup", notes = "return the usergroup associated with the species", response = UserGroupIbp.class, responseContainer = "List")
	@ApiResponses(value = {
			@ApiResponse(code = 400, message = "unable to fetch the user group", response = String.class) })

	public Response createUserGroupSpeciesMapping(@Context HttpServletRequest request,
			@PathParam("speciesId") String speciesId,
			@ApiParam(name = "ugSpeciesCreateData") UserGroupSpeciesCreateData ugSpeciesCreateData) {
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

	@ApiOperation(value = "update userGroup in species Page", notes = "return the usergroup associated with the species", response = UserGroupIbp.class, responseContainer = "List")
	@ApiResponses(value = {
			@ApiResponse(code = 400, message = "unable to fetch the user group", response = String.class) })

	public Response updateUserGroupSpeciesMapping(@Context HttpServletRequest request,
			@PathParam("speciesId") String speciesId,
			@ApiParam(name = "ugSpeciesCreateData") UserGroupSpeciesCreateData ugSpeciesCreateData) {
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
	@ApiOperation(value = "Create Datatable UserGroup Mapping", notes = "Returns List of UserGroup", response = Long.class, responseContainer = "List")
	@ApiResponses(value = { @ApiResponse(code = 404, message = "UserGroup Not Found ", response = String.class),
			@ApiResponse(code = 409, message = "UserGroup-Observation Mapping Cannot be Created", response = String.class) })

	public Response createDatatableUserGroupMapping(@Context HttpServletRequest request,
			@PathParam("datatableId") String dataTableId,
			@ApiParam(name = "userGroupData") UserGroupCreateDatatable userGroupData) {
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
	@ApiOperation(value = "Update the UserGroup Datatable Mapping", notes = "Returns the List of UserGroup Linked", response = UserGroupIbp.class, responseContainer = "List")
	@ApiResponses(value = {
			@ApiResponse(code = 400, message = "Unable to Update the UserGroup Datatable Mapping", response = String.class) })

	public Response updateDatatableUserGroupMapping(@Context HttpServletRequest request,
			@PathParam("datatableId") String dataTableId,
			@ApiParam(name = "userGroupData") UserGroupCreateDatatable userGroupDataTableData) {
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
	@ApiOperation(value = "fetch by speciesId", notes = "return the usergroup associated with the species", response = UserGroupAdminList.class)
	@ApiResponses(value = {
			@ApiResponse(code = 400, message = "unable to fetch the user group", response = String.class) })

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
	@ApiOperation(value = "Create Observation UserGroup Mapping", notes = "Returns UserGroup Observation", response = UserGroupIbp.class, responseContainer = "List")
	@ApiResponses(value = { @ApiResponse(code = 404, message = "UserGroup Not Found ", response = String.class),
			@ApiResponse(code = 409, message = "UserGroup-Observation Mapping Cannot be Created", response = String.class) })

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
	@ApiOperation(value = "Create Observation UserGroup Mapping", notes = "Returns UserGroup Observation", response = UserGroupIbp.class, responseContainer = "List")
	@ApiResponses(value = { @ApiResponse(code = 404, message = "UserGroup Not Found ", response = String.class),
			@ApiResponse(code = 409, message = "UserGroup-Observation Mapping Cannot be Created", response = String.class) })

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
	@ApiOperation(value = "Create Observation UserGroup Mapping", notes = "Returns UserGroup Observation", response = UserGroupIbp.class, responseContainer = "List")
	@ApiResponses(value = { @ApiResponse(code = 404, message = "UserGroup Not Found ", response = String.class),
			@ApiResponse(code = 409, message = "UserGroup-Observation Mapping Cannot be Created", response = String.class) })

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

	@ApiOperation(value = "Get species fields By user Group id", notes = "Returns the List of usergroup to species fields mappings", response = SpeciesFieldValuesDTO.class, responseContainer = "List")
	@ApiResponses(value = {
			@ApiResponse(code = 400, message = "Unable to Update the UserGroup Datatable Mapping", response = String.class) })

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
	@Path(ApiConstants.UPDATE + "/speciesFieldsMapping" + "/{ugId}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)

	// @ValidateUser
	@ApiOperation(value = "Create UserGroup species fields Mapping", notes = "Returns List of UserGroup specied fields mappings", response = UsergroupSpeciesFieldMapping.class, responseContainer = "List")
	@ApiResponses(value = { @ApiResponse(code = 404, message = "UserGroup Not Found ", response = String.class),
			@ApiResponse(code = 409, message = "UserGroup-speciesFields Mapping Cannot be Created", response = String.class) })

	public Response updateUserGroupSpeciesFieldsMapping(@Context HttpServletRequest request,
			@PathParam("ugId") String ugId, @ApiParam(name = "speciesFields") List<SField> speciesFields) {
		try {

			List<UsergroupSpeciesFieldMapping> result = ugServices
					.updateSpeciesFieldsMappingByUgId(Long.parseLong(ugId), speciesFields);
			if (result == null)
				return Response.status(Status.CONFLICT).entity(ERROR_OCCURED_IN_TRANSACTION).build();
			return Response.status(Status.CREATED).entity(result).build();

		} catch (Exception e) {
			return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
		}
	}

	@PUT
	@Path("/speciesField/metadata/{userGroupId}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "Update Species Field Metadata for User Group", notes = "Returns list of updated metadata", response = UserGroupSpeciesFieldMeta.class, responseContainer = "List")
//	@ValidateUser
	public Response updateSpeciesFieldMetadata(@Context HttpServletRequest request,
			@PathParam("userGroupId") Long userGroupId,
			@ApiParam(name = "speciesFieldMetadata") List<SpeciesFieldMetadata> metadata) {
		try {

			List<UserGroupSpeciesFieldMeta> result = ugServices.updateSpeciesFieldMetadata(userGroupId, metadata);

			if (result != null) {
				return Response.ok().entity(result).build();
			} else {
				return Response.status(Status.INTERNAL_SERVER_ERROR).entity("Could not update species field metadata")
						.build();
			}
		} catch (Exception e) {
			return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
		}
	}

	@GET
	@Path("/speciesField/metadata/{userGroupId}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@ApiOperation(value = "get Species Field Metadata for User Group", notes = "Returns list of species fields metadata mapped for the usergroup", response = UserGroupSpeciesFieldMeta.class, responseContainer = "List")
	public Response getSpeciesFieldMetadata(@PathParam("userGroupId") Long userGroupId) {
		try {
			List<UserGroupSpeciesFieldMeta> result = ugServices.getSpeciesFieldMetaData(userGroupId);
			if (result != null) {
				return Response.ok().entity(result).build();
			} else {
				return Response.status(Status.INTERNAL_SERVER_ERROR).entity("Could not get species field metadata")
						.build();
			}
		} catch (Exception e) {
			return Response.status(Status.BAD_REQUEST).entity(e.getMessage()).build();
		}
	}

}
