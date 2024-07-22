package com.example.MyApp.service;

import com.example.MyApp.util.DatabaseUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

import java.sql.Date;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class QueryServiceTest {

    @Mock
    private DatabaseUtil databaseUtil;

    @InjectMocks
    private QueryService queryService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testExecuteCreateDeskQuery() {
        String location = "Floor 2, Desk 9";
        String successMessage = "Successfully created desk";
        
        when(databaseUtil.executeUpdate(anyString(), any(MapSqlParameterSource.class), anyString()))
                .thenReturn(successMessage);

        String result = queryService.executeCreateDeskQuery(location);

        assertEquals(successMessage, result);
        ArgumentCaptor<String> sqlCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<MapSqlParameterSource> paramsCaptor = ArgumentCaptor.forClass(MapSqlParameterSource.class);
        ArgumentCaptor<String> successMessageCaptor = ArgumentCaptor.forClass(String.class);
        verify(databaseUtil, times(1)).executeUpdate(sqlCaptor.capture(), paramsCaptor.capture(), successMessageCaptor.capture());

        assertEquals("INSERT INTO desks (location) VALUES (:location)", sqlCaptor.getValue());
        assertEquals(location, paramsCaptor.getValue().getValue("location"));
        assertEquals(successMessage, successMessageCaptor.getValue());
    }

    @Test
    public void testExecuteCreateUserQuery() {
        String email = "test@example.com";
        String successMessage = "Successfully created user";

        when(databaseUtil.executeUpdate(anyString(), any(MapSqlParameterSource.class), anyString()))
                .thenReturn(successMessage);

        String result = queryService.executeCreateUserQuery(email);

        assertEquals(successMessage, result);
        ArgumentCaptor<String> sqlCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<MapSqlParameterSource> paramsCaptor = ArgumentCaptor.forClass(MapSqlParameterSource.class);
        ArgumentCaptor<String> successMessageCaptor = ArgumentCaptor.forClass(String.class);
        verify(databaseUtil, times(1)).executeUpdate(sqlCaptor.capture(), paramsCaptor.capture(), successMessageCaptor.capture());

        assertEquals("INSERT INTO users (email) VALUES (:email)", sqlCaptor.getValue());
        assertEquals(email, paramsCaptor.getValue().getValue("email"));
        assertEquals(successMessage, successMessageCaptor.getValue());
    }

    @Test
    public void testExecuteCreateBookingQuery() {
        String email = "test@example.com";
        String location = "Floor 2, Desk 9";
        Date bookingDate = Date.valueOf("2023-06-20");
        String successMessage = "Successfully created booking";

        when(databaseUtil.executeUpdate(anyString(), any(MapSqlParameterSource.class), anyString()))
                .thenReturn(successMessage);

        String result = queryService.executeCreateBookingQuery(email, location, bookingDate);

        assertEquals(successMessage, result);
        ArgumentCaptor<String> sqlCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<MapSqlParameterSource> paramsCaptor = ArgumentCaptor.forClass(MapSqlParameterSource.class);
        ArgumentCaptor<String> successMessageCaptor = ArgumentCaptor.forClass(String.class);
        verify(databaseUtil, times(1)).executeUpdate(sqlCaptor.capture(), paramsCaptor.capture(), successMessageCaptor.capture());

        assertEquals("INSERT INTO bookings (user_id, desk_id, booking_date) " +
                "VALUES ((SELECT user_id FROM users WHERE email = :email), " +
                "(SELECT desk_id FROM desks WHERE location = :location), :bookingDate)", sqlCaptor.getValue());
        assertEquals(email, paramsCaptor.getValue().getValue("email"));
        assertEquals(location, paramsCaptor.getValue().getValue("location"));
        assertEquals(bookingDate, paramsCaptor.getValue().getValue("bookingDate"));
        assertEquals(successMessage, successMessageCaptor.getValue());
    }

    @Test
    public void testExecuteGetDeskBookingsQuery() {
        String location = "Floor 2, Desk 9";
        List<Map<String, Object>> mockResult = Collections.singletonList(Collections.singletonMap("booking_date", "2023-06-20"));

        when(databaseUtil.executeQuery(anyString(), any(MapSqlParameterSource.class))).thenReturn(mockResult);

        List<Map<String, Object>> result = queryService.executeGetDeskBookingsQuery(location);

        assertEquals(mockResult, result);
        ArgumentCaptor<String> sqlCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<MapSqlParameterSource> paramsCaptor = ArgumentCaptor.forClass(MapSqlParameterSource.class);
        verify(databaseUtil, times(1)).executeQuery(sqlCaptor.capture(), paramsCaptor.capture());

        assertEquals("SELECT d.location, b.booking_date, u.email " +
                "FROM bookings b " +
                "JOIN desks d ON b.desk_id = d.desk_id " +
                "JOIN users u ON b.user_id = u.user_id " +
                "WHERE d.location = :location AND b.booking_date >= DATE('now') " +
                "ORDER BY b.booking_date", sqlCaptor.getValue());
        assertEquals(location, paramsCaptor.getValue().getValue("location"));
    }

    @Test
    public void testExecuteGetUserBookingsQuery() {
        String email = "test@example.com";
        List<Map<String, Object>> mockResult = Collections.singletonList(Collections.singletonMap("booking_date", "2023-06-20"));

        when(databaseUtil.executeQuery(anyString(), any(MapSqlParameterSource.class))).thenReturn(mockResult);

        List<Map<String, Object>> result = queryService.executeGetUserBookingsQuery(email);

        assertEquals(mockResult, result);
        ArgumentCaptor<String> sqlCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<MapSqlParameterSource> paramsCaptor = ArgumentCaptor.forClass(MapSqlParameterSource.class);
        verify(databaseUtil, times(1)).executeQuery(sqlCaptor.capture(), paramsCaptor.capture());

        assertEquals("SELECT d.location, b.booking_date " +
                "FROM bookings b " +
                "JOIN desks d ON b.desk_id = d.desk_id " +
                "JOIN users u ON b.user_id = u.user_id " +
                "WHERE u.email = :email AND b.booking_date >= DATE('now') " +
                "ORDER BY b.booking_date", sqlCaptor.getValue());
        assertEquals(email, paramsCaptor.getValue().getValue("email"));
    }

    @Test
    public void testExecuteCheckUserExistsQuery_UserExists() {
        String email = "test@example.com";
        List<Map<String, Object>> mockResult = Collections.singletonList(Collections.singletonMap("user_id", 1));

        when(databaseUtil.executeQuery(anyString(), any(MapSqlParameterSource.class))).thenReturn(mockResult);

        boolean result = queryService.executeCheckUserExistsQuery(email);

        assertTrue(result);
        ArgumentCaptor<String> sqlCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<MapSqlParameterSource> paramsCaptor = ArgumentCaptor.forClass(MapSqlParameterSource.class);
        verify(databaseUtil, times(1)).executeQuery(sqlCaptor.capture(), paramsCaptor.capture());

        assertEquals("SELECT 1 FROM users WHERE email = :email", sqlCaptor.getValue());
        assertEquals(email, paramsCaptor.getValue().getValue("email"));
    }

    @Test
    public void testExecuteCheckUserExistsQuery_UserDoesNotExist() {
        String email = "test@example.com";
        List<Map<String, Object>> mockResult = Collections.emptyList();

        when(databaseUtil.executeQuery(anyString(), any(MapSqlParameterSource.class))).thenReturn(mockResult);

        boolean result = queryService.executeCheckUserExistsQuery(email);

        assertFalse(result);
        ArgumentCaptor<String> sqlCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<MapSqlParameterSource> paramsCaptor = ArgumentCaptor.forClass(MapSqlParameterSource.class);
        verify(databaseUtil, times(1)).executeQuery(sqlCaptor.capture(), paramsCaptor.capture());

        assertEquals("SELECT 1 FROM users WHERE email = :email", sqlCaptor.getValue());
        assertEquals(email, paramsCaptor.getValue().getValue("email"));
    }

    @Test
    public void testExecuteCheckDeskExistsQuery_DeskExists() {
        String location = "Floor 2, Desk 9";
        List<Map<String, Object>> mockResult = Collections.singletonList(Collections.singletonMap("desk_id", 1));

        when(databaseUtil.executeQuery(anyString(), any(MapSqlParameterSource.class))).thenReturn(mockResult);

        boolean result = queryService.executeCheckDeskExistsQuery(location);

        assertTrue(result);
        ArgumentCaptor<String> sqlCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<MapSqlParameterSource> paramsCaptor = ArgumentCaptor.forClass(MapSqlParameterSource.class);
        verify(databaseUtil, times(1)).executeQuery(sqlCaptor.capture(), paramsCaptor.capture());

        assertEquals("SELECT 1 FROM desks WHERE location = :location", sqlCaptor.getValue());
        assertEquals(location, paramsCaptor.getValue().getValue("location"));
    }

    @Test
    public void testExecuteCheckDeskExistsQuery_DeskDoesNotExist() {
        String location = "Floor 2, Desk 9";
        List<Map<String, Object>> mockResult = Collections.emptyList();

        when(databaseUtil.executeQuery(anyString(), any(MapSqlParameterSource.class))).thenReturn(mockResult);

        boolean result = queryService.executeCheckDeskExistsQuery(location);

        assertFalse(result);
        ArgumentCaptor<String> sqlCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<MapSqlParameterSource> paramsCaptor = ArgumentCaptor.forClass(MapSqlParameterSource.class);
        verify(databaseUtil, times(1)).executeQuery(sqlCaptor.capture(), paramsCaptor.capture());

        assertEquals("SELECT 1 FROM desks WHERE location = :location", sqlCaptor.getValue());
        assertEquals(location, paramsCaptor.getValue().getValue("location"));
    }
}
