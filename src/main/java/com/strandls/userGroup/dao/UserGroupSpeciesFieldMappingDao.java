package com.strandls.userGroup.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.NativeQuery;
import org.hibernate.query.Query;
import org.hibernate.type.LongType;
import org.hibernate.type.StringType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.strandls.userGroup.pojo.SpeciesFieldValuesDTO;
import com.strandls.userGroup.pojo.UsergroupSpeciesFieldMapping;
import com.strandls.userGroup.util.AbstractDAO;

public class UserGroupSpeciesFieldMappingDao extends AbstractDAO<UsergroupSpeciesFieldMapping, Long> {

	private final Logger logger = LoggerFactory.getLogger(UserGroupSpeciesFieldMappingDao.class);

	@Inject
	protected UserGroupSpeciesFieldMappingDao(SessionFactory sessionFactory) {
		super(sessionFactory);
	}

	@SuppressWarnings("unchecked")
	public List<UsergroupSpeciesFieldMapping> findSpeciesFieldsByUgId(Long ugId) {
		List<UsergroupSpeciesFieldMapping> result = new ArrayList<UsergroupSpeciesFieldMapping>();
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
	        String query = "SELECT " +
	            "usergroup_id, " +
	            "species_field_id, " +
	            "CASE " +
	                "WHEN MAX(value_type) IS NULL THEN json_build_object('values', NULL) " +
	                "ELSE ( " +
	                    "SELECT json_build_object( " +
	                        "'values', " +
	                        "json_object_agg(value_type, value_array) " +
	                    ") " +
	                    "FROM ( " +
	                        "SELECT " +
	                            "value_type, " +
	                            "array_agg(value_id) as value_array " +
	                        "FROM user_group_species_fields t2 " +
	                        "WHERE t2.usergroup_id = t1.usergroup_id " +
	                        "AND t2.species_field_id = t1.species_field_id " +
	                        "AND t2.value_type IS NOT NULL " +
	                        "GROUP BY value_type " +
	                    ") subq " +
	                ") " +
	            "END as values " +
	            "FROM user_group_species_fields t1 " +
	            "WHERE usergroup_id = :ugId " +
	            "GROUP BY usergroup_id, species_field_id " +
	            "ORDER BY species_field_id";

	        NativeQuery<?> nativeQuery = session.createNativeQuery(query)
	            .addScalar("usergroup_id", LongType.INSTANCE)
	            .addScalar("species_field_id", LongType.INSTANCE)
	            .addScalar("values", StringType.INSTANCE);  // Handle JSON as string

	        nativeQuery.setParameter("ugId", ugId);

	        List<Object[]> rows = (List<Object[]>) nativeQuery.getResultList();
	        ObjectMapper mapper = new ObjectMapper();

	        for (Object[] row : rows) {
	            Long userGroupId = (Long) row[0];
	            Long speciesFieldId = (Long) row[1];
	            String jsonStr = (String) row[2];  // Now we get it as string

	            // Parse the JSON string
	            JsonNode jsonNode = mapper.readTree(jsonStr);
	            JsonNode valuesNode = jsonNode.get("values");
	            Map<String, List<Long>> values;
	            
	            if (valuesNode.isNull()) {
	                values = null;
	            } else {
	                values = mapper.convertValue(valuesNode, new TypeReference<Map<String, List<Long>>>() {});
	            }

	            result.add(new SpeciesFieldValuesDTO(userGroupId, speciesFieldId, values));
	        }
	    } catch (Exception e) {
	        logger.error("Error fetching species field values: ", e);
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
		// TODO Auto-generated method stub
		return null;
	}

}
