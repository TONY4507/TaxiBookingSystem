//package com.project.TaxiBookingSystem.dto;
//
//import javax.validation.constraints.NotNull;
//import java.util.Date;
//
//public class TaxiBookingDTO {
//    private int id;
//
//    @NotNull(message = "Customer is required")
//    private CustomerDTO customer;
//
//    @NotNull(message = "Driver is required")
//    private DriverDTO driver;
//
//    @NotNull(message = "From location is required")
//    private String fromLocation;
//
//    @NotNull(message = "To location is required")
//    private String toLocation;
//
//    @NotNull(message = "Distance is required")
//    private float distanceInKm;
//
//    private float bill;
//
//    private Date bookingDate;
//
//    private boolean status; // true: confirmed, false: pending or cancelled
//
//    // Constructors, Getters, and Setters
//}
