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
    public ResponseEntity<Customer> signUp(@RequestBody Customer customer) {
        Customer newCustomer = customerService.signup(customer);
        return ResponseEntity.ok(newCustomer);
    }

    // Customer Login
    @PostMapping("/login")
    public ResponseEntity<Customer> login(@RequestParam String email, @RequestParam String password) {
        Customer customer = customerService.login(email, password);
        if (customer != null) {
            return ResponseEntity.ok(customer);
        } else {
            return ResponseEntity.status(401).body(null); // Unauthorized
        }
    }

    // Update Customer Profile
    @PutMapping("/{customerId}")
    public ResponseEntity<Customer> updateProfile(@PathVariable int customerId, @RequestBody Customer updatedCustomer) {
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
    public ResponseEntity<TripBooking> bookTrip(@RequestParam int customerId,
    											@RequestParam int cabid,
                                                @RequestParam String fromLocation, 
                                                @RequestParam String toLocation, 
                                                @RequestParam float distanceInKm) {
        TripBooking tripBooking = tripBookingService.bookTrip(customerId, cabid,fromLocation, toLocation, distanceInKm);
        return ResponseEntity.ok(tripBooking);
    }

    // Cancel a Trip
    @DeleteMapping("/bookings/{tripBookingId}")
    public ResponseEntity<Void> cancelTrip(@PathVariable int tripBookingId, @RequestParam int customerId) {
    	tripBookingService.cancelTrip(tripBookingId, customerId);
        return ResponseEntity.noContent().build(); // No content to return
    }

    // View Booking History
    @GetMapping("/{customerId}/bookings")
    public ResponseEntity<List<TripBooking>> viewBookingHistory(@PathVariable int customerId) {
        List<TripBooking> bookingHistory = tripBookingService.viewBookingHistory(customerId);
        return ResponseEntity.ok(bookingHistory);
    }

}