/**
 * 
 */
package com.strandls.userGroup.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.HttpHeaders;

import org.pac4j.core.profile.CommonProfile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.strandls.activity.pojo.MailData;
import com.strandls.activity.pojo.UserGroupActivity;
import com.strandls.activity.pojo.UserGroupMailData;
import com.strandls.authentication_utility.util.AuthUtil;
import com.strandls.user.controller.AuthenticationServiceApi;
import com.strandls.user.controller.UserServiceApi;
import com.strandls.user.pojo.User;
import com.strandls.user.pojo.UserIbp;
import com.strandls.userGroup.dao.FeaturedDao;
import com.strandls.userGroup.dao.GroupGalleryConfigDao;
import com.strandls.userGroup.dao.GroupGallerySliderDao;
import com.strandls.userGroup.dao.MiniGroupGallerySliderDao;
import com.strandls.userGroup.dao.StatsDao;
import com.strandls.userGroup.dao.UserGroupDao;
import com.strandls.userGroup.dao.UserGroupDocumentDao;
import com.strandls.userGroup.dao.UserGroupHabitatDao;
import com.strandls.userGroup.dao.UserGroupInvitaionDao;
import com.strandls.userGroup.dao.UserGroupJoinRequestDao;
import com.strandls.userGroup.dao.UserGroupMemberRoleDao;
import com.strandls.userGroup.dao.UserGroupObservationDao;
import com.strandls.userGroup.dao.UserGroupSpeciesDao;
import com.strandls.userGroup.dao.UserGroupSpeciesFieldMappingDao;
import com.strandls.userGroup.dao.UserGroupSpeciesFieldMetaDao;
import com.strandls.userGroup.dao.UserGroupSpeciesGroupDao;
import com.strandls.userGroup.dao.UserGroupUserRequestDAO;
import com.strandls.userGroup.dto.AuthenticationDTO;
import com.strandls.userGroup.filter.MutableHttpServletRequest;
import com.strandls.userGroup.pojo.AdministrationList;
import com.strandls.userGroup.pojo.BulkGroupPostingData;
import com.strandls.userGroup.pojo.BulkGroupUnPostingData;
import com.strandls.userGroup.pojo.Featured;
import com.strandls.userGroup.pojo.FeaturedCreate;
import com.strandls.userGroup.pojo.FeaturedCreateData;
import com.strandls.userGroup.pojo.GroupAddMember;
import com.strandls.userGroup.pojo.GroupGalleryConfig;
import com.strandls.userGroup.pojo.GroupGallerySlider;
import com.strandls.userGroup.pojo.GroupHomePageData;
import com.strandls.userGroup.pojo.InvitaionMailData;
import com.strandls.userGroup.pojo.MiniGroupGallerySlider;
import com.strandls.userGroup.pojo.ObservationCustomisations;
import com.strandls.userGroup.pojo.ReorderingHomePage;
import com.strandls.userGroup.pojo.SField;
import com.strandls.userGroup.pojo.SpeciesFieldMetadata;
import com.strandls.userGroup.pojo.SpeciesFieldValuesDTO;
import com.strandls.userGroup.pojo.Stats;
import com.strandls.userGroup.pojo.UserGroup;
import com.strandls.userGroup.pojo.UserGroupAddMemebr;
import com.strandls.userGroup.pojo.UserGroupAdminList;
import com.strandls.userGroup.pojo.UserGroupCreateData;
import com.strandls.userGroup.pojo.UserGroupCreateDatatable;
import com.strandls.userGroup.pojo.UserGroupDocCreateData;
import com.strandls.userGroup.pojo.UserGroupDocument;
import com.strandls.userGroup.pojo.UserGroupEditData;
import com.strandls.userGroup.pojo.UserGroupExpanded;
import com.strandls.userGroup.pojo.UserGroupHabitat;
import com.strandls.userGroup.pojo.UserGroupHomePageEditData;
import com.strandls.userGroup.pojo.UserGroupIbp;
import com.strandls.userGroup.pojo.UserGroupInvitation;
import com.strandls.userGroup.pojo.UserGroupInvitationData;
import com.strandls.userGroup.pojo.UserGroupJoinRequest;
import com.strandls.userGroup.pojo.UserGroupMappingCreateData;
import com.strandls.userGroup.pojo.UserGroupMemberRole;
import com.strandls.userGroup.pojo.UserGroupMembersCount;
import com.strandls.userGroup.pojo.UserGroupObservation;
import com.strandls.userGroup.pojo.UserGroupObvFilterData;
import com.strandls.userGroup.pojo.UserGroupSpecies;
import com.strandls.userGroup.pojo.UserGroupSpeciesCreateData;
import com.strandls.userGroup.pojo.UserGroupSpeciesFieldMeta;
import com.strandls.userGroup.pojo.UserGroupSpeciesGroup;
import com.strandls.userGroup.pojo.UserGroupUserJoinRequest;
import com.strandls.userGroup.pojo.UsergroupSpeciesFieldMapping;
import com.strandls.userGroup.service.UserGroupDatatableService;
import com.strandls.userGroup.service.UserGroupMemberService;
import com.strandls.userGroup.service.UserGroupSerivce;
import com.strandls.userGroup.util.PropertyFileUtil;
import com.strandls.userGroup.util.RecordType;

import net.minidev.json.JSONArray;

/**
 * @author Abhishek Rudra
 *
 */
public class UserGroupServiceImpl implements UserGroupSerivce {

	private final Logger logger = LoggerFactory.getLogger(UserGroupServiceImpl.class);

	@Inject
	private ObjectMapper objectMapper;

	@Inject
	private LogActivities logActivity;

	@Inject
	private UserGroupDao userGroupDao;

	@Inject
	private UserGroupObservationDao userGroupObvDao;

	@Inject
	private FeaturedDao featuredDao;

	@Inject
	private RabbitMQProducer produce;

	@Inject
	private UserGroupSpeciesGroupDao ugSGroupDao;

	@Inject
	private UserServiceApi userService;

	@Inject
	private UserGroupInvitaionDao ugInvitationDao;

	@Inject
	private EncryptionUtils encryptionUtils;

	@Inject
	private StatsDao statsDao;

	@Inject
	private UserGroupHabitatDao ugHabitatDao;

	@Inject
	private MailUtils mailUtils;

	@Inject
	private UserGroupJoinRequestDao ugJoinRequestDao;

	@Inject
	private UserGroupDocumentDao ugDocumentDao;

	@Inject
	private UserGroupUserRequestDAO userGroupUserRequestDao;

	@Inject
	private AuthenticationServiceApi authenticationApi;

	@Inject
	private GroupGallerySliderDao groupGallerySliderDao;

	@Inject
	private MiniGroupGallerySliderDao miniGroupGallerySliderDao;

	@Inject
	private GroupGalleryConfigDao groupGalleryConfigDao;

	@Inject
	private UserGroupMemberService ugMemberService;

	@Inject
	private UserGroupSpeciesDao ugSpeciesDao;

	@Inject
	private UserGroupDatatableService ugDatatableService;

	@Inject
	private UserGroupMemberRoleDao ugMemberDao;

	@Inject
	UserGroupObservationDao ugObvDao;

	@Inject
	UserGroupSpeciesFieldMappingDao ugSfMappingDao;

	@Inject
	private UserGroupSpeciesFieldMetaDao ugSpeciesFieldMetaDao;

	private Long defaultLanguageId = Long
			.parseLong(PropertyFileUtil.fetchProperty("config.properties", "defaultLanguageId"));

	private final String messageType = "User Groups";
	private static final String roleAdmin = "ROLE_ADMIN";
	private static final String species = "species";
	private static final String ROLE_PAGE_EDITOR = "ROLE_PAGE_EDITOR";

	@Override
	public UserGroup fetchByGroupId(Long id, Long langId) {
		UserGroup userGroup = userGroupDao.findByGroupIdByLanguageId(id, langId);
		if (userGroup == null) {
			userGroup = userGroupDao.findByGroupIdByLanguageId(id, defaultLanguageId);
		}
		List<UserGroupSpeciesGroup> ugSpeciesGroups = ugSGroupDao.findByUserGroupId(id);
		List<UserGroupHabitat> ugHabitats = ugHabitatDao.findByUserGroupId(id);
		List<Long> speciesGroupId = new ArrayList<Long>();
		List<Long> habitatId = new ArrayList<Long>();
		for (UserGroupSpeciesGroup ugSpeciesGroup : ugSpeciesGroups) {
			speciesGroupId.add(ugSpeciesGroup.getSpeciesGroupId());
		}
		for (UserGroupHabitat ugHabitat : ugHabitats) {
			habitatId.add(ugHabitat.getHabitatId());
		}
		userGroup.setHabitatIds(habitatId);
		userGroup.setSpeciesGroupIds(speciesGroupId);
		return userGroup;
	}

