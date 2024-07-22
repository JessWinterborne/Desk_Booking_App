package com.example.MyApp.service;

import com.example.MyApp.util.DatabaseUtil;

import java.util.List;
import java.util.Map;
import java.sql.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.stereotype.Service;

@Service
public class QueryService {

    private final DatabaseUtil databaseUtil;

    @Autowired
    public QueryService(DatabaseUtil databaseUtil) {
        this.databaseUtil = databaseUtil;
    }

    public String executeCreateDeskQuery(String deskLocation) {
        String sql = "INSERT INTO desks (location) VALUES (:location)";
        MapSqlParameterSource params = new MapSqlParameterSource().addValue("location", deskLocation);
        String successMessage = "Successfully created desk";
        return databaseUtil.executeUpdate(sql, params, successMessage);
    }


    public String executeCreateUserQuery(String userEmail) {
        System.out.println(userEmail);
        String sql = "INSERT INTO users (email) VALUES (:email)";
        MapSqlParameterSource params = new MapSqlParameterSource().addValue("email", userEmail);
        String successMessage = "Successfully created user";
        return databaseUtil.executeUpdate(sql, params, successMessage);
    }

    public String executeCreateBookingQuery(String userEmail, String deskLocation, Date bookingDate) {
        String sql = "INSERT INTO bookings (user_id, desk_id, booking_date) " +
        "VALUES ((SELECT user_id FROM users WHERE email = :email), " +
        "(SELECT desk_id FROM desks WHERE location = :location), :bookingDate)";

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("email", userEmail);
        params.addValue("location", deskLocation);
        params.addValue("bookingDate", bookingDate);

        String successMessage = "Successfully created booking";
        return databaseUtil.executeUpdate(sql, params, successMessage);
    }

    public List<Map<String, Object>> executeGetDeskBookingsQuery(String deskLocation) {
        System.out.println(deskLocation);
        // String sql = "INSERT INTO users (email) VALUES (:email)";
        String sql = "SELECT d.location, b.booking_date, u.email " +
                     "FROM bookings b " +
                     "JOIN desks d ON b.desk_id = d.desk_id " +
                     "JOIN users u ON b.user_id = u.user_id " +
                     "WHERE d.location = :location AND b.booking_date >= DATE('now') " +
                     "ORDER BY b.booking_date";
                     MapSqlParameterSource params = new MapSqlParameterSource().addValue("location", deskLocation);
        return databaseUtil.executeQuery(sql, params);
    }

    public List<Map<String, Object>> executeGetUserBookingsQuery(String userEmail) {
        // String sql = "INSERT INTO users (email) VALUES (:email)";
        String sql = "SELECT d.location, b.booking_date " +
                     "FROM bookings b " +
                     "JOIN desks d ON b.desk_id = d.desk_id " +
                     "JOIN users u ON b.user_id = u.user_id " +
                     "WHERE u.email = :email AND b.booking_date >= DATE('now') " +
                     "ORDER BY b.booking_date";

        MapSqlParameterSource params = new MapSqlParameterSource().addValue("email", userEmail);
        return databaseUtil.executeQuery(sql, params);
    }

    public  boolean executeCheckUserExistsQuery(String userEmail) {
        String sql = "SELECT 1 FROM users WHERE email = :email";
        MapSqlParameterSource params = new MapSqlParameterSource().addValue("email", userEmail);
        List<Map<String, Object>> results = databaseUtil.executeQuery(sql, params);
        // Check if the results list is not empty
        return !results.isEmpty();
    }

    public  boolean executeCheckDeskExistsQuery(String location) {
        String sql = "SELECT 1 FROM desks WHERE location = :location";
        MapSqlParameterSource params = new MapSqlParameterSource().addValue("location", location);
        List<Map<String, Object>> results = databaseUtil.executeQuery(sql, params);
        // Check if the results list is not empty
        return !results.isEmpty();
    }

}




