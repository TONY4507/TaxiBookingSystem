package com.project.TaxiBookingSystem.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.project.TaxiBookingSystem.entity.Cab;
import com.project.TaxiBookingSystem.entity.Customer;
import com.project.TaxiBookingSystem.entity.TripBooking;
import com.project.TaxiBookingSystem.service.CabService;
import com.project.TaxiBookingSystem.service.CustomerService;
import com.project.TaxiBookingSystem.service.TripBookingService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.util.List;

@Tag(name = "Customer", description = "API for Customer Management")
@RestController
@RequestMapping("/customers")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private CabService cabService;

    @Autowired
    private TripBookingService tripBookingService;

    
    @Operation(summary = "Customer SignUp", description = "Customer SignUp Endpoint")
    @PostMapping("/signup")
    public ResponseEntity<Customer> signUp(@Valid @RequestBody Customer customer) {
        Customer newCustomer = customerService.signup(customer);
        return ResponseEntity.ok(newCustomer);
    }

    @Operation(summary = "Customer Login", description = "Existing Customer can login")
    @PostMapping("/login")
    public  ResponseEntity<String> login(@RequestParam @Email(message = "{email.invalid}")String email, @RequestParam @Size(min = 8, max = 20, message = "{customer.password.size}") String password) {
        try {
    	Customer customer= customerService.login(email, password);
            return ResponseEntity.ok("Welcome "+ customer.getUsername());
            
        } 
        catch(Exception e) {
        	return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Invalid credentials: " + e.getMessage());
    }
    }

    
    @Operation(summary = "Customer Profile Update", description = "Customer Endpoint update data")
    @PutMapping("/{customerId}")
    public ResponseEntity<Customer> updateProfile(@PathVariable @Min(value = 1, message = "Customer ID must be greater than 0") int customerId, @Valid @RequestBody Customer updatedCustomer) {
        Customer customer = customerService.updateProfile(customerId, updatedCustomer);
        return ResponseEntity.ok(customer);
    }

    
//    @Operation(summary = "Approve Customers", description = "Admin Endpoint Approve Customer request")
//    @GetMapping("/cabs")
//    public ResponseEntity<List<Cab>> viewAvailableCabs() {
//        List<Cab> availableCabs = cabService.viewAvailableCabs();
//        return ResponseEntity.ok(availableCabs);
//    }

    @Operation(summary = "Trip Booking", description = "Customer Can BookTrip")
    @PostMapping("/bookings")
    public ResponseEntity<TripBooking> bookTrip(@RequestParam @Min(value = 1, message = "Customer ID must be greater than 0") int customerId,
    											@RequestParam @Min(value = 1, message = "Cab ID must be greater than 0") String cabid,
                                                @RequestParam @NotEmpty(message = "From location cannot be empty") String fromLocation, 
                                                @RequestParam @NotEmpty(message = "To location cannot be empty") String toLocation, 
                                                @RequestParam @Min(value = 1, message = "Distance must be greater than 0 km") float distanceInKm) {
        TripBooking tripBooking = tripBookingService.bookTrip(customerId, cabid,fromLocation, toLocation, distanceInKm);
        return ResponseEntity.ok(tripBooking);
    }

    
    @Operation(summary = "Cancel Booking", description = "Customer can Cancel Booking")
    @DeleteMapping("/bookings/{tripBookingId}")
    public ResponseEntity<Void> cancelTrip(@PathVariable @Min(value = 1, message = "Booking ID must be greater than 0") int tripBookingId, @RequestParam @Min(value = 1, message = "Customer ID must be greater than 0") int customerId) {
    	tripBookingService.cancelTrip(tripBookingId, customerId);
        return ResponseEntity.noContent().build(); // No content to return
    }

    
    @Operation(summary = "Get Booking History", description = "Customer Can get Booking History")
    @GetMapping("/{customerId}/bookings")
    public ResponseEntity<List<TripBooking>> viewBookingHistory(@PathVariable @Min(value = 1, message = "Customer ID must be greater than 0") int customerId) {
        List<TripBooking> bookingHistory = tripBookingService.viewBookingHistory(customerId);
        return ResponseEntity.ok(bookingHistory);
    }

}