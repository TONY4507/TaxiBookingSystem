package com.project.TaxiBookingSystem.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.project.TaxiBookingSystem.entity.Driver;
import com.project.TaxiBookingSystem.entity.TripBooking;
import com.project.TaxiBookingSystem.service.DriverService;
import com.project.TaxiBookingSystem.service.TripBookingService;

import java.util.List;

@RestController
@RequestMapping("/api/drivers")
public class DriverController {

    @Autowired
    private DriverService driverService;

    @Autowired
    private TripBookingService tripBookingService;

    // Driver Signup
    @PostMapping("/signup")
    public ResponseEntity<Driver> signUp(@RequestBody Driver driver) {
        Driver newDriver = driverService.signup(driver);
        return ResponseEntity.ok(newDriver);
    }

    // Driver Login
    @PostMapping("/login")
    public ResponseEntity<Driver> login(@RequestParam String email, @RequestParam String password) {
        Driver driver = driverService.login(email, password);
        if (driver != null) {
            return ResponseEntity.ok(driver);
        } else {
            return ResponseEntity.status(401).body(null); // Unauthorized
        }
    }

    // View Booking History
    @GetMapping("/{driverId}/bookings")
    public ResponseEntity<List<TripBooking>> getDriverBookingHistory(@PathVariable int driverId) {
        List<TripBooking> bookingHistory = driverService.getDriverBookingHistory(driverId);
        return ResponseEntity.ok(bookingHistory);
    }
    
    @PutMapping("/bookings/{tripBookingId}/confirm")
    public ResponseEntity<String> confirmBooking(@PathVariable int tripBookingId,
                                                  @RequestParam boolean accept,
                                                  @RequestHeader("Authorization") String authHeader) {
       
            // Extract the driver ID from the authorization header or session (depends on your authentication method)
            int driverId = extractDriverIdFromAuthHeader(authHeader);

            // Verify if the booking belongs to the driver
     
            // Confirm or reject the trip
            driverService.confirmTrip(tripBookingId,driverId, accept);

       
            return ResponseEntity.status(500).body("An unexpected error occurred.");
        
    }

    // Method to extract driver ID from the authorization header or session
    private int extractDriverIdFromAuthHeader(String authHeader) {
        // Implement extraction logic based on your authentication mechanism
        // For example, decode JWT token to extract driver ID
        return 0; // Placeholder value
    }

}
