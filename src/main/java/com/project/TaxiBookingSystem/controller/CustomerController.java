package com.project.TaxiBookingSystem.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.project.TaxiBookingSystem.dto.CustomerDTO;
import com.project.TaxiBookingSystem.entity.Cab;
import com.project.TaxiBookingSystem.entity.Customer;
import com.project.TaxiBookingSystem.entity.TripBooking;
import com.project.TaxiBookingSystem.exception.EntityNotFoundException;
import com.project.TaxiBookingSystem.service.CabService;
import com.project.TaxiBookingSystem.service.CustomerService;
import com.project.TaxiBookingSystem.service.TripBookingService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.List;

@Tag(name = "Customer", description = "API for Customer Management")
@RestController
@RequestMapping("/customers")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

 

    @Autowired
    private TripBookingService tripBookingService;

    
    @Operation(summary = "Customer SignUp", description = "Customer SignUp Endpoint")
    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@Valid @RequestBody Customer customer) {
        try {
    	CustomerDTO newCustomer = customerService.signup(customer);
        return ResponseEntity.status(201).body(newCustomer);
        }
        catch(Exception e){
        	return ResponseEntity.status(404).body(e.getMessage());
        }
    
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
    @PutMapping("/update/{customerId}")
    public ResponseEntity<Customer> updateProfile(@PathVariable @Min(value = 1, message = "Customer ID must be greater than 0") int customerId, @Valid @RequestBody Customer updatedCustomer) {
        Customer customer = customerService.updateProfile(customerId,updatedCustomer);
        return ResponseEntity.status(202).body(customer);
    }

   
    @Operation(summary = "Trip Booking", description = "Customer Can BookTrip")
//    @PostMapping("/bookings")
//    public ResponseEntity<TripBooking> bookTrip(
//            @RequestParam @Min(value = 1, message = "Customer ID must be greater than 0") int customerId,
//            @RequestParam @Min(value = 1, message = "Cab ID must be greater than 0") String cabid,
//            @RequestParam @NotEmpty(message = "From location cannot be empty") String fromLocation, 
//            @RequestParam @NotEmpty(message = "To location cannot be empty") String toLocation, 
//            @RequestParam @NotNull(message = "DateTime cannot be empty")  @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") @Schema(type = "string", format = "date-time") LocalDateTime DateTime,
//            @RequestParam @Min(value = 1, message = "Distance must be greater than 0 km") float distanceInKm) {
//    	 System.out.println(customerId+"00000000000000000000000000000");
//        System.out.println(DateTime+"00000000000000000000000000000");
//    	TripBooking tripBooking = tripBookingService.bookTrip(customerId, cabid,fromLocation, toLocation,DateTime, distanceInKm);
//        return ResponseEntity.ok(tripBooking);
//  @NotNull(message = "DateTime cannot be empty")  @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") @Schema(type = "string", format = "date-time")  }
    @PostMapping("/bookings")
    public ResponseEntity<TripBooking> bookTrip(
            @RequestParam @Min(value = 1, message = "Customer ID must be greater than 0") int customerId,
            @RequestParam  @NotEmpty( message = "Cab ID must be greater than 0") String cabid,
            @RequestParam   @NotEmpty(message = "From location cannot be empty") String fromLocation, 
            @RequestParam  @NotEmpty(message = "To location cannot be empty") String toLocation, 
            @RequestParam  @NotNull(message = "DateTime cannot be empty")  @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") @Schema(type = "string", format = "date-time")  LocalDateTime DateTime,
            @RequestParam @Min(value = 1, message = "Distance must be greater than 0 km") float distanceInKm) {
    	
    	TripBooking tripBooking = tripBookingService.bookTrip(customerId, cabid,fromLocation, toLocation,DateTime, distanceInKm);
        return ResponseEntity.ok(tripBooking);
    }
  
    

    
    @Operation(summary = "Cancel Booking", description = "Customer can Cancel Booking")
    @DeleteMapping("/bookings/{tripBookingId}")
    public ResponseEntity<String> cancelTrip(@PathVariable @Min(value = 1, message = "Booking ID must be greater than 0") int tripBookingId, @RequestParam @Min(value = 1, message = "Customer ID must be greater than 0") int customerId) {
    	tripBookingService.cancelTrip(tripBookingId, customerId);
        return ResponseEntity.status(200).body("Booking Cancelled");// No content to return
    }

    
    @Operation(summary = "Get Booking History", description = "Customer Can get Booking History")
    @GetMapping("/{customerId}/bookings")
    public ResponseEntity<Object> viewBookingHistory(@PathVariable @Min(value =1,message ="Required" ) int customerId) {
        try {
    	List<TripBooking> bookingHistory = tripBookingService.viewBookingHistory(customerId);
        return ResponseEntity.ok(bookingHistory);
        }
        catch(Exception e) {
        	return ResponseEntity.status(404).body(e.getMessage());
        }
    }
    
    
    @Operation(summary = "Delete Customer", description = "Customer can remove account")
    @DeleteMapping("/delete/{customerid}")
    public ResponseEntity<String> deleteCustomer(@RequestParam @NotBlank(message = "Customer Email not blank") String  Email) {
    	customerService.deleteCustomer( Email);
        return ResponseEntity.status(200).body("Deleted Success"); 
    }

}