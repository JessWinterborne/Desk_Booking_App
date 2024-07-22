package com.example.MyApp.controller;

import com.example.MyApp.dto.InsertDesk;
import com.example.MyApp.dto.InsertUser;
import com.example.MyApp.dto.InsertBooking;
import com.example.MyApp.service.QueryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.sql.Date;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(ApiController.class)
public class ApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private QueryService queryService;

    @Test
    public void testCreateDesk_Success() throws Exception {
        InsertDesk insertDesk = new InsertDesk();
        insertDesk.setLocation("Floor 2, Desk 9");

        when(queryService.executeCreateDeskQuery(anyString())).thenReturn("Desk created successfully");

        mockMvc.perform(post("/api/createDesk")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(insertDesk))
                .header("Authorization", "Bearer token"))
                .andExpect(status().isOk())
                .andExpect(content().string("Desk created successfully"));

        verify(queryService, Mockito.times(1)).executeCreateDeskQuery("Floor 2, Desk 9");
    }

    @Test
    public void testCreateUser_Success() throws Exception {
        InsertUser insertUser = new InsertUser();
        insertUser.setUserEmail("test@example.com");

        when(queryService.executeCreateUserQuery(anyString())).thenReturn("User created successfully");

        mockMvc.perform(post("/api/createUser")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(insertUser))
                .header("Authorization", "Bearer token"))
                .andExpect(status().isOk())
                .andExpect(content().string("User created successfully"));

        verify(queryService, Mockito.times(1)).executeCreateUserQuery("test@example.com");
    }

    @Test
    public void testCreateBooking_UserNotFound() throws Exception {
        InsertBooking insertBooking = new InsertBooking();
        insertBooking.setUserEmail("test@example.com");
        insertBooking.setLocation("Floor 2, Desk 9");
        insertBooking.setBookingDate("2023-06-20");

        when(queryService.executeCheckUserExistsQuery(anyString())).thenReturn(false);

        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", "User not found");

        mockMvc.perform(post("/api/createBooking")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(insertBooking))
                .header("Authorization", "Bearer token"))
                .andExpect(status().isOk())
                .andExpect(content().json(new ObjectMapper().writeValueAsString(errorResponse)));

        verify(queryService, Mockito.times(1)).executeCheckUserExistsQuery("test@example.com");
        verify(queryService, Mockito.times(0)).executeCheckDeskExistsQuery(anyString());
    }

    @Test
    public void testGetDeskBookings_DeskNotFound() throws Exception {
        InsertDesk insertDesk = new InsertDesk();
        insertDesk.setLocation("Floor 2, Desk 9");

        when(queryService.executeCheckDeskExistsQuery(anyString())).thenReturn(false);

        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", "Desk not found");

        mockMvc.perform(post("/api/getDeskBookings")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(insertDesk))
                .header("Authorization", "Bearer token"))
                .andExpect(status().isOk())
                .andExpect(content().json(new ObjectMapper().writeValueAsString(errorResponse)));

        verify(queryService, Mockito.times(1)).executeCheckDeskExistsQuery("Floor 2, Desk 9");
        verify(queryService, Mockito.times(0)).executeGetDeskBookingsQuery(anyString());
    }

    @Test
    public void testGetUserBookings_UserNotFound() throws Exception {
        InsertUser insertUser = new InsertUser();
        insertUser.setUserEmail("test@example.com");

        when(queryService.executeCheckUserExistsQuery(anyString())).thenReturn(false);

        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", "User not found");

        mockMvc.perform(post("/api/getUserBookings")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(insertUser))
                .header("Authorization", "Bearer token"))
                .andExpect(status().isOk())
                .andExpect(content().json(new ObjectMapper().writeValueAsString(errorResponse)));

        verify(queryService, Mockito.times(1)).executeCheckUserExistsQuery("test@example.com");
        verify(queryService, Mockito.times(0)).executeGetUserBookingsQuery(anyString());
    }

    @Test
    public void testCheckUserExists_UserExists() throws Exception {
        InsertUser insertUser = new InsertUser();
        insertUser.setUserEmail("test@example.com");

        when(queryService.executeCheckUserExistsQuery(anyString())).thenReturn(true);

        mockMvc.perform(post("/api/checkUserExists")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(insertUser))
                .header("Authorization", "Bearer token"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));

        verify(queryService, Mockito.times(1)).executeCheckUserExistsQuery("test@example.com");
    }

    @Test
    public void testCheckDeskExists_DeskExists() throws Exception {
        InsertDesk insertDesk = new InsertDesk();
        insertDesk.setLocation("Floor 2, Desk 9");

        when(queryService.executeCheckDeskExistsQuery(anyString())).thenReturn(true);

        mockMvc.perform(post("/api/checkDeskExists")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(insertDesk))
                .header("Authorization", "Bearer token"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));

        verify(queryService, Mockito.times(1)).executeCheckDeskExistsQuery("Floor 2, Desk 9");
    }
}
