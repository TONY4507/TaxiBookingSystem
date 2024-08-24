package com.project.TaxiBookingSystem.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.TaxiBookingSystem.dto.CustomerDTO;
import com.project.TaxiBookingSystem.dto.DriverDTO;

import com.project.TaxiBookingSystem.service.AdminService;
import com.project.TaxiBookingSystem.service.CustomerService;
import com.project.TaxiBookingSystem.service.DriverService;

import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Admin", description = "API for Tenant Management")
@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;
    
    @Autowired
    private CustomerService customerService;
    
    @Autowired
    private DriverService driverService;
    
    
    @Operation(summary = "Pending Customers", description = "Admin Endpoint Get Pending Customer")
    @GetMapping("/pending/customers")
    public ResponseEntity<List<CustomerDTO>> getPendingCustomers() {
        List<CustomerDTO> pendingCustomers = customerService.getPendingCustomers();
        return ResponseEntity.ok(pendingCustomers);
    }

    @Operation(summary = "Pending Drivers", description = "Admin Endpoint Get Pending Drivers")
    @GetMapping("/pending/drivers")
    public ResponseEntity<List<DriverDTO>> getPendingDrivers() {
        List<DriverDTO> pendingCustomers = driverService.getPendingDrivers();
        return ResponseEntity.ok(pendingCustomers);
    }

    @Operation(summary = "Approve Customers", description = "Admin Endpoint Approve Customer request")
    @PutMapping("/approve/customer/{customerId}")
    public ResponseEntity<String> approveCustomer(@PathVariable int customerId) {
        try {
    	adminService.approveCustomer(customerId);
        return ResponseEntity.ok("Customer approved successfully");
        }
        catch(Exception e) {
       	 return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Customer not found with ID: " + customerId);
       	
       }
    }

    @Operation(summary = "Approve Driver", description = "Admin Endpoint Approve Driver request")
    @PutMapping("/approve/driver/{driverId}")
    public ResponseEntity<String> approveDriver(@PathVariable int driverId) {
        try {
    	adminService.approveDriver(driverId);
    	return ResponseEntity.ok("Driver approved successfully");
        }
        
        catch(Exception e) {
        	 return ResponseEntity.status(HttpStatus.NOT_FOUND)
                     .body("Driver not found with ID: " + driverId);
        	
        }
    
    }

    @Operation(summary = "Reject Customer", description = "This Endpoint Reject Customer")
    @PutMapping("/reject/customer/{customerId}")
    public ResponseEntity<String> rejectCustomer(@PathVariable int customerId) {
        try {
    	adminService.rejectCustomer(customerId);
        return ResponseEntity.ok("Customer rejected successfully");
        }
        catch(Exception e) {
       	 return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Customer not found with ID: " + customerId);
       	
       }
    }

    @Operation(summary = "Reject Driver", description = "This Endpoint Reject Driver")
    @PutMapping("/reject/driver/{driverId}")
    public ResponseEntity<String> rejectDriver(@PathVariable int driverId) {
        try {
    	adminService.rejectDriver(driverId);
        return ResponseEntity.ok("Driver rejected successfully");
        }
        catch(Exception e) {
       	 return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Driver not found with ID: " + driverId);
       	
       }
    }
}
