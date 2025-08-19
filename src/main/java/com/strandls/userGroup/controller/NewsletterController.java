package com.strandls.userGroup.controller;

import java.util.List;

import com.strandls.user.ApiException;
import com.strandls.userGroup.ApiConstants;
import com.strandls.userGroup.pojo.Newsletter;
import com.strandls.userGroup.pojo.NewsletterWithParentChildRelationship;
import com.strandls.userGroup.service.NewsletterSerivce;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.inject.Inject;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

@Tag(name = "Newsletter Service", description = "APIs for newsletter operations (userGroup module)")
@Path(ApiConstants.V1 + ApiConstants.NEWSLETTER)
@Produces(MediaType.APPLICATION_JSON)
public class NewsletterController {

	@Inject
	private NewsletterSerivce newsletterSerivce;

	@GET
	@Path("ping")
	@Produces(MediaType.TEXT_PLAIN)
	@Operation(summary = "Ping endpoint for the newsletter service", description = "Returns 'pong' if the service is running", responses = @ApiResponse(responseCode = "200", description = "pong", content = @Content(schema = @Schema(implementation = String.class))))
	public String ping() {
		return "pong";
	}

	@GET
	@Path("{objectId}")
	@Consumes(MediaType.TEXT_PLAIN)
	@Operation(summary = "Find Newsletter by ID", responses = {
			@ApiResponse(responseCode = "200", description = "Newsletter details", content = @Content(schema = @Schema(implementation = Newsletter.class))),
			@ApiResponse(responseCode = "404", description = "Newsletter not found", content = @Content(schema = @Schema(implementation = String.class))) })
	public Response getNewsletter(
			@Parameter(description = "Newsletter object ID", required = true) @PathParam("objectId") String objectId) {
		try {
			Long id = Long.parseLong(objectId);
			Newsletter newsletter = newsletterSerivce.findById(id);
			return Response.status(Status.OK).entity(newsletter).build();
		} catch (Exception e) {
			return Response.status(Status.BAD_REQUEST).build();
		}
	}

	@GET
	@Path("group")
	@Consumes(MediaType.TEXT_PLAIN)
	@Operation(summary = "Find Newsletters by UserGroup and Language", responses = {
			@ApiResponse(responseCode = "200", description = "Newsletter list for a user group and language", content = @Content(array = @ArraySchema(schema = @Schema(implementation = NewsletterWithParentChildRelationship.class)))),
			@ApiResponse(responseCode = "404", description = "Newsletter not found", content = @Content(schema = @Schema(implementation = String.class))) })
	public Response getNewslettersByGroup(@Context HttpServletRequest request,
			@Parameter(description = "UserGroup ID") @QueryParam("userGroupId") Long userGroupId,
			@Parameter(description = "Language ID") @QueryParam("languageId") Long languageId) throws ApiException {
		try {
			List<NewsletterWithParentChildRelationship> newsletter = newsletterSerivce
					.getByUserGroupAndLanguage(userGroupId, languageId);
			return Response.status(Status.OK).entity(newsletter).build();
		} catch (Exception e) {
			return Response.status(Status.BAD_REQUEST).build();
		}
	}
}
