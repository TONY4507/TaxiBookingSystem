package com.project.TaxiBookingSystem.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.project.TaxiBookingSystem.dto.DriverDTO;
import com.project.TaxiBookingSystem.entity.Driver;
import com.project.TaxiBookingSystem.entity.TripBooking;
import com.project.TaxiBookingSystem.service.DriverService;
import com.project.TaxiBookingSystem.service.TripBookingService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

import java.util.List;

@Tag(name = "Driver", description = "API for Driver Management")
@RestController
@RequestMapping("/drivers")
public class DriverController {

    @Autowired
    private DriverService driverService;

    @Autowired
    TripBookingService tripBookingService;
 

    @Operation(summary = "Driver SignUp", description = "Driver SignUp Endpoint")
    @PostMapping("/signup")
    public ResponseEntity<DriverDTO> signUp(@Valid @RequestBody Driver driver) {
        DriverDTO newDriver = driverService.signup(driver);
        return ResponseEntity.status(201).body(newDriver);
    }

    @Operation(summary = "Driver Login", description = "Driver Login Endpoint")
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestParam @Email(message = "Please provide correct Email")String email, @RequestParam @Size(min = 8, max = 20, message = "{customer.password.size}")String password) {
        try {
    	Driver driver = driverService.login(email, password);
            return ResponseEntity.ok("Welcome "+ driver.getUsername());
        }
        catch(Exception e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Invalid credentials: " + e.getMessage()); // Unauthorized
        }
    }

    @Operation(summary = "Driver Trip History", description = "Driver Enpoint to get Driver History")
    @GetMapping("/{driverId}/bookings")
    public ResponseEntity<?> getDriverBookingHistory(@PathVariable @Min(value = 1, message = "Driver ID must be greater than 0")  int driverId) {
        try {
    	List<TripBooking> bookingHistory = tripBookingService.getConfirmedTripsByDriverId(driverId);
        return ResponseEntity.ok(bookingHistory);
        }
        catch(Exception e) {
        	return ResponseEntity.status(404).body(e.getMessage());
        }
    }
    
    
    @Operation(summary = "Confirm Ride", description = "Driver can Confirm the Customer Ride") 
    @PutMapping("/confirm-booking/{tripBookingId}/{driverId}")
    public String confirmBooking(@PathVariable int tripBookingId,@PathVariable @Min(value = 1, message = "Booking ID must be greater than 0")int driverId,@RequestParam  boolean accept) {
        boolean isConfirmed = tripBookingService.confirmTrip(tripBookingId,driverId,accept);

        if (isConfirmed) {
            return "Booking confirmed, and cab availability updated.";
        } else {
            return "Booking could not be confirmed. Please check the booking ID.";
        }
    }
    
    @Operation(summary = "Available Bookings", description = "Driver can see available Rides")
    @GetMapping("/available-bookings/{driverId}")
    public ResponseEntity<?> getAvailableRides(@PathVariable @Min(value = 1, message = "Booking ID must be greater than 0")int driverId) {
        try {
    	return ResponseEntity.ok(tripBookingService.getAvailableRides(driverId));
        }
        catch(Exception e) {
        	return ResponseEntity.status(404).body(e.getMessage());
        }
    }
    

}
