package com.project.TaxiBookingSystem.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.project.TaxiBookingSystem.entity.Cab;
import com.project.TaxiBookingSystem.entity.Customer;
import com.project.TaxiBookingSystem.entity.TripBooking;
import com.project.TaxiBookingSystem.service.CabService;
import com.project.TaxiBookingSystem.service.CustomerService;
import com.project.TaxiBookingSystem.service.TripBookingService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private CabService cabService;

    @Autowired
    private TripBookingService tripBookingService;

    // Customer Signup
    @PostMapping("/signup")
    public ResponseEntity<Customer> signUp(@Valid @RequestBody Customer customer) {
        Customer newCustomer = customerService.signup(customer);
        return ResponseEntity.ok(newCustomer);
    }

    // Customer Login
    @PostMapping("/login")
    public ResponseEntity<Customer> login(@RequestParam @Email(message = "{email.invalid}")String email, @RequestParam @Size(min = 8, max = 20, message = "{customer.password.size}") String password) {
        Customer customer = customerService.login(email, password);
        if (customer != null) {
            return ResponseEntity.ok(customer);
        } else {
            return ResponseEntity.status(401).body(null); // Unauthorized
        }
    }

    // Update Customer Profile
    @PutMapping("/{customerId}")
    public ResponseEntity<Customer> updateProfile(@PathVariable @Min(value = 1, message = "Customer ID must be greater than 0") int customerId, @Valid @RequestBody Customer updatedCustomer) {
        Customer customer = customerService.updateProfile(customerId, updatedCustomer);
        return ResponseEntity.ok(customer);
    }

    // View All Available Cabs
    @GetMapping("/cabs")
    public ResponseEntity<List<Cab>> viewAvailableCabs() {
        List<Cab> availableCabs = cabService.viewAvailableCabs();
        return ResponseEntity.ok(availableCabs);
    }

    // Book a Trip
    @PostMapping("/bookings")
    public ResponseEntity<TripBooking> bookTrip(@RequestParam @Min(value = 1, message = "Customer ID must be greater than 0") int customerId,
    											@RequestParam @Min(value = 1, message = "Cab ID must be greater than 0") int cabid,
                                                @RequestParam @NotEmpty(message = "From location cannot be empty") String fromLocation, 
                                                @RequestParam @NotEmpty(message = "To location cannot be empty") String toLocation, 
                                                @RequestParam @Min(value = 1, message = "Distance must be greater than 0 km") float distanceInKm) {
        TripBooking tripBooking = tripBookingService.bookTrip(customerId, cabid,fromLocation, toLocation, distanceInKm);
        return ResponseEntity.ok(tripBooking);
    }

    // Cancel a Trip
    @DeleteMapping("/bookings/{tripBookingId}")
    public ResponseEntity<Void> cancelTrip(@PathVariable @Min(value = 1, message = "Booking ID must be greater than 0") int tripBookingId, @RequestParam @Min(value = 1, message = "Customer ID must be greater than 0") int customerId) {
    	tripBookingService.cancelTrip(tripBookingId, customerId);
        return ResponseEntity.noContent().build(); // No content to return
    }

    // View Booking History
    @GetMapping("/{customerId}/bookings")
    public ResponseEntity<List<TripBooking>> viewBookingHistory(@PathVariable @Min(value = 1, message = "Customer ID must be greater than 0") int customerId) {
        List<TripBooking> bookingHistory = tripBookingService.viewBookingHistory(customerId);
        return ResponseEntity.ok(bookingHistory);
    }

}