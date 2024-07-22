package com.example.MyApp.dto;

import java.sql.Date;
import java.time.LocalDate;

public class InsertBooking {
    private String bookingDate;
    private String location;
    private String userEmail;

    // Getters and Setters
    public String getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(String bookingDate) {
        this.bookingDate = bookingDate;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public Date getBookingDateAsSqlDate() {
        LocalDate localDate = LocalDate.parse(this.bookingDate);
        return Date.valueOf(localDate);
    }
}

