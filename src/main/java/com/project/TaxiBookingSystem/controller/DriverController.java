package com.project.TaxiBookingSystem.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.project.TaxiBookingSystem.entity.Driver;
import com.project.TaxiBookingSystem.entity.TripBooking;
import com.project.TaxiBookingSystem.service.DriverService;
import com.project.TaxiBookingSystem.service.TripBookingService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

import java.util.List;

@RestController
@RequestMapping("/api/drivers")
public class DriverController {

    @Autowired
    private DriverService driverService;

 

    // Driver Signup
    @PostMapping("/signup")
    public ResponseEntity<Driver> signUp(@Valid @RequestBody Driver driver) {
        Driver newDriver = driverService.signup(driver);
        return ResponseEntity.ok(newDriver);
    }

    // Driver Login
    @PostMapping("/login")
    public ResponseEntity<Driver> login(@RequestParam @Email(message = "Please provide correct Email")String email, @RequestParam @Size(min = 8, max = 20, message = "{customer.password.size}")String password) {
        Driver driver = driverService.login(email, password);
        if (driver != null) {
            return ResponseEntity.ok(driver);
        } else {
            return ResponseEntity.status(401).body(null); // Unauthorized
        }
    }

    // View Booking History
    @GetMapping("/{driverId}/bookings")
    public ResponseEntity<List<TripBooking>> getDriverBookingHistory(@PathVariable @Min(value = 1, message = "Driver ID must be greater than 0")  int driverId) {
        List<TripBooking> bookingHistory = driverService.getDriverBookingHistory(driverId);
        return ResponseEntity.ok(bookingHistory);
    }
    
    @PutMapping("/bookings/{tripBookingId}/confirm")
    public ResponseEntity<String> confirmBooking(@PathVariable @Min(value = 1, message = "trip ID must be greater than 0") int tripBookingId,
                                                  @RequestParam  boolean accept,
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