	@Override
	public ObservationCustomisations fetchMediaToggle(Long ugId) {
		try {
			ObservationCustomisations customisations = new ObservationCustomisations();
			String mediaToggle = userGroupDao.findMediaToggleByUgId(ugId);
			customisations.setMediaToggle(mediaToggle);
			customisations.setUserGroupId(ugId);
			return customisations;
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return null;
	}

	@Override
	public UserGroupIbp fetchByGroupIdIbp(Long id) {
		try {

			UserGroup ug = userGroupDao.findById(id);
			return getUserGroupIbp(ug);

		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return null;
	}

	private UserGroupIbp getUserGroupIbp(UserGroup ug) {
		UserGroupIbp ibp;
		if (ug != null) {
			if (ug.getDomianName() != null)
				ibp = new UserGroupIbp(ug.getId(), ug.getName(), ug.getIcon(), ug.getDomianName(),
						ug.getAllowUserToJoin(), ug.getGroupId());
			else {
				String webAddress = "/group/" + ug.getWebAddress();
				ibp = new UserGroupIbp(ug.getId(), ug.getName(), ug.getIcon(), webAddress, ug.getAllowUserToJoin(),
						ug.getGroupId());
			}
			return ibp;
		}

		return null;

	}

	@Override
	public List<UserGroupIbp> fetchByObservationId(Long id) {
		try {
			List<UserGroupObservation> userGroupObv = userGroupObvDao.findByObservationId(id);
			List<UserGroupIbp> userGroup = new ArrayList<UserGroupIbp>();
			if (userGroupObv != null && !userGroupObv.isEmpty()) {
				for (UserGroupObservation ugObv : userGroupObv) {
					userGroup.add(fetchByGroupIdIbp(ugObv.getUserGroupId()));
				}
			}
			if (!userGroup.isEmpty())
				return userGroup;

		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return null;

	}

	@Override
	public List<UserGroupIbp> fetchByUserGroupDetails(List<Long> userGroupMember) {
		List<UserGroupIbp> userGroupList = new ArrayList<UserGroupIbp>();
		try {
			for (Long userGroupId : userGroupMember) {
				userGroupList.add(fetchByGroupIdIbp(userGroupId));
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}

		return userGroupList;
	}

	@Override
	public List<Long> createUserGroupObservationMapping(HttpServletRequest request, Long observationId,
			UserGroupMappingCreateData userGroups, Boolean canEsUpdate, Boolean setActivity) {

		CommonProfile profile = AuthUtil.getProfileFromRequest(request);
		Long userId = Long.parseLong(profile.getId());
		List<Long> resultList = new ArrayList<Long>();
		JSONArray roles = (JSONArray) profile.getAttribute("roles");

		for (Long userGroup : userGroups.getUserGroups()) {

			UserGroupObservation userGroupObs = new UserGroupObservation(userGroup, observationId);
			UserGroupObservation result = userGroupObvDao.save(userGroupObs);
			if (result != null) {
				resultList.add(result.getUserGroupId());
				UserGroupIbp ugIbp = fetchByGroupIdIbp(userGroup);
				String description = createUgDescription(ugIbp);
				MailData mailData = null;
				if (userGroups.getMailData() != null) {
					mailData = updateMailData(observationId, userGroups.getMailData());
				}

				if (setActivity) {
					logActivity.LogActivity(request.getHeader(HttpHeaders.AUTHORIZATION), description, observationId,
							observationId, "observation", result.getUserGroupId(), "Posted resource", mailData);

				}
			}

		}

		if (Boolean.TRUE.equals(canEsUpdate)) {
			try {
				produce.setMessage("observation", observationId.toString(), messageType);
			} catch (Exception e) {
				logger.error(e.getMessage());
			}
		}

		return resultList;
	}

	public List<UserGroupIbp> removeUserGroupObservationMapping(HttpServletRequest request, Long observationId,
			UserGroupMappingCreateData userGorups, Boolean canEsUpdate) {
		CommonProfile profile = AuthUtil.getProfileFromRequest(request);
		Long userId = Long.parseLong(profile.getId());
		JSONArray roles = (JSONArray) profile.getAttribute("roles");

		List<Long> previousUserGroup = new ArrayList<Long>();
		List<UserGroupObservation> previousMapping = userGroupObvDao.findByObservationId(observationId);
		for (UserGroupObservation ug : previousMapping) {
			if ((userGorups.getUserGroups().contains(ug.getUserGroupId()))) {
				Boolean eligible = ugMemberService.checkUserGroupMember(userId, ug.getUserGroupId());

				if (roles.contains(roleAdmin) || Boolean.TRUE.equals(eligible)) {
					userGroupObvDao.delete(ug);

					UserGroupIbp ugIbp = fetchByGroupIdIbp(ug.getUserGroupId());

					String description = createUgDescription(ugIbp);

					MailData mailData = userGorups.getMailData() != null
							? updateMailData(observationId, userGorups.getMailData())
							: null;
					logActivity.LogActivity(request.getHeader(HttpHeaders.AUTHORIZATION), description, observationId,
							observationId, "observation", ug.getUserGroupId(), "Removed resoruce", mailData);
				}

			} else {
				previousUserGroup.add(ug.getUserGroupId());
			}

		}
		if (Boolean.TRUE.equals(canEsUpdate)) {
			try {
				produce.setMessage("observation", observationId.toString(), messageType);
			} catch (Exception e) {
				logger.error(e.getMessage());
			}
		}

		return fetchByObservationId(observationId);

	}

	public void removeUGSpeciesMapping(HttpServletRequest request, Long speciesId,
			UserGroupSpeciesCreateData ugSpeciesCreateData) {
		CommonProfile profile = AuthUtil.getProfileFromRequest(request);
		Long userId = Long.parseLong(profile.getId());

		List<UserGroupSpecies> ugSpeciesList = ugSpeciesDao.findBySpeciesId(speciesId);
		List<Long> userGroupIds = ugSpeciesCreateData.getUserGroupIds();
		JSONArray roles = (JSONArray) profile.getAttribute("roles");

//		remove the existing groups
		for (UserGroupSpecies ugSpecies : ugSpeciesList) {
			if (userGroupIds.contains(ugSpecies.getUserGroupId())) {
				Boolean eligible = ugMemberService.checkUserGroupMember(userId, ugSpecies.getUserGroupId());
				if (roles.contains(roleAdmin) || Boolean.TRUE.equals(eligible)) {
					ugSpeciesDao.delete(ugSpecies);
					UserGroupActivity ugActivity = new UserGroupActivity();
					UserGroupIbp ugIbp = fetchByGroupIdIbp(ugSpecies.getUserGroupId());
					String description = null;
					ugActivity.setFeatured(null);
					ugActivity.setUserGroupId(ugIbp.getId());
					ugActivity.setUserGroupName(ugIbp.getName());
					ugActivity.setWebAddress(ugIbp.getWebAddress());
					try {
						description = objectMapper.writeValueAsString(ugActivity);
					} catch (Exception e) {
						logger.error(e.getMessage());
					}

				}

			}

		}
	}

	@Override
	public List<UserGroupIbp> updateUserGroupObservationMapping(HttpServletRequest request, Long observationId,
			UserGroupMappingCreateData userGorups) {

		CommonProfile profile = AuthUtil.getProfileFromRequest(request);
		Long userId = Long.parseLong(profile.getId());
		JSONArray roles = (JSONArray) profile.getAttribute("roles");

		List<Long> previousUserGroup = new ArrayList<Long>();
		List<UserGroupObservation> previousMapping = userGroupObvDao.findByObservationId(observationId);
		for (UserGroupObservation ug : previousMapping) {
			if (!(userGorups.getUserGroups().contains(ug.getUserGroupId()))) {
				Boolean eligible = ugMemberService.checkUserGroupMember(userId, ug.getUserGroupId());

				if (roles.contains(roleAdmin) || eligible) {
					userGroupObvDao.delete(ug);

					UserGroupIbp ugIbp = fetchByGroupIdIbp(ug.getUserGroupId());

					String description = createUgDescription(ugIbp);

					MailData mailData = updateMailData(observationId, userGorups.getMailData());
					logActivity.LogActivity(request.getHeader(HttpHeaders.AUTHORIZATION), description, observationId,
							observationId, "observation", ug.getUserGroupId(), "Removed resoruce", mailData);
				}

			}
			previousUserGroup.add(ug.getUserGroupId());
		}

		for (Long userGroupId : userGorups.getUserGroups()) {
			if (!(previousUserGroup.contains(userGroupId))) {

				UserGroupObservation userGroupMapping = new UserGroupObservation(userGroupId, observationId);
				userGroupObvDao.save(userGroupMapping);

				UserGroupIbp ugIbp = fetchByGroupIdIbp(userGroupId);
				String description = createUgDescription(ugIbp);

				MailData mailData = updateMailData(observationId, userGorups.getMailData());
				logActivity.LogActivity(request.getHeader(HttpHeaders.AUTHORIZATION), description, observationId,
						observationId, "observation", userGroupId, "Posted resource", mailData);

			}
		}
		try {
			produce.setMessage("observation", observationId.toString(), messageType);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		List<UserGroupIbp> result = fetchByObservationId(observationId);

		return result;
	}

	@Override
	public List<UserGroupIbp> fetchAllUserGroup(Long langId) {
		List<UserGroupIbp> result = new ArrayList<UserGroupIbp>();
		try {
			List<UserGroup> userGroupList = userGroupDao.findAll();
			List<UserGroupMembersCount> count = ugMemberService.getUserGroupMemberCount();
			Map<Long, UserGroupIbp> ugMap = new HashMap<Long, UserGroupIbp>();
			UserGroupIbp ibp = null;
			List<Long> groupIds = new ArrayList<>();

			for (UserGroup userGroup : userGroupList) {
				Long groupId = userGroup.getGroupId();
				boolean alreadyAdded = groupIds.contains(groupId);
				boolean isPreferredLang = userGroup.getLanguageId().equals(langId);

				if (!alreadyAdded || isPreferredLang) {
					if (!alreadyAdded) {
						groupIds.add(groupId);
					}

					String webAddress = userGroup.getDomianName() != null ? userGroup.getDomianName()
							: "/group/" + userGroup.getWebAddress();

					ibp = new UserGroupIbp(userGroup.getId(), userGroup.getName(), userGroup.getIcon(), webAddress,
							userGroup.getAllowUserToJoin(), groupId);

					ugMap.put(groupId, ibp);
				}
			}
			for (UserGroupMembersCount ugm : count) {
				if (ugMap.get(ugm.getUserGroupId()) != null) {
					result.add(ugMap.get(ugm.getUserGroupId()));
					ugMap.remove(ugm.getUserGroupId());
				}
			}
			for (Entry<Long, UserGroupIbp> entry : ugMap.entrySet()) {
				result.add(entry.getValue());
			}

		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return result;
	}

	@Override
	public List<UserGroupExpanded> fetchAllUserGroupExpanded() {
		List<UserGroupExpanded> result = new ArrayList<UserGroupExpanded>();

		try {
			List<UserGroup> userGroupList = userGroupDao.findAll();
			List<UserGroupMembersCount> count = ugMemberService.getUserGroupMemberCount();
			Map<Long, UserGroupExpanded> ugMap = new HashMap<Long, UserGroupExpanded>();
			UserGroupExpanded ibp = null;
			for (UserGroup userGroup : userGroupList) {

				List<UserGroupSpeciesGroup> ugSpeciesGroups = ugSGroupDao.findByUserGroupId(userGroup.getId());
				List<UserGroupHabitat> ugHabitats = ugHabitatDao.findByUserGroupId(userGroup.getId());
				List<Long> speciesGroupIds = new ArrayList<Long>();
				List<Long> habitatIds = new ArrayList<Long>();
				for (UserGroupSpeciesGroup ugSpeciesGroup : ugSpeciesGroups) {
					speciesGroupIds.add(ugSpeciesGroup.getSpeciesGroupId());
				}
				for (UserGroupHabitat ugHabitat : ugHabitats) {
					habitatIds.add(ugHabitat.getHabitatId());
				}

				String webAddress = userGroup.getDomianName();

				if (webAddress == null) {
					webAddress = "/group/" + userGroup.getWebAddress();
				}

				ibp = new UserGroupExpanded(userGroup.getId(), userGroup.getName(), userGroup.getIcon(), webAddress,
						userGroup.getAllowUserToJoin(), 0L, userGroup.getFoundedOn(), userGroup.getStartDate(),
						speciesGroupIds, habitatIds);

				ugMap.put(userGroup.getId(), ibp);
			}

			// Iterate through member count and assign it to expanded modal
			for (UserGroupMembersCount ugm : count) {
				UserGroupExpanded ugx = ugMap.get(ugm.getUserGroupId());
				if (ugx != null) {
					ugx.setMemberCount(ugm.getCount());
					result.add(ugx);
					ugMap.remove(ugm.getUserGroupId());
				}

			}

			for (Entry<Long, UserGroupExpanded> entry : ugMap.entrySet()) {
				result.add(entry.getValue());
			}

		} catch (Exception e) {
			logger.error(e.getMessage());
		}

		return result;
	}

	@Override
	public List<Featured> fetchFeatured(String objectType, Long id) {
		List<Featured> featuredList = featuredDao.fetchAllFeatured(objectType, id);
		return featuredList;
	}

	@Override
	public List<Featured> createFeatured(HttpServletRequest request, Long userId,
			FeaturedCreateData featuredCreateData) {

		List<Featured> result = new ArrayList<Featured>();
		try {

			FeaturedCreate featuredCreate = featuredCreateData.getFeaturedCreate();
			Featured featured;
			if (featuredCreate.getObjectType().equalsIgnoreCase("observation"))
				featuredCreate.setObjectType("species.participation.Observation");

			else if (featuredCreate.getObjectType().equalsIgnoreCase("document"))
				featuredCreate.setObjectType("content.eml.Document");

			else if (featuredCreate.getObjectType().equalsIgnoreCase(species))
				featuredCreate.setObjectType("species.Species");

			List<Featured> featuredList = featuredDao.fetchAllFeatured(featuredCreate.getObjectType(),
					featuredCreate.getObjectId());

			for (Long userGroupId : featuredCreate.getUserGroup()) {

				int flag = 0;
				for (Featured alreadyFeatured : featuredList) {
					if (alreadyFeatured.getUserGroup().equals(userGroupId)) {
						alreadyFeatured.setCreatedOn(new Date());
						alreadyFeatured.setNotes(featuredCreate.getNotes());
						alreadyFeatured.setAuthorId(userId);
						featuredDao.update(alreadyFeatured);
						flag = 1;
					}
				}

				if (flag == 0) {
					featured = new Featured(null, userId, new Date(), featuredCreate.getNotes(),
							featuredCreate.getObjectId(), featuredCreate.getObjectType(), userGroupId,
							featuredCreate.getLanguageId() != null ? featuredCreate.getLanguageId()
									: defaultLanguageId);
					featured = featuredDao.save(featured);

				}

				Long activityId = userGroupId;
				if (userGroupId == null)
					activityId = featuredCreate.getObjectId();

				UserGroupActivity ugActivity = new UserGroupActivity();
				if (userGroupId != null) {
					UserGroupIbp ugIbp = fetchByGroupIdIbp(userGroupId);

					ugActivity.setUserGroupId(ugIbp.getId());
					ugActivity.setUserGroupName(ugIbp.getName());
					ugActivity.setWebAddress(ugIbp.getWebAddress());
				} else {
					InputStream in = Thread.currentThread().getContextClassLoader()
							.getResourceAsStream("config.properties");

					Properties properties = new Properties();
					try {
						properties.load(in);
					} catch (IOException e) {
						logger.error(e.getMessage());
					}
					String portalName = properties.getProperty("siteName");
					String portalWebAddress = properties.getProperty("serverUrl");
					in.close();
					ugActivity.setUserGroupId(null);
					ugActivity.setUserGroupName(portalName);
					ugActivity.setWebAddress(portalWebAddress);

				}
				ugActivity.setFeatured(featuredCreate.getNotes());

				String description = null;

				try {
					description = objectMapper.writeValueAsString(ugActivity);
				} catch (Exception e) {
					logger.error(e.getMessage());
				}

				if (featuredCreate.getObjectType().equalsIgnoreCase("species.participation.Observation")) {

					MailData mailData = updateMailData(featuredCreate.getObjectId(), featuredCreateData.getMailData());
					logActivity.LogActivity(request.getHeader(HttpHeaders.AUTHORIZATION), description,
							featuredCreate.getObjectId(), featuredCreate.getObjectId(), "observation", activityId,
							"Featured", mailData);

				} else if (featuredCreate.getObjectType().equalsIgnoreCase("content.eml.Document")) {
					MailData mailData = updateDocumentMailData(featuredCreate.getObjectId(),
							featuredCreateData.getMailData());
					logActivity.LogDocumentActivities(request.getHeader(HttpHeaders.AUTHORIZATION), description,
							featuredCreate.getObjectId(), featuredCreate.getObjectId(), "document", activityId,
							"Featured", mailData);

				} else if (featuredCreate.getObjectType().equals("species.Species")) {
					MailData mailData = null;
//					TODO mailData
					logActivity.logSpeciesActivities(request.getHeader(HttpHeaders.AUTHORIZATION), description,
							featuredCreate.getObjectId(), featuredCreate.getObjectId(), species, activityId, "Featured",
							mailData);
				}

			}

			result = featuredDao.fetchAllFeatured(featuredCreate.getObjectType(), featuredCreate.getObjectId());
		} catch (

		Exception e) {
			logger.error(e.getMessage());
		}
		return result;
	}

	@Override
	public List<Featured> removeFeatured(HttpServletRequest request, Long userId, String objectType, Long objectId,
			UserGroupMappingCreateData userGroupList) {

		List<Featured> resultList = null;
		try {
			if (objectType.equalsIgnoreCase("observation"))
				objectType = "species.participation.Observation";

			else if (objectType.equalsIgnoreCase("document"))
				objectType = "content.eml.Document";

			else if (objectType.equalsIgnoreCase(species))
				objectType = "species.Species";

			List<Featured> featuredList = featuredDao.fetchAllFeatured(objectType, objectId);

			for (Long userGroupId : userGroupList.getUserGroups()) {

				for (Featured featured : featuredList) {
					if (featured.getUserGroup().equals(userGroupId)) {
						featuredDao.delete(featured);
						Long activityId = userGroupId;
						if (userGroupId == null)
							activityId = objectId;

						UserGroupActivity ugActivity = new UserGroupActivity();
						if (userGroupId != null) {
							UserGroupIbp ugIbp = fetchByGroupIdIbp(userGroupId);

							ugActivity.setUserGroupId(ugIbp.getId());
							ugActivity.setUserGroupName(ugIbp.getName());
							ugActivity.setWebAddress(ugIbp.getWebAddress());
						} else {
							InputStream in = Thread.currentThread().getContextClassLoader()
									.getResourceAsStream("config.properties");

							Properties properties = new Properties();
							try {
								properties.load(in);
							} catch (IOException e) {
								logger.error(e.getMessage());
							}
							String portalName = properties.getProperty("siteName");
							String portalWebAddress = properties.getProperty("serverUrl");
							in.close();
							ugActivity.setUserGroupId(null);
							ugActivity.setUserGroupName(portalName);
							ugActivity.setWebAddress(portalWebAddress);

						}
						ugActivity.setFeatured(featured.getNotes());

						String description = null;
						try {
							description = objectMapper.writeValueAsString(ugActivity);
						} catch (Exception e) {
							logger.error(e.getMessage());
						}

						if (objectType.equalsIgnoreCase("species.participation.Observation")) {

							MailData mailData = updateMailData(objectId, userGroupList.getMailData());
							logActivity.LogActivity(request.getHeader(HttpHeaders.AUTHORIZATION), description, objectId,
									objectId, "observation", activityId, "UnFeatured", mailData);
						} else if (objectType.equalsIgnoreCase("content.eml.Documen")) {
							MailData mailData = updateDocumentMailData(objectId, userGroupList.getMailData());
							logActivity.LogDocumentActivities(request.getHeader(HttpHeaders.AUTHORIZATION), description,
									objectId, objectId, objectType, activityId, "UnFeatured", mailData);
						} else if (objectType.equalsIgnoreCase("species.Species")) {
							MailData mailData = null;
//							TODO mailData
							logActivity.logSpeciesActivities(request.getHeader(HttpHeaders.AUTHORIZATION), description,
									objectId, objectId, species, activityId, "UnFeatured", mailData);

						}

						break;
					}
				}
			}

			resultList = featuredDao.fetchAllFeatured(objectType, objectId);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return resultList;
	}

	@Override
	public List<UserGroupSpeciesGroup> getUserGroupSpeciesGroup(Long ugId) {
		List<UserGroupSpeciesGroup> result = ugSGroupDao.findByUserGroupId(ugId);
		return result;
	}

	@Override
	public MailData updateMailData(Long observationId, MailData mailData) {
		List<UserGroupMailData> userGroup = new ArrayList<UserGroupMailData>();
		List<UserGroupIbp> updatedUG = fetchByObservationId(observationId);

		if (updatedUG != null && !updatedUG.isEmpty()) {
			for (UserGroupIbp ug : updatedUG) {
				UserGroupMailData ugMail = new UserGroupMailData();
				ugMail.setIcon(ug.getIcon());
				ugMail.setId(ug.getId());
				ugMail.setName(ug.getName());
				ugMail.setWebAddress(ug.getWebAddress());

				userGroup.add(ugMail);
			}
		}

		mailData.setUserGroupData(userGroup);
		return mailData;
	}

	@Override
	public Boolean addMemberRoleInvitaions(HttpServletRequest request, CommonProfile profile,
			UserGroupInvitationData userGroupInvitations) {

		try {

			InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream("config.properties");
			Properties properties = new Properties();
			try {
				properties.load(in);
			} catch (IOException e) {
				logger.error(e.getMessage());
			}

			String serverUrl = properties.getProperty("serverUrl");
			Long founderId = Long.parseLong(properties.getProperty("userGroupFounder"));
			Long moderatorId = Long.parseLong(properties.getProperty("userGroupExpert"));
			Long memberId = Long.parseLong(properties.getProperty("userGroupMember"));
			in.close();

			Long inviterId = Long.parseLong(profile.getId());
			List<InvitaionMailData> inviteData = new ArrayList<InvitaionMailData>();
			JSONArray roles = (JSONArray) profile.getAttribute("roles");
			Boolean isFounder = ugMemberService.checkFounderRole(inviterId, userGroupInvitations.getUserGroupId());
			if (roles.contains(roleAdmin) || Boolean.TRUE.equals(isFounder)) {
				UserGroupIbp userGroupIbp = fetchByGroupIdIbp(userGroupInvitations.getUserGroupId());
				if (!userGroupInvitations.getFounderIds().isEmpty()) {
					for (Long inviteeId : userGroupInvitations.getFounderIds()) {
						InvitaionMailData mailData = getInvitationMailData(request, inviterId, inviteeId,
								userGroupInvitations.getUserGroupId(), founderId, "Founder", null, userGroupIbp, false);

						if (mailData != null) {
							validateMember(request, inviteeId, mailData.getToken());
							inviteData.add(mailData);
						}

					}
				}
				if (!userGroupInvitations.getModeratorsIds().isEmpty()) {
					for (Long inviteeId : userGroupInvitations.getModeratorsIds()) {

						InvitaionMailData mailData = getInvitationMailData(request, inviterId, inviteeId,
								userGroupInvitations.getUserGroupId(), moderatorId, "Moderator", null, userGroupIbp,
								false);

						if (mailData != null) {
							validateMember(request, inviteeId, mailData.getToken());
							inviteData.add(mailData);

						}

					}
				}
				if (!userGroupInvitations.getMemberIds().isEmpty()) {
					for (Long inviteeId : userGroupInvitations.getMemberIds()) {

						InvitaionMailData mailData = getInvitationMailData(request, inviterId, inviteeId,
								userGroupInvitations.getUserGroupId(), memberId, "Member", null, userGroupIbp, false);

						if (mailData != null) {
							validateMember(request, inviteeId, mailData.getToken());
							inviteData.add(mailData);

						}

					}
				}
				if (!userGroupInvitations.getFounderEmail().isEmpty()) {
					for (String email : userGroupInvitations.getFounderEmail()) {
						InvitaionMailData mailData = getInvitationMailData(request, inviterId, null,
								userGroupInvitations.getUserGroupId(), founderId, "Founder", email, userGroupIbp,
								false);
						if (mailData != null)
							inviteData.add(mailData);
					}
				}
				if (!userGroupInvitations.getModeratorsEmail().isEmpty()) {
					for (String email : userGroupInvitations.getModeratorsEmail()) {
						InvitaionMailData mailData = getInvitationMailData(request, inviterId, null,
								userGroupInvitations.getUserGroupId(), moderatorId, "Moderator", email, userGroupIbp,
								false);
						if (mailData != null)
							inviteData.add(mailData);
					}
				}
				mailUtils.sendInvites(inviteData, serverUrl);
				return true;
			}
			return false;
		} catch (Exception e) {
			logger.error(e.getMessage());
		}

		return false;

	}

	private InvitaionMailData getInvitationMailData(HttpServletRequest request, Long inviterId, Long inviteeId,
			Long userGroupId, Long roleId, String role, String email, UserGroupIbp userGroupIbp,
			Boolean isEncryptionRequired) {
		try {
			if (inviteeId != null) {
				UserGroupInvitation ugInvitee = ugInvitationDao.findByUserIdUGId(inviteeId, userGroupId);
				if (ugInvitee != null)
					ugInvitationDao.delete(ugInvitee);
			}
			if (email != null) {
				UserGroupInvitation ugEmailInvitees = ugInvitationDao.findByUGIdEmailId(userGroupId, email);
				if (ugEmailInvitees != null)
					ugInvitationDao.delete(ugEmailInvitees);
			}
			UserGroupInvitation ugInvite = new UserGroupInvitation(null, inviterId, inviteeId, userGroupId, roleId,
					email);
			ugInvite = ugInvitationDao.save(ugInvite);

			if (ugInvite != null) {
				String desc = "Sent invitation for Role: " + role;
				if (inviteeId == null && email != null) {
					String emailsplit[] = email.split("@");
					desc = "Sent Invitaion to a NON-registred user : " + emailsplit[0] + " for role : " + role;
				}
				logActivity.logUserGroupActivities(request.getHeader(HttpHeaders.AUTHORIZATION), desc, userGroupId,
						userGroupId, "userGroup", inviteeId, "Invitation Sent");
			}
//			create mail invitation data
			String ugInviteStr = objectMapper.writeValueAsString(ugInvite);
			UserIbp inviterObject = userService.getUserIbp(inviterId.toString());
			String inviteeName = "";
			String inviteeEmail = "";
			if (inviteeId != null) {
				User inviteeObject = userService.getUser(inviteeId.toString());
				inviteeName = inviteeObject.getName();
				inviteeEmail = inviteeObject.getEmail();
			} else if (email != null) {
				inviteeName = email.split("@")[0];
				inviteeEmail = email;
			}
			String userGroupInvitationString = ugInviteStr;
			if (Boolean.TRUE.equals(isEncryptionRequired)) {
				userGroupInvitationString = encryptionUtils.encrypt(ugInviteStr);
			}

			if (inviteeEmail != null && inviteeEmail.length() > 0) {
				InvitaionMailData mailData = new InvitaionMailData(inviterObject, inviteeName, inviteeEmail,
						userGroupIbp, role, userGroupInvitationString);

				return mailData;
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return null;
	}

	private UserGroupIbp validateMember(HttpServletRequest request, Long userId, String userGroupInvitation) {
		try {

			InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream("config.properties");
			Properties properties = new Properties();
			try {
				properties.load(in);
			} catch (IOException e) {
				logger.error(e.getMessage());
			}

			Long founderId = Long.parseLong(properties.getProperty("userGroupFounder"));
			Long moderatorId = Long.parseLong(properties.getProperty("userGroupExpert"));
			in.close();

			UserGroupInvitation ugInvte = objectMapper.readValue(userGroupInvitation, UserGroupInvitation.class);
			if (userId.equals(ugInvte.getInviteeId())) {
				UserGroupInvitation ugInviteDB = ugInvitationDao.findByUserIdUGId(userId, ugInvte.getUserGroupId());
				if (ugInviteDB.equals(ugInvte)) {
					Boolean isMember = ugMemberService.checkUserGroupMember(userId, ugInviteDB.getUserGroupId());
					if (!isMember) {
						ugMemberService.addMemberUG(userId, ugInviteDB.getRoleId(), ugInviteDB.getUserGroupId());
						ugInvitationDao.delete(ugInviteDB);

						String role = "Member";
						if (ugInviteDB.getRoleId().equals(founderId))
							role = "Founder";
						else if (ugInviteDB.getRoleId().equals(moderatorId))
							role = "Moderator";

						String desc = "Joined Group with Role:" + role;
						logActivity.logUserGroupActivities(request.getHeader(HttpHeaders.AUTHORIZATION), desc,
								ugInviteDB.getUserGroupId(), ugInviteDB.getUserGroupId(), "userGroup", userId,
								"Joined group");

						return fetchByGroupIdIbp(ugInviteDB.getUserGroupId());
					} else {
//						code for role update

						String previousRole = "Member";
						Boolean isFounder = ugMemberService.checkFounderRole(userId, ugInviteDB.getUserGroupId());
						if (isFounder)
							previousRole = "Founder";
						else {
							Boolean isModerator = ugMemberService.checkModeratorRole(userId,
									ugInviteDB.getUserGroupId());
							if (isModerator)
								previousRole = "Moderator";
						}
						ugMemberService.removeGroupMember(userId, ugInviteDB.getUserGroupId());

						ugMemberService.addMemberUG(userId, ugInviteDB.getRoleId(), ugInviteDB.getUserGroupId());
						ugInvitationDao.delete(ugInviteDB);

						String role = "Member";
						if (ugInviteDB.getRoleId().equals(founderId))
							role = "Founder";
						else if (ugInviteDB.getRoleId().equals(moderatorId))
							role = "Moderator";

						String desc = "User Role Updated from ROLE " + previousRole + " to ROLE " + role;
						logActivity.logUserGroupActivities(request.getHeader(HttpHeaders.AUTHORIZATION), desc,
								ugInviteDB.getUserGroupId(), ugInviteDB.getUserGroupId(), "userGroup", userId,
								"Role updated");

						return fetchByGroupIdIbp(ugInviteDB.getUserGroupId());

					}

				}
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}

		return null;
	}

	@Override
	public Boolean removeUser(HttpServletRequest request, String userGroupId, String userId) {
		try {

			CommonProfile profile = AuthUtil.getProfileFromRequest(request);
			Long tokenUserId = Long.parseLong(profile.getId());
			JSONArray roles = (JSONArray) profile.getAttribute("roles");
			Boolean isFounder = ugMemberService.checkFounderRole(tokenUserId, Long.parseLong(userGroupId));
			if (roles.contains(roleAdmin) || Boolean.TRUE.equals(isFounder)) {
				Boolean result = ugMemberService.removeGroupMember(Long.parseLong(userId), Long.parseLong(userGroupId));
				if (result) {
					logActivity.logUserGroupActivities(request.getHeader(HttpHeaders.AUTHORIZATION), null,
							Long.parseLong(userGroupId), Long.parseLong(userGroupId), "userGroup",
							Long.parseLong(userId), "Removed user");
				}
				return result;
			}

		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return null;
	}

	@Override
	public Boolean removeBulkUser(HttpServletRequest request, String userGroupId, String userIds) {
		try {
			CommonProfile profile = AuthUtil.getProfileFromRequest(request);
			Long tokenUserId = Long.parseLong(profile.getId());
			JSONArray roles = (JSONArray) profile.getAttribute("roles");
			Boolean isFounder = ugMemberService.checkFounderRole(tokenUserId, Long.parseLong(userGroupId));

			if (roles.contains(roleAdmin) || Boolean.TRUE.equals(isFounder)) {
				List<Long> userList = Arrays.stream(userIds.split(",")).map(Long::parseLong)
						.collect(Collectors.toList());
				return ugMemberService.removeBulkGroupMember(request, userList, Long.parseLong(userGroupId));

			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return false;
	}

	@Override
	public Boolean leaveGroup(HttpServletRequest request, Long userId, String userGroupId) {
		try {
			Boolean result = ugMemberService.removeGroupMember(userId, Long.parseLong(userGroupId));
			if (result) {
				logActivity.logUserGroupActivities(request.getHeader(HttpHeaders.AUTHORIZATION), null,
						Long.parseLong(userGroupId), Long.parseLong(userGroupId), "userGroup", userId, "Left Group");
			}
			return result;
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return null;
	}

	@Override
	public Boolean joinGroup(HttpServletRequest request, Long userId, String userGroupId) {
		try {
			UserGroup userGroup = userGroupDao.findById(Long.parseLong(userGroupId));
			if (userGroup.getAllowUserToJoin()) {
//				OPEN GROUP - Directly Join the Group
				Boolean result = ugMemberService.joinGroup(userId, Long.parseLong(userGroupId));
				if (result) {
					String desc = "Joined Group with Role: Member";
					logActivity.logUserGroupActivities(request.getHeader(HttpHeaders.AUTHORIZATION), desc,
							Long.parseLong(userGroupId), Long.parseLong(userGroupId), "userGroup", userId,
							"Joined group");
				}
				return result;
			} else {
//				CLOSED GROUP - send out a Request

				InputStream in = Thread.currentThread().getContextClassLoader()
						.getResourceAsStream("config.properties");
				Properties properties = new Properties();
				try {
					properties.load(in);
				} catch (IOException e) {
					logger.error(e.getMessage());
				}
				String serverUrl = properties.getProperty("serverUrl");
				in.close();

				UserGroupJoinRequest ugJoin = ugJoinRequestDao.findByuserIdUGId(userId, Long.parseLong(userGroupId));
				if (ugJoin == null) {
					ugJoin = new UserGroupJoinRequest(null, Long.parseLong(userGroupId), userId);
					ugJoin = ugJoinRequestDao.save(ugJoin);
					String desc = "Requested to Join Group with Role: Member";
					logActivity.logUserGroupActivities(request.getHeader(HttpHeaders.AUTHORIZATION), desc,
							Long.parseLong(userGroupId), Long.parseLong(userGroupId), "userGroup", userId,
							"Requested Join");
				}
				UserIbp userIbp = userService.getUserIbp(userId.toString());
				String ugJoinStr = objectMapper.writeValueAsString(ugJoin);
				String encrptyedKey = encryptionUtils.encrypt(ugJoinStr);
				List<User> userList = ugMemberService.getFounderModerator(Long.parseLong(userGroupId));
				UserGroupIbp userGroupIbp = fetchByGroupIdIbp(Long.parseLong(userGroupId));

				mailUtils.sendRequest(userList, userIbp, userGroupIbp, encrptyedKey, serverUrl);
				return true;

			}

		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return null;
	}

	@Override
	public UserGroupIbp validateJoinRequest(HttpServletRequest request, String token) {
		try {

			InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream("config.properties");
			Properties properties = new Properties();
			try {
				properties.load(in);
			} catch (IOException e) {
				logger.error(e.getMessage());
			}
			Long memberId = Long.parseLong(properties.getProperty("userGroupMember"));
			in.close();
			String decryptedStr = encryptionUtils.decrypt(token);
			UserGroupJoinRequest userGroupJoin = objectMapper.readValue(decryptedStr, UserGroupJoinRequest.class);

			CommonProfile profile = AuthUtil.getProfileFromRequest(request);
			Long userId = Long.parseLong(profile.getId());
			JSONArray roles = (JSONArray) profile.getAttribute("roles");

			Boolean isFounder = ugMemberService.checkFounderRole(userId, userGroupJoin.getUserGroupId());
			Boolean isModerator = ugMemberService.checkModeratorRole(userId, userGroupJoin.getUserGroupId());
			if (roles.contains(roleAdmin) || Boolean.TRUE.equals(isFounder) || Boolean.TRUE.equals(isModerator)) {
				UserGroupJoinRequest originalObject = ugJoinRequestDao.findById(userGroupJoin.getId());
				if (originalObject != null) {
					if (userGroupJoin.equals(originalObject)) {
						Boolean isMember = ugMemberService.checkUserGroupMember(originalObject.getUserId(),
								originalObject.getUserGroupId());
						if (!isMember) {
							ugMemberService.addMemberUG(originalObject.getUserId(), memberId,
									originalObject.getUserGroupId());
							String desc = "Joined Group with Role: Member";
							logActivity.logUserGroupActivities(request.getHeader(HttpHeaders.AUTHORIZATION), desc,
									originalObject.getUserGroupId(), originalObject.getUserGroupId(), "userGroup",
									originalObject.getUserId(), "Joined group");
							ugJoinRequestDao.delete(originalObject);
						} else {
							ugJoinRequestDao.delete(originalObject);
						}
						return fetchByGroupIdIbp(originalObject.getUserGroupId());
					}
				}

			}

		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return null;
	}

	@Override
	public Boolean sendInvitesForMemberRole(HttpServletRequest request, CommonProfile profile, Long userGroupId,
			List<Long> inviteeList) {

		try {

			InputStream in = Thread.currentThread().getContextClassLoader().getResourceAsStream("config.properties");
			Properties properties = new Properties();
			try {
				properties.load(in);
			} catch (IOException e) {
				logger.error(e.getMessage());
			}
			Long memberId = Long.parseLong(properties.getProperty("userGroupMember"));
			String serverUrl = properties.getProperty("serverUrl");
			in.close();

			Long inviterId = Long.parseLong(profile.getId());
			UserGroup userGroup = userGroupDao.findById(userGroupId);
			JSONArray roles = (JSONArray) profile.getAttribute("roles");
			UserGroupIbp userGroupIbp = fetchByGroupIdIbp(userGroupId);
			List<InvitaionMailData> iniviteData = new ArrayList<InvitaionMailData>();

//			open group any body can send the invitation
			if (userGroup.getAllowUserToJoin().equals(true)) {
				for (Long inviteeId : inviteeList) {
					InvitaionMailData mailData = getInvitationMailData(request, inviterId, inviteeId, userGroupId,
							memberId, "Member", null, userGroupIbp, true);
					if (mailData != null) {
						iniviteData.add(mailData);
						String desc = "Sent invitation for Role: Member";
						logActivity.logUserGroupActivities(request.getHeader(HttpHeaders.AUTHORIZATION), desc,
								userGroupId, userGroupId, "userGroup", inviteeId, "Invitation Sent");
					}

				}

			} else {
//			closer group check for founder , moderator and admin
				Boolean isFounder = ugMemberService.checkFounderRole(inviterId, userGroupId);
				Boolean isModerator = ugMemberService.checkModeratorRole(inviterId, userGroupId);
				if (roles.contains(roleAdmin) || Boolean.TRUE.equals(isFounder) || Boolean.TRUE.equals(isModerator)) {

					for (Long inviteeId : inviteeList) {
						InvitaionMailData mailData = getInvitationMailData(request, inviterId, inviteeId, userGroupId,
								memberId, "Member", null, userGroupIbp, true);
						if (mailData != null) {
							iniviteData.add(mailData);
							String desc = "Sent invitation for Role: Member";
							logActivity.logUserGroupActivities(request.getHeader(HttpHeaders.AUTHORIZATION), desc,
									userGroupId, userGroupId, "userGroup", inviteeId, "Invitation Sent");
						}
					}
				}
			}
			mailUtils.sendInvites(iniviteData, serverUrl);
			return true;
		} catch (Exception e) {
			logger.error(e.getMessage());
		}

		return null;
	}

	@Override
	public Boolean bulkPosting(HttpServletRequest request, CommonProfile profile,
			BulkGroupPostingData bulkGroupPosting) {
		try {

			List<Long> userGroupList = bulkGroupPosting.getUserGroupList();
			List<UserGroupObvFilterData> ugObservationFilterList = bulkGroupPosting.getUgObvFilterDataList();

			if (userGroupList == null || userGroupList.isEmpty() || ugObservationFilterList == null
					|| ugObservationFilterList.isEmpty() || bulkGroupPosting.getRecordType() == null
					|| bulkGroupPosting.getRecordType().isEmpty())
				return false;

			for (Long userGroupId : userGroupList) {

				JSONArray roles = (JSONArray) profile.getAttribute("roles");
				Long userId = Long.parseLong(profile.getId());
				Boolean isFounder = ugMemberService.checkFounderRole(userId, userGroupId);
				Boolean isModerator = ugMemberService.checkModeratorRole(userId, userGroupId);
				int counter = 0;

				if (roles.contains(roleAdmin) || Boolean.TRUE.equals(isFounder) || Boolean.TRUE.equals(isModerator)) {

					for (UserGroupObvFilterData ugData : ugObservationFilterList) {
						List<Long> ugList = new ArrayList<Long>();
						ugList.add(userGroupId);

						UserGroupCreateDatatable ugDataTableGroupList = new UserGroupCreateDatatable();
						ugDataTableGroupList.setUserGroupIds(ugList);

						if (bulkGroupPosting.getRecordType().contains(RecordType.OBSERVATION.getValue())) {
							UserGroupObservation isAlreadyMapped = userGroupObvDao
									.checkObservationUGMApping(ugData.getObservationId(), userGroupId);
							if (isAlreadyMapped != null)
								continue;

							UserGroupMappingCreateData ugObservationPayload = new UserGroupMappingCreateData();
							ugObservationPayload.setMailData(null);
							ugObservationPayload.setUserGroups(ugList);
							ugObservationPayload.setUgFilterData(ugData);
							createUserGroupObservationMapping(request, ugData.getObservationId(), ugObservationPayload,
									false, true);
							counter++;
						} else if (bulkGroupPosting.getRecordType().contains(RecordType.DOCUMENT.getValue())) {
							UserGroupDocCreateData ugDatapayload = new UserGroupDocCreateData();
							ugDatapayload.setDocumentId(ugData.getObservationId());
							ugDatapayload.setUserGroupIds(ugList);
							ugDatapayload.setMailData(null);
							createUGDocMapping(request, ugDatapayload);
							counter++;
						} else if (bulkGroupPosting.getRecordType().contains(RecordType.DATATABLE.getValue())) {
							ugDatatableService.updateUserGroupDatatableMapping(request, ugData.getObservationId(),
									ugDataTableGroupList);
							counter++;
						} else if (bulkGroupPosting.getRecordType().contains(RecordType.SPECIES.getValue())) {
							UserGroupSpeciesCreateData ugSpeciesPayload = new UserGroupSpeciesCreateData();
							ugSpeciesPayload.setUserGroupIds(ugList);
							createUGSpeciesMapping(request, ugData.getObservationId(), ugSpeciesPayload);
							counter++;
						}

					}

					if (counter > 0) {
						String description = "Posted " + counter + " " + bulkGroupPosting.getRecordType()
								+ "  to group";
						logActivity.logUserGroupActivities(request.getHeader(HttpHeaders.AUTHORIZATION), description,
								userGroupId, userGroupId, "userGroup", userGroupId, "Posted resource");
					}
				}
			}
			return true;

		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return false;
	}

	@Override
	public Boolean bulkRemoving(HttpServletRequest request, CommonProfile profile,
			BulkGroupUnPostingData bulkGroupUnPosting) {
		try {

			Long userId = Long.parseLong(profile.getId());
			List<Long> userGroupList = bulkGroupUnPosting.getUserGroupList();
			List<UserGroupObvFilterData> ugFilterList = bulkGroupUnPosting.getUgFilterDataList();
			String recordType = bulkGroupUnPosting.getRecordType();

			if (userGroupList == null || userGroupList.isEmpty() || ugFilterList == null || ugFilterList.isEmpty()
					|| recordType == null || recordType.isEmpty())
				return false;

			for (Long userGroupId : userGroupList) {

				JSONArray roles = (JSONArray) profile.getAttribute("roles");
				Boolean isFounder = ugMemberService.checkFounderRole(userId, userGroupId);
				Boolean isModerator = ugMemberService.checkModeratorRole(userId, userGroupId);
				int counter = 0;

				if (roles.contains(roleAdmin) || Boolean.TRUE.equals(isFounder) || Boolean.TRUE.equals(isModerator)) {
					for (UserGroupObvFilterData item : ugFilterList) {
						List<Long> ugList = new ArrayList<Long>();
						ugList.add(userGroupId);

						UserGroupCreateDatatable ugDataTableGroupList = new UserGroupCreateDatatable();
						ugDataTableGroupList.setUserGroupIds(ugList);

						if (recordType.contains(RecordType.OBSERVATION.getValue())) {
							UserGroupMappingCreateData ugObservationPayload = new UserGroupMappingCreateData();
							ugObservationPayload.setMailData(null);
							ugObservationPayload.setUserGroups(ugList);
							ugObservationPayload.setUgFilterData(item);
							removeUserGroupObservationMapping(request, item.getObservationId(), ugObservationPayload,
									false);
							counter++;
						} else if (recordType.contains(RecordType.DOCUMENT.getValue())) {
							UserGroupDocCreateData ugDatapayload = new UserGroupDocCreateData();
							ugDatapayload.setDocumentId(item.getObservationId());
							ugDatapayload.setUserGroupIds(ugList);
							ugDatapayload.setMailData(null);
							updateUGDocMapping(request, ugDatapayload);
							counter++;
						} else if (recordType.contains(RecordType.DATATABLE.getValue())) {
							ugDatatableService.updateUserGroupDatatableMapping(request, item.getObservationId(),
									ugDataTableGroupList);
							counter++;
						} else if (recordType.contains(RecordType.SPECIES.getValue())) {
							UserGroupSpeciesCreateData ugSpeciesPayload = new UserGroupSpeciesCreateData();
							ugSpeciesPayload.setUserGroupIds(ugList);
							removeUGSpeciesMapping(request, item.getObservationId(), ugSpeciesPayload);
							counter++;
						}

					}

					if (counter > 0) {
						String description = "Removed " + counter + " " + bulkGroupUnPosting.getRecordType()
								+ "  to group";
						logActivity.logUserGroupActivities(request.getHeader(HttpHeaders.AUTHORIZATION), description,
								userGroupId, userGroupId, "userGroup", userGroupId, "Removed resoruce");
					}
				}
			}

			return true;

		} catch (Exception e) {
			logger.error(e.getMessage());
		}

		return false;
	}

	@Override
	public UserGroupIbp createUserGroup(HttpServletRequest request, CommonProfile profile,
			UserGroupCreateData ugCreateData) {
		try {
			String webAddress = ugCreateData.getName().replace(" ", "_");

			// Validate that the web address is unique before creating or updating a group
			boolean isAllowed = userGroupDao.isWebAddressAllowedForGroup(webAddress, null);
			if (!isAllowed) {
				throw new IllegalArgumentException("Webaddress '" + webAddress + "' is already used by another group");
			}

			UserGroup userGroup = new UserGroup(null, true, true, true, ugCreateData.getAllowUserToJoin(),
					ugCreateData.getDescription(), ugCreateData.getDomainName(), new Date(), ugCreateData.getHomePage(),
					ugCreateData.getIcon(), false, ugCreateData.getName(), ugCreateData.getNeLatitude(),
					ugCreateData.getNeLongitude(), ugCreateData.getSwLatitude(), ugCreateData.getSwLongitude(),
					ugCreateData.getTheme(), 1L, webAddress,
					ugCreateData.getLanguageId() != null ? ugCreateData.getLanguageId() : defaultLanguageId, new Date(),
					ugCreateData.getShowGallery(), ugCreateData.getShowStats(), ugCreateData.getShowRecentObservation(),
					ugCreateData.getShowGridMap(), ugCreateData.getShowPartners(), ugCreateData.getShowDesc(),
					ugCreateData.getMediaToggle(), null);

			userGroup = userGroupDao.save(userGroup);
			userGroup.setGroupId(userGroup.getId());
			userGroupDao.update(userGroup);

			if (ugCreateData.getSpeciesGroup() != null && !ugCreateData.getSpeciesGroup().isEmpty()) {
				for (Long speciesGroupId : ugCreateData.getSpeciesGroup()) {
					UserGroupSpeciesGroup ugSpeciesGroup = new UserGroupSpeciesGroup(userGroup.getId(), speciesGroupId);
					ugSGroupDao.save(ugSpeciesGroup);
				}
			}

			if (ugCreateData.getHabitatId() != null && !ugCreateData.getHabitatId().isEmpty()) {
				for (Long habitatId : ugCreateData.getHabitatId()) {
					UserGroupHabitat ugHabitat = new UserGroupHabitat(habitatId, userGroup.getId());
					ugHabitatDao.save(ugHabitat);
				}
			}

			UserGroupAddMemebr memberList = new UserGroupAddMemebr(
					new ArrayList<Long>(Arrays.asList(Long.parseLong(profile.getId()))), null, null);
			addMemberDirectly(request, userGroup.getId(), memberList);

			if (ugCreateData.getInvitationData() != null) {
				UserGroupInvitationData userGroupInvitations = ugCreateData.getInvitationData();
				userGroupInvitations.setUserGroupId(userGroup.getId());
				addMemberRoleInvitaions(request, profile, userGroupInvitations);
			}

			logActivity.logUserGroupActivities(request.getHeader(HttpHeaders.AUTHORIZATION), null, userGroup.getId(),
					userGroup.getId(), "userGroup", null, "Group created");

			return fetchByGroupIdIbp(userGroup.getId());

		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return null;
	}

	@Override
	public UserGroupEditData getUGEditData(HttpServletRequest request, CommonProfile profile, Long userGroupId) {
		try {
			JSONArray roles = (JSONArray) profile.getAttribute("roles");
			Long userId = Long.parseLong(profile.getId());
			Boolean isFounder = ugMemberService.checkFounderRole(userId, userGroupId);
			if (roles.contains(roleAdmin) || Boolean.TRUE.equals(isFounder)) {
				List<UserGroup> userGroupTranslations = userGroupDao.findByGroupId(userGroupId);
				List<Map<String, Object>> translation = new ArrayList<>();
				for (UserGroup userGroup : userGroupTranslations) {
					Map<String, Object> nameMap = new HashMap<>();
					nameMap.put("language", userGroup.getLanguageId());
					nameMap.put("name", userGroup.getName());
					nameMap.put("description", userGroup.getDescription());
					nameMap.put("id", userGroup.getId());
					translation.add(nameMap);
				}
				if (userGroupTranslations != null) {
					List<UserGroupSpeciesGroup> ugSpeciesGroups = ugSGroupDao.findByUserGroupId(userGroupId);
					List<UserGroupHabitat> ugHabitats = ugHabitatDao.findByUserGroupId(userGroupId);
					List<Long> speciesGroupId = new ArrayList<Long>();
					List<Long> habitatId = new ArrayList<Long>();
					for (UserGroupSpeciesGroup ugSpeciesGroup : ugSpeciesGroups) {
						speciesGroupId.add(ugSpeciesGroup.getSpeciesGroupId());
					}
					for (UserGroupHabitat ugHabitat : ugHabitats) {
						habitatId.add(ugHabitat.getHabitatId());
					}
					UserGroupEditData ugEditData = new UserGroupEditData(
							userGroupTranslations.get(0).getAllowUserToJoin(),
							userGroupTranslations.get(0).getHomePage(), userGroupTranslations.get(0).getIcon(),
							userGroupTranslations.get(0).getDomianName(), translation,
							userGroupTranslations.get(0).getNeLatitude(), userGroupTranslations.get(0).getNeLongitude(),
							userGroupTranslations.get(0).getSwLatitude(), userGroupTranslations.get(0).getSwLongitude(),
							userGroupTranslations.get(0).getTheme(), userGroupTranslations.get(0).getLanguageId(),
							speciesGroupId, habitatId, userGroupTranslations.get(0).getWebAddress());
					return ugEditData;
				}
			}

		} catch (Exception e) {
			logger.error(e.getMessage());
		}

		return null;
	}

	@Override
	public UserGroupIbp saveUGEdit(HttpServletRequest request, CommonProfile profile, Long userGroupId,
			UserGroupEditData ugEditData) {
		try {

			JSONArray roles = (JSONArray) profile.getAttribute("roles");
			Long userId = Long.parseLong(profile.getId());
			Boolean isFounder = ugMemberService.checkFounderRole(userId, userGroupId);
			boolean isAllowed = userGroupDao.isWebAddressAllowedForGroup(ugEditData.getWebAddress(), userGroupId);
			if (!isAllowed) {
				throw new IllegalArgumentException(
						"Webaddress '" + ugEditData.getWebAddress() + "' is already used by another group");
			}

			if (roles.contains(roleAdmin) || Boolean.TRUE.equals(isFounder)) {
				UserGroup ug = userGroupDao.findById(userGroupId);
				for (Map<String, Object> translationData : ugEditData.getTranslation()) {
					if (translationData.get("id") != null) {
						UserGroup userGroup = new UserGroup(Long.parseLong(translationData.get("id").toString()),
								ug.getAllow_members_to_make_species_call(), ug.getAllow_non_members_to_comment(),
								ug.getAllow_obv_cross_posting(), ugEditData.getAllowUserToJoin(),
								translationData.get("description").toString(), ug.getDomianName(), new Date(),
								ugEditData.getHomePage(), ugEditData.getIcon(), false,
								translationData.get("name").toString(), ugEditData.getNeLatitude(),
								ugEditData.getNeLongitude(), ugEditData.getSwLatitude(), ugEditData.getSwLongitude(),
								ugEditData.getTheme(), ug.getVisitCount(), ugEditData.getWebAddress(),
								Long.parseLong(translationData.get("language").toString()), new Date(),
								ug.getShowGallery(), ug.getShowStats(), ug.getShowRecentObservations(),
								ug.getShowGridMap(), ug.getShowPartners(), ug.getShowDesc(), ug.getMediaToggle(),
								ug.getGroupId());

						userGroup = userGroupDao.update(userGroup);
					} else {
						UserGroup userGroup = new UserGroup(null, ug.getAllow_members_to_make_species_call(),
								ug.getAllow_non_members_to_comment(), ug.getAllow_obv_cross_posting(),
								ugEditData.getAllowUserToJoin(), translationData.get("description").toString(),
								ug.getDomianName(), new Date(), ugEditData.getHomePage(), ugEditData.getIcon(), false,
								translationData.get("name").toString(), ugEditData.getNeLatitude(),
								ugEditData.getNeLongitude(), ugEditData.getSwLatitude(), ugEditData.getSwLongitude(),
								ugEditData.getTheme(), ug.getVisitCount(), ugEditData.getWebAddress(),
								Long.parseLong(translationData.get("language").toString()), new Date(),
								ug.getShowGallery(), ug.getShowStats(), ug.getShowRecentObservations(),
								ug.getShowGridMap(), ug.getShowPartners(), ug.getShowDesc(), ug.getMediaToggle(),
								ug.getGroupId());
						userGroup = userGroupDao.save(userGroup);
					}
				}

				List<UserGroupSpeciesGroup> ugSpeciesGroups = ugSGroupDao.findByUserGroupId(userGroupId);
				List<UserGroupHabitat> ugHabitats = ugHabitatDao.findByUserGroupId(userGroupId);
				List<Long> speciesGroupList = new ArrayList<Long>();
				List<Long> habitatList = new ArrayList<Long>();
				if (ugSpeciesGroups != null && !ugSpeciesGroups.isEmpty()) {
					for (UserGroupSpeciesGroup ugSpeciesGroup : ugSpeciesGroups) {
						speciesGroupList.add(ugSpeciesGroup.getSpeciesGroupId());
					}
				}
				if (ugHabitats != null && !ugHabitats.isEmpty()) {
					for (UserGroupHabitat ugHabitat : ugHabitats) {
						habitatList.add(ugHabitat.getHabitatId());
					}
				}

				for (Long speciesGroupId : ugEditData.getSpeciesGroupId()) {
					if (!speciesGroupList.contains(speciesGroupId)) {
						UserGroupSpeciesGroup ugSpeciesGroup = new UserGroupSpeciesGroup(userGroupId, speciesGroupId);
						ugSGroupDao.save(ugSpeciesGroup);
					}

				}
				for (Long sGroupid : speciesGroupList) {
					if (!ugEditData.getSpeciesGroupId().contains(sGroupid)) {
						UserGroupSpeciesGroup ugSpeciesGroup = new UserGroupSpeciesGroup(userGroupId, sGroupid);
						ugSGroupDao.delete(ugSpeciesGroup);
					}
				}

				for (Long habitatId : ugEditData.getHabitatId()) {
					if (!habitatList.contains(habitatId)) {
						UserGroupHabitat ugHabitat = new UserGroupHabitat(habitatId, userGroupId);
						ugHabitatDao.save(ugHabitat);
					}
				}
				for (long habitatId : habitatList) {
					if (!ugEditData.getHabitatId().contains(habitatId)) {
						UserGroupHabitat ugHabitat = new UserGroupHabitat(habitatId, userGroupId);
						ugHabitatDao.delete(ugHabitat);
					}
				}

				logActivity.logUserGroupActivities(request.getHeader(HttpHeaders.AUTHORIZATION), null, userGroupId,
						userGroupId, "userGroup", null, "Group updated");

				return fetchByGroupIdIbp(userGroupId);
			}

		} catch (Exception e) {
			logger.error(e.getMessage());
		}

		return null;
	}

	@Override
	public Boolean addMemberDirectly(HttpServletRequest request, Long userGroupId, UserGroupAddMemebr memberList) {
		try {

			CommonProfile profile = AuthUtil.getProfileFromRequest(request);
			JSONArray roles = (JSONArray) profile.getAttribute("roles");
			if (roles.contains(roleAdmin)) {

				InputStream in = Thread.currentThread().getContextClassLoader()
						.getResourceAsStream("config.properties");
				Properties properties = new Properties();
				try {
					properties.load(in);
				} catch (IOException e) {
					logger.error(e.getMessage());
				}
				Long founderId = Long.parseLong(properties.getProperty("userGroupFounder"));
				Long moderatorId = Long.parseLong(properties.getProperty("userGroupExpert"));
				Long memberId = Long.parseLong(properties.getProperty("userGroupMember"));
				in.close();

				if (!memberList.getFounderList().isEmpty()) {
					GroupAddMember groupAddMember = new GroupAddMember();
					groupAddMember.setMemberList(memberList.getFounderList());
					groupAddMember.setRoleId(founderId);
					groupAddMember.setUserGroupId(userGroupId);
					List<Long> addedUser = ugMemberService.addMemberDirectly(groupAddMember);
					if (addedUser != null && !addedUser.isEmpty()) {
						for (Long userId : addedUser) {
							String desc = "Admin Added user with Role: Founder";
							logActivity.logUserGroupActivities(request.getHeader(HttpHeaders.AUTHORIZATION), desc,
									userGroupId, userGroupId, "userGroup", userId, "Joined group");
						}
					}
				}
				if (!memberList.getModeratorList().isEmpty()) {
					GroupAddMember groupAddMember = new GroupAddMember();
					groupAddMember.setMemberList(memberList.getModeratorList());
					groupAddMember.setRoleId(moderatorId);
					groupAddMember.setUserGroupId(userGroupId);
					List<Long> addedUser = ugMemberService.addMemberDirectly(groupAddMember);
					if (addedUser != null && !addedUser.isEmpty()) {
						for (Long userId : addedUser) {
							String desc = "Admin Added user with Role: Moderator";
							logActivity.logUserGroupActivities(request.getHeader(HttpHeaders.AUTHORIZATION), desc,
									userGroupId, userGroupId, "userGroup", userId, "Joined group");
						}
					}
				}
				if (!memberList.getMemberList().isEmpty()) {
					GroupAddMember groupAddMember = new GroupAddMember();
					groupAddMember.setMemberList(memberList.getMemberList());
					groupAddMember.setRoleId(memberId);
					groupAddMember.setUserGroupId(userGroupId);
					List<Long> addedUser = ugMemberService.addMemberDirectly(groupAddMember);
					if (addedUser != null && !addedUser.isEmpty()) {
						for (Long userId : addedUser) {
							String desc = "Admin Added user with Role: Member";
							logActivity.logUserGroupActivities(request.getHeader(HttpHeaders.AUTHORIZATION), desc,
									userGroupId, userGroupId, "userGroup", userId, "Joined group");
						}
					}
				}

				return true;

			}

		} catch (Exception e) {
			logger.error(e.getMessage());
		}

		return false;
	}

	@Override
	public List<Long> findAllObservation(Long userGroupId) {
		List<Long> observationList = new ArrayList<Long>();
		List<UserGroupObservation> ugObvMappingList = userGroupObvDao.findByUserGroupId(userGroupId);
		for (UserGroupObservation ugObv : ugObvMappingList) {
			observationList.add(ugObv.getObservationId());
		}
		return observationList;
	}

	@Override
	public AdministrationList getAdminMembers(String userGroupId) {
		try {
			List<UserIbp> founderList = ugMemberService.getFounderList(Long.parseLong(userGroupId));
			List<UserIbp> moderatorList = ugMemberService.getModeratorList(Long.parseLong(userGroupId));
			AdministrationList result = new AdministrationList(founderList, moderatorList);
			return result;
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return null;
	}

	@Override
	public List<UserGroupIbp> fetchByDocumentId(Long documentId) {
		try {
			List<UserGroupDocument> UserGroupDocuments = ugDocumentDao.findByDocumentId(documentId);
			List<UserGroupIbp> userGroupIbp = new ArrayList<UserGroupIbp>();
			for (UserGroupDocument ugDoc : UserGroupDocuments) {
				UserGroupIbp ugIbp = fetchByGroupIdIbp(ugDoc.getUserGroupId());
				if (ugIbp != null)
					userGroupIbp.add(ugIbp);
			}
			return userGroupIbp;
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return null;

	}

	@Override
	public List<UserGroupIbp> createUGDocMapping(HttpServletRequest request, UserGroupDocCreateData ugDocCreate) {
		List<Long> resultList = new ArrayList<Long>();
		for (Long ugId : ugDocCreate.getUserGroupIds()) {
			UserGroupDocument ugDoc = new UserGroupDocument(ugId, ugDocCreate.getDocumentId());
			ugDoc = ugDocumentDao.save(ugDoc);
			if (ugDoc != null) {
				resultList.add(ugDoc.getUserGroupId());
				UserGroupActivity ugActivity = new UserGroupActivity();
				UserGroupIbp ugIbp = fetchByGroupIdIbp(ugId);
				String description = null;
				ugActivity.setFeatured(null);
				ugActivity.setUserGroupId(ugIbp.getId());
				ugActivity.setUserGroupName(ugIbp.getName());
				ugActivity.setWebAddress(ugIbp.getWebAddress());
				try {
					description = objectMapper.writeValueAsString(ugActivity);
				} catch (Exception e) {
					logger.error(e.getMessage());
				}
				MailData mailData = null;
				if (ugDocCreate.getMailData() != null) {
					mailData = updateDocumentMailData(ugDocCreate.getDocumentId(), ugDocCreate.getMailData());
				}
				logActivity.LogDocumentActivities(request.getHeader(HttpHeaders.AUTHORIZATION), description,
						ugDocCreate.getDocumentId(), ugDocCreate.getDocumentId(), "document", ugDoc.getUserGroupId(),
						"Posted resource", mailData);
			}
		}

		return fetchByDocumentId(ugDocCreate.getDocumentId());
	}

	@Override
	public List<UserGroupIbp> updateUGDocMapping(HttpServletRequest request, UserGroupDocCreateData ugDocCreate) {

		List<Long> previousUserGroup = new ArrayList<Long>();
		List<UserGroupDocument> previousMapping = ugDocumentDao.findByDocumentId(ugDocCreate.getDocumentId());
		for (UserGroupDocument ug : previousMapping) {
			if (!(ugDocCreate.getUserGroupIds().contains(ug.getUserGroupId()))) {
				ugDocumentDao.delete(ug);

				UserGroupActivity ugActivity = new UserGroupActivity();
				UserGroupIbp ugIbp = fetchByGroupIdIbp(ug.getUserGroupId());
				String description = null;
				ugActivity.setFeatured(null);
				ugActivity.setUserGroupId(ugIbp.getId());
				ugActivity.setUserGroupName(ugIbp.getName());
				ugActivity.setWebAddress(ugIbp.getWebAddress());
				try {
					description = objectMapper.writeValueAsString(ugActivity);
				} catch (Exception e) {
					logger.error(e.getMessage());
				}

				MailData mailData = updateDocumentMailData(ugDocCreate.getDocumentId(), ugDocCreate.getMailData());

				logActivity.LogDocumentActivities(request.getHeader(HttpHeaders.AUTHORIZATION), description,
						ugDocCreate.getDocumentId(), ugDocCreate.getDocumentId(), "document", ug.getUserGroupId(),
						"Removed resoruce", mailData);
			}
			previousUserGroup.add(ug.getUserGroupId());
		}

		for (Long userGroupId : ugDocCreate.getUserGroupIds()) {
			if (!(previousUserGroup.contains(userGroupId))) {

				UserGroupDocument ugDoc = new UserGroupDocument(userGroupId, ugDocCreate.getDocumentId());
				ugDoc = ugDocumentDao.save(ugDoc);

				UserGroupActivity ugActivity = new UserGroupActivity();
				UserGroupIbp ugIbp = fetchByGroupIdIbp(userGroupId);
				String description = null;
				ugActivity.setFeatured(null);
				ugActivity.setUserGroupId(ugIbp.getId());
				ugActivity.setUserGroupName(ugIbp.getName());
				ugActivity.setWebAddress(ugIbp.getWebAddress());
				try {
					description = objectMapper.writeValueAsString(ugActivity);
				} catch (Exception e) {
					logger.error(e.getMessage());
				}

				MailData mailData = updateDocumentMailData(ugDocCreate.getDocumentId(), ugDocCreate.getMailData());
				logActivity.LogDocumentActivities((request.getHeader(HttpHeaders.AUTHORIZATION)), description,
						ugDocCreate.getDocumentId(), ugDocCreate.getDocumentId(), "document", userGroupId,
						"Posted resource", mailData);

			}
		}

		return fetchByDocumentId(ugDocCreate.getDocumentId());

	}

	private MailData updateDocumentMailData(Long documentId, MailData mailData) {
		List<UserGroupMailData> userGroup = new ArrayList<UserGroupMailData>();
		List<UserGroupIbp> updatedUG = fetchByDocumentId(documentId);

		for (UserGroupIbp ug : updatedUG) {
			UserGroupMailData ugMail = new UserGroupMailData();
			ugMail.setIcon(ug.getIcon());
			ugMail.setId(ug.getId());
			ugMail.setName(ug.getName());
			ugMail.setWebAddress(ug.getWebAddress());

			userGroup.add(ugMail);
		}
		mailData.setUserGroupData(userGroup);
		return mailData;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> registerUserProxy(HttpServletRequest request, AuthenticationDTO authDTO) {
		Map<String, Object> userData = new HashMap<String, Object>();
		try {
			userData = authenticationApi.signUp(authDTO.getCredentials());
			Long groupId = authDTO.getGroupId() != null ? Long.parseLong(authDTO.getGroupId().toString()) : null;
			if (Boolean.parseBoolean(userData.get("status").toString())) {
				boolean verificationRequired = Boolean.parseBoolean(userData.get("verificationRequired").toString());
				if (!verificationRequired) {
					MutableHttpServletRequest mutableRequest = new MutableHttpServletRequest(request);
					mutableRequest.putHeader(HttpHeaders.AUTHORIZATION,
							"Bearer " + userData.get("access_token").toString());
					CommonProfile profile = AuthUtil.getProfileFromRequest(mutableRequest);
					Long user = Long.parseLong(profile.getId());
					if (groupId != null) {
						joinGroup(mutableRequest, user, String.valueOf(groupId));
					}
				} else {
					Long userId = null;
					if (userData.containsKey("user")) {
						userId = Long.parseLong(((Map<String, Object>) userData.get("user")).get("id").toString());
					}
					if (userId != null && groupId != null) {
						UserGroupUserJoinRequest joinRequest = userGroupUserRequestDao
								.checkExistingGroupJoinRequest(userId, groupId);
						if (joinRequest == null) {
							joinRequest = new UserGroupUserJoinRequest(groupId, userId);
							joinRequest = userGroupUserRequestDao.save(joinRequest);
						}
					}
				}
			}
		} catch (Exception ex) {
			logger.error(ex.getMessage());
		}
		return userData;
	}

	@Override
	public Map<String, Object> signupProxy(HttpServletRequest request, String userName, String password, String mode) {
		Map<String, Object> userData = new HashMap<String, Object>();
		try {
			userData = authenticationApi.authenticate(userName, password, mode);
			if (Boolean.parseBoolean(userData.get("status").toString())) {
				boolean verificationRequired = Boolean.parseBoolean(userData.get("verificationRequired").toString());
				if (!verificationRequired) {
					MutableHttpServletRequest mutableRequest = new MutableHttpServletRequest(request);
					mutableRequest.putHeader(HttpHeaders.AUTHORIZATION,
							"Bearer " + userData.get("access_token").toString());
					CommonProfile profile = AuthUtil.getProfileFromRequest(mutableRequest);
					Long userId = Long.parseLong(profile.getId());
					UserGroupUserJoinRequest joinRequest = userGroupUserRequestDao.getGroupJoinRequestByUser(userId);
					if (joinRequest != null) {
						if (joinRequest.getUserGroupId() != null) {
							joinGroup(mutableRequest, userId, String.valueOf(joinRequest.getUserGroupId()));
						}
						userGroupUserRequestDao.delete(joinRequest);
					}
				}
			}
		} catch (Exception ex) {
			logger.error(ex.getMessage());
		}
		return userData;
	}

	@Override
	public Map<String, Object> verifyOTPProxy(HttpServletRequest request, Long id, String otp) {
		Map<String, Object> userData = new HashMap<String, Object>();
		try {
			userData = authenticationApi.validateAccount(id, otp);
			if (Boolean.parseBoolean(userData.get("status").toString())) {
				MutableHttpServletRequest mutableRequest = new MutableHttpServletRequest(request);
				mutableRequest.putHeader(HttpHeaders.AUTHORIZATION,
						"Bearer " + userData.get("access_token").toString());
				CommonProfile profile = AuthUtil.getProfileFromRequest(mutableRequest);
				Long userId = Long.parseLong(profile.getId());
				UserGroupUserJoinRequest joinRequest = userGroupUserRequestDao.getGroupJoinRequestByUser(userId);
				if (joinRequest != null) {
					if (joinRequest.getUserGroupId() != null) {
						joinGroup(mutableRequest, userId, String.valueOf(joinRequest.getUserGroupId()));
					}
					userGroupUserRequestDao.delete(joinRequest);
				}
			}
		} catch (Exception ex) {
			logger.error(ex.getMessage());
		}
		return userData;
	}

	/*
	 * @Override public UserGroupHomePageEditData
	 * getGroupHomePageEditData(HttpServletRequest request, Long userGroupId) { try
	 * {
	 * 
	 * CommonProfile profile = AuthUtil.getProfileFromRequest(request); JSONArray
	 * roles = (JSONArray) profile.getAttribute("roles"); Long userId =
	 * Long.parseLong(profile.getId()); Boolean isFounder =
	 * ugMemberService.checkFounderRole(userId, userGroupId); if
	 * (roles.contains(roleAdmin) || Boolean.TRUE.equals(isFounder)) { UserGroup
	 * userGroup = userGroupDao.findById(userGroupId);
	 * 
	 * List<GroupGallerySlider> gallerySlider =
	 * groupGallerySliderDao.findByUsergroupId(userGroupId);
	 * 
	 * UserGroupHomePageEditData result = new
	 * UserGroupHomePageEditData(userGroup.getShowGallery(),
	 * userGroup.getShowStats(), userGroup.getShowRecentObservations(),
	 * userGroup.getShowGridMap(), userGroup.getShowPartners(),
	 * userGroup.getShowDesc(), userGroup.getDescription(), groupedBySliderId);
	 * 
	 * return result; }
	 * 
	 * } catch (Exception e) { logger.error(e.getMessage()); } return null; }
	 */

	@Override
	public GroupHomePageData getGroupHomePageData(Long userGroupId, Long langId) {
		try {
			UserGroup userGroup = userGroupDao.findByGroupIdByLanguageId(userGroupId, langId);
			if (userGroup == null) {
				userGroup = userGroupDao.findByGroupIdByLanguageId(userGroupId, defaultLanguageId);
			}

			List<GroupGallerySlider> gallerySlider = groupGallerySliderDao.findByUsergroupId(userGroupId);
			Map<String, Map<Long, List<GroupGallerySlider>>> groupedBySliderId = new HashMap<>();

			for (GroupGallerySlider slider : gallerySlider) {
				if (slider.getAuthorId() != null) {
					UserIbp userIbp = userService.getUserIbp(slider.getAuthorId().toString());
					if (userIbp != null) {
						slider.setAuthorImage(userIbp.getProfilePic());
						slider.setAuthorName(userIbp.getName());
					}
				}

				String compositeKey = slider.getSliderId() + "|" + slider.getDisplayOrder();
				groupedBySliderId.computeIfAbsent(compositeKey, k -> new HashMap<>())
						.computeIfAbsent(slider.getLanguageId(), k -> new ArrayList<>()).add(slider);
			}

			Stats stats = statsDao.fetchStats(userGroupId);

			List<GroupGalleryConfig> miniGalleryData = groupGalleryConfigDao.getAllMiniSliderByGroup(userGroupId);
			List<Long> miniGalleryIds = new ArrayList<>();
			Map<String, Map<Long, List<GroupGalleryConfig>>> groupedByGalleryId = new HashMap<>();
			for (GroupGalleryConfig miniGallery : miniGalleryData) {
				Long galleryId = miniGallery.getGalleryId();
				if (!miniGalleryIds.contains(galleryId)) {
					miniGalleryIds.add(galleryId);
				}
				Long languageId = miniGallery.getLanguageId();
				groupedByGalleryId.computeIfAbsent(galleryId.toString(), k -> new HashMap<>())
						.computeIfAbsent(languageId, k -> new ArrayList<>()).add(miniGallery);
			}
			List<Map<String, Map<Long, List<MiniGroupGallerySlider>>>> miniGallerySlider = new ArrayList<>();
			for (Long miniGalleryId : miniGalleryIds) {
				List<MiniGroupGallerySlider> miniSliders = miniGroupGallerySliderDao
						.getAllGallerySliderInfoByGroupId(userGroupId, miniGalleryId);
				miniGallerySlider.add(groupMiniGallerySliders(miniSliders));
			}

			GroupHomePageData result = new GroupHomePageData(userGroup.getShowGallery(), userGroup.getShowStats(),
					userGroup.getShowRecentObservations(), userGroup.getShowGridMap(), userGroup.getShowPartners(),
					userGroup.getShowDesc(), userGroup.getDescription(), stats, groupedBySliderId, groupedByGalleryId,
					miniGallerySlider);

			return result;

		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return null;
	}

	private Map<String, Map<Long, List<MiniGroupGallerySlider>>> groupMiniGallerySliders(
			List<MiniGroupGallerySlider> sliders) {
		Map<String, Map<Long, List<MiniGroupGallerySlider>>> grouped = new HashMap<>();
		try {
			for (MiniGroupGallerySlider gallery : sliders) {
				if (gallery.getAuthorId() != null) {
					try {
						UserIbp user = userService.getUserIbp(gallery.getAuthorId().toString());
						gallery.setAuthorImage(user.getProfilePic());
						gallery.setAuthorName(user.getName());
					} catch (Exception e) {
						logger.error("Failed to fetch author details for ID: " + gallery.getAuthorId(), e);
					}
				}
				String key = gallery.getSliderId() + "|" + gallery.getDisplayOrder();
				grouped.computeIfAbsent(key, k -> new HashMap<>())
						.computeIfAbsent(gallery.getLanguageId(), k -> new ArrayList<>()).add(gallery);
			}
		} catch (Exception e) {
			logger.error("Error while grouping mini gallery sliders", e);
		}
		return grouped;
	}

	@Override
	public GroupHomePageData updateGroupHomePage(HttpServletRequest request, Long userGroupId,
			UserGroupHomePageEditData editData) {
		try {
			CommonProfile profile = AuthUtil.getProfileFromRequest(request);
			JSONArray roles = (JSONArray) profile.getAttribute("roles");
			Long userId = Long.parseLong(profile.getId());
			Boolean isFounder = ugMemberService.checkFounderRole(userId, userGroupId);
			if (roles.contains(roleAdmin) || Boolean.TRUE.equals(isFounder)) {

				List<UserGroup> userGroupTranslation = userGroupDao.findByGroupId(userGroupId);
				for (UserGroup userGroup : userGroupTranslation) {
					userGroup.setShowDesc(editData.getShowDesc());
					userGroup.setShowGallery(editData.getShowGallery());
					userGroup.setShowGridMap(editData.getShowGridMap());
					userGroup.setShowPartners(editData.getShowPartners());
					userGroup.setShowRecentObservations(editData.getShowRecentObservation());
					userGroup.setShowStats(editData.getShowStats());
					userGroup.setDescription(editData.getDescription());

					userGroupDao.update(userGroup);
				}

//		update gallery slider

				List<Map<Long, List<GroupGallerySlider>>> galleryData = editData.getGallerySlider();

				if (galleryData != null && !galleryData.isEmpty()) {
					for (Map<Long, List<GroupGallerySlider>> galleryMap : galleryData) {
						Long sliderId = null;

						for (Map.Entry<Long, List<GroupGallerySlider>> entry : galleryMap.entrySet()) {
							Long languageId = entry.getKey();
							List<GroupGallerySlider> sliderList = entry.getValue();

							if (sliderList == null || sliderList.isEmpty()) {
								continue; // Skip if no sliders for this language
							}

							GroupGallerySlider slider = sliderList.get(0); // Use the first entry
							slider.setLanguageId(languageId);

							if (sliderId != null) {
								slider.setSliderId(sliderId);
							}

							GroupGallerySlider savedSlider = groupGallerySliderDao.save(slider);

							if (sliderId == null) {
								sliderId = savedSlider.getId();
								savedSlider.setSliderId(sliderId);
								groupGallerySliderDao.update(savedSlider); // Update with its own sliderId
							}
						}
					}
				}

				for (Map<String, Map<Long, List<MiniGroupGallerySlider>>> miniGallerySlider : editData
						.getMiniGallerySlider()) {
					if (miniGallerySlider != null && !miniGallerySlider.isEmpty()) {
						miniGallerySlider.values()
								.forEach(languageMap -> saveMiniGroupGallerySliderTranslations(languageMap));
					}
				}

				return getGroupHomePageData(userGroupId, defaultLanguageId);
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return null;
	}

	private void saveMiniGroupGallerySliderTranslations(Map<Long, List<MiniGroupGallerySlider>> translations) {
		Long sliderId = null;
		for (Map.Entry<Long, List<MiniGroupGallerySlider>> entry : translations.entrySet()) {
			MiniGroupGallerySlider temp = entry.getValue().get(0);
			temp.setLanguageId(entry.getKey());

			if (sliderId != null) {
				temp.setSliderId(sliderId);
			}

			MiniGroupGallerySlider saved = miniGroupGallerySliderDao.save(temp);

			if (sliderId == null) {
				sliderId = saved.getId();
				saved.setSliderId(sliderId);
				miniGroupGallerySliderDao.update(saved);
			}
		}
	}

	@Override
	public GroupHomePageData removeHomePage(HttpServletRequest request, Long userGroupId, Long groupGalleryId) {
		try {
			CommonProfile profile = AuthUtil.getProfileFromRequest(request);
			JSONArray roles = (JSONArray) profile.getAttribute("roles");
			Long userId = Long.parseLong(profile.getId());
			Boolean isFounder = ugMemberService.checkFounderRole(userId, userGroupId);
			if (roles.contains(roleAdmin) || Boolean.TRUE.equals(isFounder)) {
				List<GroupGallerySlider> translations = groupGallerySliderDao.findBySliderId(groupGalleryId);
				for (GroupGallerySlider translation : translations) {
					groupGallerySliderDao.delete(translation);
				}
				return getGroupHomePageData(userGroupId, defaultLanguageId);
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}

		return null;
	}

	@Override
	public GroupHomePageData removeMiniHomePage(HttpServletRequest request, Long userGroupId, Long groupGalleryId) {
		try {
			CommonProfile profile = AuthUtil.getProfileFromRequest(request);
			JSONArray roles = (JSONArray) profile.getAttribute("roles");
			Long userId = Long.parseLong(profile.getId());
			Boolean isFounder = ugMemberService.checkFounderRole(userId, userGroupId);
			if (roles.contains(roleAdmin) || Boolean.TRUE.equals(isFounder)) {
				List<MiniGroupGallerySlider> translations = miniGroupGallerySliderDao
						.findBySliderIdByGroupId(userGroupId, groupGalleryId);
				for (MiniGroupGallerySlider translation : translations) {
					miniGroupGallerySliderDao.delete(translation);
				}
				return getGroupHomePageData(userGroupId, defaultLanguageId);
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}

		return null;
	}

	@Override
	public GroupHomePageData editHomePage(HttpServletRequest request, Long userGroupId, Long groupGalleryId,
			Map<Long, List<GroupGallerySlider>> editData) {
		try {
			CommonProfile profile = AuthUtil.getProfileFromRequest(request);
			JSONArray roles = (JSONArray) profile.getAttribute("roles");
			Long userId = Long.parseLong(profile.getId());
			Boolean isFounder = ugMemberService.checkFounderRole(userId, userGroupId);
			if (roles.contains(roleAdmin) || Boolean.TRUE.equals(isFounder)) {
				for (Map.Entry<Long, List<GroupGallerySlider>> entry : editData.entrySet()) {
					Long languageId = entry.getKey();
					List<GroupGallerySlider> sliderList = entry.getValue();

					if (sliderList == null || sliderList.isEmpty()) {
						continue; // Skip empty or null lists
					}

					GroupGallerySlider slider = sliderList.get(0); // Use the first slider object
					slider.setLanguageId(languageId);
					slider.setSliderId(groupGalleryId); // Set parent sliderId

					if (slider.getId() != null) {
						GroupGallerySlider existingSlider = groupGallerySliderDao.findById(slider.getId());

						if (existingSlider != null) {
							existingSlider.setUgId(slider.getUgId());
							existingSlider.setFileName(slider.getFileName());
							existingSlider.setTitle(slider.getTitle());
							existingSlider.setCustomDescripition(slider.getCustomDescripition());
							existingSlider.setMoreLinks(slider.getMoreLinks());
							existingSlider.setDisplayOrder(slider.getDisplayOrder());
							existingSlider.setReadMoreText(slider.getReadMoreText());
							existingSlider.setReadMoreUIType(slider.getReadMoreUIType());
							existingSlider.setGallerySidebar(slider.getGallerySidebar());
							existingSlider.setLanguageId(languageId);
							existingSlider.setSliderId(groupGalleryId);

							groupGallerySliderDao.update(existingSlider);
						}
					} else {
						groupGallerySliderDao.save(slider);
					}
				}

				return getGroupHomePageData(userGroupId, defaultLanguageId);
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}

		return null;
	}
	
	@Override
	public GroupHomePageData editMiniHomePage(HttpServletRequest request, Long userGroupId, Long groupGalleryId,
			Map<Long, List<MiniGroupGallerySlider>> editData) {
		try {
			CommonProfile profile = AuthUtil.getProfileFromRequest(request);
			JSONArray roles = (JSONArray) profile.getAttribute("roles");
			Long userId = Long.parseLong(profile.getId());
			Boolean isFounder = ugMemberService.checkFounderRole(userId, userGroupId);
			if (roles.contains(roleAdmin) || Boolean.TRUE.equals(isFounder)) {
				for (Map.Entry<Long, List<MiniGroupGallerySlider>> entry : editData.entrySet()) {
					Long languageId = entry.getKey();
					List<MiniGroupGallerySlider> sliderList = entry.getValue();

					if (sliderList == null || sliderList.isEmpty()) {
						continue; // Skip empty or null lists
					}

					MiniGroupGallerySlider slider = sliderList.get(0); // Use the first slider object
					slider.setLanguageId(languageId);
					slider.setSliderId(groupGalleryId); // Set parent sliderId

					if (slider.getId() != null) {
						MiniGroupGallerySlider existingSlider = miniGroupGallerySliderDao.findById(slider.getId());

						if (existingSlider != null) {
							existingSlider.setUgId(slider.getUgId());
							existingSlider.setFileName(slider.getFileName());
							existingSlider.setTitle(slider.getTitle());
							existingSlider.setCustomDescripition(slider.getCustomDescripition());
							existingSlider.setMoreLinks(slider.getMoreLinks());
							existingSlider.setDisplayOrder(slider.getDisplayOrder());
							existingSlider.setReadMoreText(slider.getReadMoreText());
							existingSlider.setReadMoreUIType(slider.getReadMoreUIType());
							existingSlider.setLanguageId(languageId);
							existingSlider.setSliderId(groupGalleryId);
							existingSlider.setColor(slider.getColor());
							existingSlider.setBgColor(slider.getBgColor());

							miniGroupGallerySliderDao.update(existingSlider);
						}
					} else {
						miniGroupGallerySliderDao.save(slider);
					}
				}

				return getGroupHomePageData(userGroupId, defaultLanguageId);
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}

		return null;
	}

	@Override
	public GroupHomePageData reorderingHomePageSlider(HttpServletRequest request, Long userGroupId,
			List<ReorderingHomePage> reorderingHomePage) {
		try {
			CommonProfile profile = AuthUtil.getProfileFromRequest(request);
			JSONArray roles = (JSONArray) profile.getAttribute("roles");
			Long userId = Long.parseLong(profile.getId());
			Boolean isFounder = ugMemberService.checkFounderRole(userId, userGroupId);
			if (roles.contains(roleAdmin) || Boolean.TRUE.equals(isFounder)) {
				for (ReorderingHomePage reOrder : reorderingHomePage) {
					List<GroupGallerySlider> gallery = groupGallerySliderDao.findBySliderId(reOrder.getGalleryId());
					for (GroupGallerySlider translation : gallery) {
						translation.setDisplayOrder(reOrder.getDisplayOrder());
						groupGallerySliderDao.update(translation);
					}
				}

				return getGroupHomePageData(userGroupId, defaultLanguageId);
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return null;
	}

	@Override
	public GroupHomePageData reorderMiniHomePageSlider(HttpServletRequest request, Long userGroupId,
			List<ReorderingHomePage> reorderingHomePage) {
		try {
			CommonProfile profile = AuthUtil.getProfileFromRequest(request);
			JSONArray roles = (JSONArray) profile.getAttribute("roles");
			Long userId = Long.parseLong(profile.getId());
			Boolean isFounder = ugMemberService.checkFounderRole(userId, userGroupId);
			if (roles.contains(roleAdmin) || Boolean.TRUE.equals(isFounder)) {
				for (ReorderingHomePage reOrder : reorderingHomePage) {
					List<MiniGroupGallerySlider> gallery = miniGroupGallerySliderDao
							.findBySliderIdByGroupId(userGroupId, reOrder.getGalleryId());
					for (MiniGroupGallerySlider translation : gallery) {
						translation.setDisplayOrder(reOrder.getDisplayOrder());
						miniGroupGallerySliderDao.update(translation);
					}
				}

				return getGroupHomePageData(userGroupId, defaultLanguageId);
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return null;
	}

	@Override
	public Boolean enableEdit(HttpServletRequest request, Long userGroupId) {

		CommonProfile profile = AuthUtil.getProfileFromRequest(request);
		Long userId = Long.parseLong(profile.getId());
		JSONArray roles = (JSONArray) profile.getAttribute("roles");
		Boolean isFounder = ugMemberService.checkFounderRole(userId, userGroupId);
		Boolean isModerator = ugMemberService.checkModeratorRole(userId, userGroupId);
		return (roles.contains(roleAdmin) || roles.contains(ROLE_PAGE_EDITOR) || Boolean.TRUE.equals(isFounder)
				|| Boolean.TRUE.equals(isModerator));
	}

	@Override
	public String createUgDescription(UserGroupIbp ugIbp) {
		UserGroupActivity ugActivity = new UserGroupActivity();
		String description = null;
		ugActivity.setFeatured(null);
		ugActivity.setUserGroupId(ugIbp.getId());
		ugActivity.setUserGroupName(ugIbp.getName());
		ugActivity.setWebAddress(ugIbp.getWebAddress());
		try {
			description = objectMapper.writeValueAsString(ugActivity);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return description;
	}

	public List<UserGroupIbp> fetchBySpeciesId(Long speciesId) {

		List<UserGroupSpecies> ugSpeciesList = ugSpeciesDao.findBySpeciesId(speciesId);

		List<UserGroupIbp> userGroup = new ArrayList<UserGroupIbp>();
		if (ugSpeciesList != null && !ugSpeciesList.isEmpty()) {
			for (UserGroupSpecies ugSpecies : ugSpeciesList) {
				userGroup.add(fetchByGroupIdIbp(ugSpecies.getUserGroupId()));
			}
		}

		return userGroup;

	}

	@Override
	public List<UserGroupIbp> createUGSpeciesMapping(HttpServletRequest request, Long speciesId,
			UserGroupSpeciesCreateData ugSpeciesCreateData) {
		CommonProfile profile = AuthUtil.getProfileFromRequest(request);
		Long userId = Long.parseLong(profile.getId());
		JSONArray roles = (JSONArray) profile.getAttribute("roles");
		for (Long userGroupId : ugSpeciesCreateData.getUserGroupIds()) {
			Boolean eligible = ugMemberService.checkUserGroupMember(userId, userGroupId);
			if (roles.contains(roleAdmin) || Boolean.TRUE.equals(eligible)) {
				UserGroupSpecies ugSpecies = new UserGroupSpecies(userGroupId, speciesId);
				ugSpecies = ugSpeciesDao.save(ugSpecies);
				if (ugSpecies != null) {
					UserGroupActivity ugActivity = new UserGroupActivity();
					UserGroupIbp ugIbp = fetchByGroupIdIbp(userGroupId);
					String description = null;
					ugActivity.setFeatured(null);
					ugActivity.setUserGroupId(ugIbp.getId());
					ugActivity.setUserGroupName(ugIbp.getName());
					ugActivity.setWebAddress(ugIbp.getWebAddress());
					try {
						description = objectMapper.writeValueAsString(ugActivity);
					} catch (Exception e) {
						logger.error(e.getMessage());
					}
					MailData mailData = null;
//					TODO mailData
//					if (userGroups.getMailData() != null) {
//						mailData = updateMailData(observationId, userGroups.getMailData());
//					}
					logActivity.logSpeciesActivities(request.getHeader(HttpHeaders.AUTHORIZATION), description,
							speciesId, speciesId, species, ugSpecies.getUserGroupId(), "Posted resource", mailData);

				}
			}
		}

		return fetchBySpeciesId(speciesId);
	}

	@Override
	public List<UserGroupIbp> updateUGSpeciesMapping(HttpServletRequest request, Long speciesId,
			UserGroupSpeciesCreateData ugSpeciesCreateData) {
		CommonProfile profile = AuthUtil.getProfileFromRequest(request);
		Long userId = Long.parseLong(profile.getId());

		List<UserGroupSpecies> ugSpeciesList = ugSpeciesDao.findBySpeciesId(speciesId);
		List<Long> userGroupIds = ugSpeciesCreateData.getUserGroupIds();
		List<Long> existingGroup = new ArrayList<Long>();

//		remove the existing groups
		for (UserGroupSpecies ugSpecies : ugSpeciesList) {
			if (!userGroupIds.contains(ugSpecies.getUserGroupId())) {
				Boolean eligible = ugMemberService.checkUserGroupMember(userId, ugSpecies.getUserGroupId());
				if (eligible) {
					ugSpeciesDao.delete(ugSpecies);
					UserGroupActivity ugActivity = new UserGroupActivity();
					UserGroupIbp ugIbp = fetchByGroupIdIbp(ugSpecies.getUserGroupId());
					String description = null;
					ugActivity.setFeatured(null);
					ugActivity.setUserGroupId(ugIbp.getId());
					ugActivity.setUserGroupName(ugIbp.getName());
					ugActivity.setWebAddress(ugIbp.getWebAddress());
					try {
						description = objectMapper.writeValueAsString(ugActivity);
					} catch (Exception e) {
						logger.error(e.getMessage());
					}

//					TODO mail Data
					MailData mailData = null;
//					MailData mailData = updateMailData(observationId, userGorups.getMailData());

					logActivity.logSpeciesActivities(request.getHeader(HttpHeaders.AUTHORIZATION), description,
							speciesId, speciesId, species, ugSpecies.getUserGroupId(), "Removed resoruce", mailData);

				}

			} else {
				existingGroup.add(ugSpecies.getUserGroupId());
			}

		}
//		add new groups
		for (Long ugId : userGroupIds) {
			if (!existingGroup.contains(ugId)) {
				Boolean eligible = ugMemberService.checkUserGroupMember(userId, ugId);
				if (eligible) {
					UserGroupSpecies ugSpecies = new UserGroupSpecies(ugId, speciesId);
					ugSpecies = ugSpeciesDao.save(ugSpecies);
					if (ugSpecies != null) {
						UserGroupActivity ugActivity = new UserGroupActivity();
						UserGroupIbp ugIbp = fetchByGroupIdIbp(ugId);
						String description = null;
						ugActivity.setFeatured(null);
						ugActivity.setUserGroupId(ugIbp.getId());
						ugActivity.setUserGroupName(ugIbp.getName());
						ugActivity.setWebAddress(ugIbp.getWebAddress());
						try {
							description = objectMapper.writeValueAsString(ugActivity);
						} catch (Exception e) {
							logger.error(e.getMessage());
						}
						MailData mailData = null;
//						TODO mailData
//						mailData = updateMailData(observationId, userGroups.getMailData());
						logActivity.logSpeciesActivities(request.getHeader(HttpHeaders.AUTHORIZATION), description,
								speciesId, speciesId, species, ugSpecies.getUserGroupId(), "Posted resource", mailData);
					}
				}
			}
		}

		return fetchBySpeciesId(speciesId);
	}

	@Override
	public UserGroupAdminList getUserGroupAdminListByUserId(HttpServletRequest request) {

		UserGroupAdminList result = new UserGroupAdminList();
		CommonProfile profile = AuthUtil.getProfileFromRequest(request);
		Long userId = Long.parseLong(profile.getId());
		JSONArray roles = (JSONArray) profile.getAttribute("roles");
		if (roles.contains(roleAdmin)) {
			result.setIsAdmin(true);
			result.setUgList(fetchAllUserGroup(defaultLanguageId));
			return result;
		}
		List<UserGroupMemberRole> ugMemberList = ugMemberDao.findUserGroupbyUserIdRole(userId);

		if (!ugMemberList.isEmpty()) {
			ArrayList<Long> arrayList = ugMemberList.stream().map(item -> item.getUserGroupId())
					.collect(Collectors.toCollection(ArrayList::new));

			List<UserGroupIbp> list = userGroupDao.findUgListByIds(arrayList).stream()
					.map(item -> getUserGroupIbp(item)).collect(Collectors.toCollection(ArrayList::new));

			result.setIsAdmin(true);
			result.setUgList(list);

			return result;
		}

		return null;
	}

	@Override
	public UserGroupObservation checkObservationUGMApping(Long observationId, Long userGroupId) {
		return ugObvDao.checkObservationUGMApping(observationId, userGroupId);
	}

	@Override
	public List<UserGroupIbp> createUserGroupObervation(HttpServletRequest request, Long ObvId, Long ugId) {

		CommonProfile profile = AuthUtil.getProfileFromRequest(request);
		JSONArray roles = (JSONArray) profile.getAttribute("roles");
		Long userId = Long.parseLong(profile.getId());
		Boolean eligible = ugMemberService.checkUserGroupMember(userId, ugId);
		if (roles.contains(roleAdmin) || Boolean.TRUE.equals(eligible)) {
			UserGroupObservation ugObv = new UserGroupObservation(ugId, ObvId);
			return fetchByObservationId(ObvId);
		}

		return null;
	}

	@Override
	public List<UserGroupIbp> removeUserGroupObervation(HttpServletRequest request, Long ObvId, Long ugId) {

		CommonProfile profile = AuthUtil.getProfileFromRequest(request);
		JSONArray roles = (JSONArray) profile.getAttribute("roles");
		Long userId = Long.parseLong(profile.getId());
		Boolean eligible = ugMemberService.checkUserGroupMember(userId, ugId);
		if (roles.contains(roleAdmin) || Boolean.TRUE.equals(eligible)) {
			UserGroupObservation ugObvMapping = ugObvDao.checkObservationUGMApping(ObvId, ugId);

			try {
				if (ugObvMapping != null) {
					ugObvDao.delete(ugObvMapping);
					produce.setMessage("observation", ObvId.toString(), messageType);
				}

			} catch (Exception e) {
				logger.error(e.getMessage());
			}
			return fetchByObservationId(ObvId);
		}

		return null;
	}

	@Override
	public List<UserGroupIbp> removeUserGroupObervationForDatatable(HttpServletRequest request, Long obvId, Long ugId) {

		CommonProfile profile = AuthUtil.getProfileFromRequest(request);
		JSONArray roles = (JSONArray) profile.getAttribute("roles");
		Long userId = Long.parseLong(profile.getId());
		Boolean eligible = ugMemberService.checkUserGroupMember(userId, ugId);
		if (roles.contains(roleAdmin) || Boolean.TRUE.equals(eligible)) {
			UserGroupObservation ugObvMapping = ugObvDao.checkObservationUGMApping(obvId, ugId);

			try {
				if (ugObvMapping != null) {
					ugObvDao.delete(ugObvMapping);
				}

			} catch (Exception e) {
				logger.error(e.getMessage());
			}
			return fetchByObservationId(obvId);
		}

		return null;
	}

	@Override
	public UserGroup updateObservationCustomisations(ObservationCustomisations updateCustomisationData) {
		try {
			Long ugId = updateCustomisationData.getUserGroupId();
			String mediaToggle = updateCustomisationData.getMediaToggle();
			List<UserGroup> translations = userGroupDao.findByGroupId(ugId);
			UserGroup userGroup = null;
			for (UserGroup translation : translations) {
				translation.setMediaToggle(mediaToggle);
				if (translation.getId().equals(translation.getGroupId())) {
					userGroup = userGroupDao.update(translation);
				} else {
					userGroupDao.update(translation);
				}
			}
			return userGroup;
		} catch (Exception e) {
			logger.error(e.getMessage());
		}

		return null;
	}

	@Override
	public List<SpeciesFieldValuesDTO> fetchSpeciesFieldsWithValuesByUgId(Long ugId) {
		List<SpeciesFieldValuesDTO> result = new ArrayList<>();
		try {
			result = ugSfMappingDao.findSpeciesFieldsWithValuesByUgId(ugId);
			return result;
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return result;
	}

	@Override
	public List<UsergroupSpeciesFieldMapping> updateSpeciesFieldsMappingByUgId(Long ugId, List<SField> speciesFields) {
		List<UsergroupSpeciesFieldMapping> result = new ArrayList<>();

		try {
			// Get existing mappings and their IDs
			List<UsergroupSpeciesFieldMapping> existingMappings = ugSfMappingDao.findSpeciesFieldsByUgId(ugId);
			Set<Long> existingSfIds = new HashSet<>();
			for (UsergroupSpeciesFieldMapping mapping : existingMappings) {
				existingSfIds.add(mapping.getSpeciesFieldId());
			}

			// Collect all new IDs (including path IDs)
			Set<Long> newSfIds = new HashSet<>();
			for (SField sField : speciesFields) {
				// Add main ID
				newSfIds.add(sField.getId());

				// Add path IDs if they exist
				if (sField.getPath() != null && !sField.getPath().isEmpty()) {
					String[] pathIds = sField.getPath().split("\\.");
					for (String pathId : pathIds) {
						try {
							newSfIds.add(Long.parseLong(pathId));
						} catch (NumberFormatException e) {
							logger.error("Invalid path id format: " + pathId);
						}
					}
				}
			}

			// Add new mappings (IDs in newSfIds but not in existingSfIds)
			for (Long sfId : newSfIds) {
				if (!existingSfIds.contains(sfId)) {
					UsergroupSpeciesFieldMapping newMapping = new UsergroupSpeciesFieldMapping();
					newMapping.setSpeciesFieldId(sfId);
					newMapping.setUsergroupId(ugId);
					ugSfMappingDao.addUserGroupSpeciesField(newMapping);
					result.add(newMapping);
				}
			}

			// Delete old mappings (IDs in existingSfIds but not in newSfIds)
			for (Long sfId : existingSfIds) {
				if (!newSfIds.contains(sfId)) {
					UsergroupSpeciesFieldMapping mappingToDelete = new UsergroupSpeciesFieldMapping();
					mappingToDelete.setSpeciesFieldId(sfId);
					mappingToDelete.setUsergroupId(ugId);
					ugSfMappingDao.deleteUserGroupSpeciesField(mappingToDelete);
				}
			}

			return result;

		} catch (Exception e) {
			logger.error(e.getMessage());
			return result;
		}
	}

	@Override
	public List<UserGroupSpeciesFieldMeta> updateSpeciesFieldMetadata(Long userGroupId,
			List<SpeciesFieldMetadata> metadata) {
		try {
			// Get existing metadata for this user group
			List<UserGroupSpeciesFieldMeta> existingMeta = ugSpeciesFieldMetaDao.findByUserGroupId(userGroupId);
			Set<Long> existingValueIds = new HashSet<>();
			for (UserGroupSpeciesFieldMeta meta : existingMeta) {
				existingValueIds.add(meta.getValueId());
			}

			List<UserGroupSpeciesFieldMeta> result = new ArrayList<>();
			Set<Long> newValueIds = new HashSet<>();

			// Process new metadata
			for (SpeciesFieldMetadata field : metadata) {
				newValueIds.add(field.getValueId());

				// If value doesn't exist, create new metadata
				if (!existingValueIds.contains(field.getValueId())) {
					UserGroupSpeciesFieldMeta newMeta = new UserGroupSpeciesFieldMeta();
					newMeta.setUserGroupId(userGroupId);
					newMeta.setValueType(field.getValueType());
					newMeta.setValueId(field.getValueId());

					ugSpeciesFieldMetaDao.save(newMeta);
					result.add(newMeta);
				}
			}

			// Delete metadata for values that are no longer included
			for (Long valueId : existingValueIds) {
				if (!newValueIds.contains(valueId)) {
					UserGroupSpeciesFieldMeta metaToDelete = ugSpeciesFieldMetaDao
							.findByUserGroupAndValueId(userGroupId, valueId);
					ugSpeciesFieldMetaDao.delete(metaToDelete);
				}
			}

			return result;
		} catch (Exception e) {
			logger.error(e.getMessage());
			return null;
		}
	}

	@Override
	public List<UserGroupSpeciesFieldMeta> getSpeciesFieldMetaData(Long userGroupId) {
		List<UserGroupSpeciesFieldMeta> speciesFieldMetaData = new ArrayList<>();
		try {
			speciesFieldMetaData = ugSpeciesFieldMetaDao.findByUserGroupId(userGroupId);
			return speciesFieldMetaData;
		} catch (Exception e) {
			logger.error(e.getMessage());
			return speciesFieldMetaData;
		}
	}

	@Override
	public Map<String, Map<Long, List<GroupGalleryConfig>>> createMiniGallery(HttpServletRequest request,
			Map<Long, List<GroupGalleryConfig>> miniGalleryData, Long ugId) {
		try {
			Map<String, Map<Long, List<GroupGalleryConfig>>> groupedByGalleryId = new HashMap<>();
			Long galleryId = null;
			for (Entry<Long, List<GroupGalleryConfig>> translation : miniGalleryData.entrySet()) {
				GroupGalleryConfig temp = translation.getValue().get(0);
				temp.setIsActive(true);
				temp.setId(null);
				temp.setUgId(ugId);

				// First save without galleryId to get the generated one
				if (galleryId == null) {
					temp.setGalleryId(null);
					temp = groupGalleryConfigDao.save(temp);
					galleryId = temp.getId();
					temp.setGalleryId(galleryId);
					temp = groupGalleryConfigDao.update(temp);
				} else {
					temp.setGalleryId(galleryId);
					temp = groupGalleryConfigDao.save(temp); // just one save now
				}

				groupedByGalleryId.computeIfAbsent(galleryId.toString(), k -> new HashMap<>())
						.computeIfAbsent(translation.getKey(), k -> new ArrayList<>()).add(temp);
			}
			return groupedByGalleryId;
		} catch (Exception e) {
			logger.error("Failed to create mini gallery: {}", e.getMessage(), e);
			return null;
		}
	}

	@Override
	public Map<String, Map<Long, List<GroupGalleryConfig>>> editMiniGallery(HttpServletRequest request, Long ugId,
			Long galleryId, Map<Long, List<GroupGalleryConfig>> miniGalleryData) {
		try {
			Map<String, Map<Long, List<GroupGalleryConfig>>> groupedByGalleryId = new HashMap<>();
			for (Entry<Long, List<GroupGalleryConfig>> translation : miniGalleryData.entrySet()) {
				GroupGalleryConfig temp = translation.getValue().get(0);
				if (temp.getId() != null) {
					GroupGalleryConfig miniGallery = groupGalleryConfigDao.findById(temp.getId());
					miniGallery.setTitle(temp.getTitle());
					miniGallery.setSlidesPerView(temp.getSlidesPerView());
					miniGallery.setIsVertical(temp.getIsVertical());
					miniGallery.setIsActive(temp.getIsActive());
					temp = groupGalleryConfigDao.update(miniGallery);
				} else {
					temp = groupGalleryConfigDao.save(temp);
				}
				groupedByGalleryId.computeIfAbsent(galleryId.toString(), k -> new HashMap<>())
						.computeIfAbsent(translation.getKey(), k -> new ArrayList<>()).add(temp);
			}

			return groupedByGalleryId;
		} catch (Exception e) {
			logger.error("Failed to edit mini gallery with ID {}: {}", galleryId, e.getMessage(), e);
			return null;
		}
	}

	@Override
	public Boolean removeMiniGallery(HttpServletRequest request, Long ugId, Long galleryId) {
		try {
			List<GroupGalleryConfig> miniGallery = groupGalleryConfigDao.getByGalleryId(ugId, galleryId);
			if (miniGallery == null) {
				logger.warn("Mini gallery with ID {} not found for deletion.", galleryId);
				return false;
			}
			for (GroupGalleryConfig translation : miniGallery) {
				groupGalleryConfigDao.delete(translation);
			}
			List<MiniGroupGallerySlider> miniGallerySlides = miniGroupGallerySliderDao
					.getAllGallerySliderInfoByGroupId(ugId, galleryId);
			for (MiniGroupGallerySlider slide : miniGallerySlides) {
				miniGroupGallerySliderDao.delete(slide);
			}
			return true;
		} catch (Exception e) {
			logger.error("Error while deleting mini gallery with ID {}: {}", galleryId, e.getMessage(), e);
			return false;
		}
	}

}
