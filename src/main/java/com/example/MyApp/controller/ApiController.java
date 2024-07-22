package com.example.MyApp.controller;

import com.example.MyApp.service.QueryService;

// Data transfer objects
import com.example.MyApp.dto.InsertDesk;
import com.example.MyApp.dto.InsertUser;
import com.example.MyApp.dto.InsertBooking;

// Spring boot
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
// Java types
import java.util.List;
import java.util.Map;
import java.sql.Date;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/api")
public class ApiController {

    @Autowired
    private QueryService queryService;

    @PostMapping("/createDesk")
    public String createDesk(
            @RequestHeader Map<String, String> headers,
            @RequestBody InsertDesk body
    ) {
        // Log or process headers, body, and path parameter
        // headers.forEach((key, value) -> System.out.println(key + " : " + value));
        // System.out.println("Body: " + body);
        // String deskLocation = "Floor 2, Desk 9";
        String deskLocation = body.getLocation();
        return queryService.executeCreateDeskQuery(deskLocation);
    }

    @PostMapping("/createUser")
    public String createUser(
            @RequestHeader Map<String, String> headers,
            @RequestBody InsertUser body
    ) {
        String userEmail = body.getUserEmail();
        return queryService.executeCreateUserQuery(userEmail);
    }

    @PostMapping("/createBooking")
    public ResponseEntity<?> createBooking(
            @RequestHeader Map<String, String> headers,
            @RequestBody InsertBooking body
    ) {
        String userEmail = body.getUserEmail();
        String deskLocation = body.getLocation();
        Date bookingDate = body.getBookingDateAsSqlDate();

        // Check if the user exists
        boolean userExists = queryService.executeCheckUserExistsQuery(userEmail);
        if (!userExists) {
            // Return a JSON response with an error message if the desk does not exist
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "User not found");
            return new ResponseEntity<>(errorResponse, HttpStatus.OK);
        }

        // Check if the desk exists
        boolean deskExists = queryService.executeCheckDeskExistsQuery(deskLocation);
        if (!deskExists) {
            // Return a JSON response with an error message if the desk does not exist
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Desk not found");
            return new ResponseEntity<>(errorResponse, HttpStatus.OK);
        }

        // If both user and desk exist:
        String result = queryService.executeCreateBookingQuery(userEmail, deskLocation, bookingDate);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/getDeskBookings")
    public ResponseEntity<?> getDeskBookings(
            @RequestHeader Map<String, String> headers,
            @RequestBody InsertDesk body
    ) {
        String deskLocation = body.getLocation();

        // Check if the desk exists
        boolean deskExists = queryService.executeCheckDeskExistsQuery(deskLocation);
        if (!deskExists) {
            // Return a JSON response with an error message if the desk does not exist
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Desk not found");
            return new ResponseEntity<>(errorResponse, HttpStatus.OK);
        }

        // If the desk exists, continue as usual
        List<Map<String, Object>> bookings = queryService.executeGetDeskBookingsQuery(deskLocation);
        return new ResponseEntity<>(bookings, HttpStatus.OK);
    }


    @PostMapping("/getUserBookings")
    public ResponseEntity<?> getDeskBookings(
            @RequestHeader Map<String, String> headers,
            @RequestBody InsertUser body
    ) {
        String userEmail = body.getUserEmail();

        // Check if the user exists
        boolean userExists = queryService.executeCheckUserExistsQuery(userEmail);
        if (!userExists) {
            // Return a JSON response with an error message if the desk does not exist
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "User not found");
            return new ResponseEntity<>(errorResponse, HttpStatus.OK);
        }

        // If the user exists, continue as usual
        List<Map<String, Object>> bookings = queryService.executeGetUserBookingsQuery(userEmail);
        return new ResponseEntity<>(bookings, HttpStatus.OK);
    }

    @PostMapping("/checkUserExists")
    public boolean checkUserExists(
            @RequestHeader Map<String, String> headers,
            @RequestBody InsertUser body
    ) {
        String userEmail = body.getUserEmail();
        return queryService.executeCheckUserExistsQuery(userEmail);
    }

    @PostMapping("/checkDeskExists")
    public boolean checkDeskExists(
            @RequestHeader Map<String, String> headers,
            @RequestBody InsertDesk body
    ) {
        String deskLocation = body.getLocation();
        return queryService.executeCheckDeskExistsQuery(deskLocation);
    }

}
