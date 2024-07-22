package com.example.MyApp.util;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.postgresql.util.PSQLException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class DatabaseUtil {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public DatabaseUtil(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    // This is for .update() - i.e. SQL queries that don't return results (like INSERTs)
    public String executeUpdate(String sql, MapSqlParameterSource params, String successMessage) {
        try {
            namedParameterJdbcTemplate.update(sql, params);
            return successMessage;
        } catch (DuplicateKeyException e) {
            if (e.getCause() instanceof PSQLException) {
                PSQLException psqlException = (PSQLException) e.getCause();
                if (psqlException.getSQLState().equals("23505")) { // PostgreSQL error code for unique violation
                    System.out.println("Error: Duplicate key violation.");
                    return "Error: Duplicate key violation";
                }
            }
        } catch (DataAccessException e) {
            System.out.println("An error occurred while executing the update: " + e.getMessage());
            return "An error occurred while executing the update";
        }
        return "0";
    }

    // This is for SQL queries that return values like SELECTs
    public List<Map<String, Object>> executeQuery(String sql, MapSqlParameterSource params) {
        try {
            List<Map<String, Object>> results = namedParameterJdbcTemplate.queryForList(sql, params);
            System.out.println(results);
            return results;
        } catch (DuplicateKeyException e) {
            if (e.getCause() instanceof PSQLException) {
                PSQLException psqlException = (PSQLException) e.getCause();
                if (psqlException.getSQLState().equals("23505")) { // PostgreSQL error code for unique violation
                    System.out.println("Error: Duplicate key violation.");
                    throw new RuntimeException("Error: Duplicate key violation", e);
                }
            }
            throw new RuntimeException("Error: Duplicate key violation", e);
        } catch (DataAccessException e) {
            System.out.println("An error occurred while executing the query: " + e.getMessage());
            throw new RuntimeException("An error occurred while executing the query", e);
        }
        // return "0";
    }
}
