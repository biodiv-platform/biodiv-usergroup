package com.strandls.userGroup.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.NativeQuery;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.strandls.userGroup.pojo.SpeciesFieldValuesDTO;
import com.strandls.userGroup.pojo.UsergroupSpeciesFieldMapping;
import com.strandls.userGroup.util.AbstractDAO;

import jakarta.inject.Inject;

public class UserGroupSpeciesFieldMappingDao extends AbstractDAO<UsergroupSpeciesFieldMapping, Long> {

	private final Logger logger = LoggerFactory.getLogger(UserGroupSpeciesFieldMappingDao.class);

	@Inject
	protected UserGroupSpeciesFieldMappingDao(SessionFactory sessionFactory) {
		super(sessionFactory);
	}

	@SuppressWarnings("unchecked")
	public List<UsergroupSpeciesFieldMapping> findSpeciesFieldsByUgId(Long ugId) {
		List<UsergroupSpeciesFieldMapping> result = new ArrayList<>();
		String qry = "from UsergroupSpeciesFieldMapping where usergroupId = :ugId";
		Session session = sessionFactory.openSession();
		try {
			Query<UsergroupSpeciesFieldMapping> query = session.createQuery(qry);
			query.setParameter("ugId", ugId);
			return query.getResultList();
		} catch (Exception e) {
			logger.error(e.getMessage());
		} finally {
			session.close();
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	public List<SpeciesFieldValuesDTO> findSpeciesFieldsWithValuesByUgId(Long ugId) {
		List<SpeciesFieldValuesDTO> result = new ArrayList<>();
		Session session = sessionFactory.openSession();
		try {
			String query = "SELECT " + "t1.usergroup_id, " + "t1.species_field_id, " + "CASE "
					+ "WHEN MAX(meta.value_type) IS NULL THEN json_build_object('values', NULL) " + "ELSE ( "
					+ "SELECT json_build_object( " + "'values', " + "json_object_agg(value_type, value_array) " + ") "
					+ "FROM ( " + "SELECT " + "value_type, " + "array_agg(DISTINCT value_id) as value_array "
					+ "FROM user_group_species_field_meta meta2 " + "WHERE meta2.usergroup_id = t1.usergroup_id "
					+ "AND meta2.value_type IS NOT NULL " + "GROUP BY value_type " + ") subq " + ") " + "END as values "
					+ "FROM user_group_species_fields t1 "
					+ "LEFT JOIN user_group_species_field_meta meta ON t1.usergroup_id = meta.usergroup_id "
					+ "WHERE t1.usergroup_id = :ugId " + "GROUP BY t1.usergroup_id, t1.species_field_id "
					+ "ORDER BY t1.species_field_id";

			NativeQuery<?> nativeQuery = session.createNativeQuery(query).addScalar("usergroup_id", Long.class)
					.addScalar("species_field_id", Long.class).addScalar("values", String.class);

			nativeQuery.setParameter("ugId", ugId);

			List<Object[]> rows = (List<Object[]>) nativeQuery.getResultList();
			ObjectMapper mapper = new ObjectMapper();

			for (Object[] row : rows) {
				Long userGroupId = (Long) row[0];
				Long speciesFieldId = (Long) row[1];
				String jsonStr = (String) row[2];

				JsonNode jsonNode = mapper.readTree(jsonStr);
				JsonNode valuesNode = jsonNode.get("values");
				Map<String, List<Long>> values;

				if (valuesNode.isNull()) {
					values = null;
				} else {
					values = mapper.convertValue(valuesNode, new TypeReference<Map<String, List<Long>>>() {
					});
				}

				result.add(new SpeciesFieldValuesDTO(userGroupId, speciesFieldId, values));
			}
		} catch (Exception e) {
			logger.error("Error fetching usergroup values: ", e);
		} finally {
			session.close();
		}
		return result;
	}

	public UsergroupSpeciesFieldMapping addUserGroupSpeciesField(UsergroupSpeciesFieldMapping mapping) {
		Session session = sessionFactory.openSession();
		Transaction tx = null;
		try {
			tx = session.beginTransaction();
			session.save(mapping);
			tx.commit();
			return mapping;
		} catch (Exception e) {
			if (tx != null && tx.isActive()) {
				tx.rollback();
			}
			logger.error(e.getMessage());
			return null;
		} finally {
			session.close();
		}
	}

	public boolean deleteUserGroupSpeciesField(UsergroupSpeciesFieldMapping mapping) {
		Session session = sessionFactory.openSession();
		Transaction tx = null;

		try {
			tx = session.beginTransaction();

			// Create query with composite key conditions
			String hql = "DELETE FROM UsergroupSpeciesFieldMapping WHERE usergroupId = :ugId AND speciesFieldId = :sfId";
			Query<?> query = session.createQuery(hql);
			query.setParameter("ugId", mapping.getUsergroupId());
			query.setParameter("sfId", mapping.getSpeciesFieldId());
			query.executeUpdate();

			tx.commit();
			return true;

		} catch (Exception e) {
			if (tx != null && tx.isActive()) {
				tx.rollback();
			}
			logger.error(e.getMessage());
			return false;

		} finally {
			session.close();
		}
	}

	@Override
	public UsergroupSpeciesFieldMapping findById(Long id) {
		return null;
	}
}
