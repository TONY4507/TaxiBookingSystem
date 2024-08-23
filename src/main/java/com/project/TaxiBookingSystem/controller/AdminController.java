package com.project.TaxiBookingSystem.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.TaxiBookingSystem.service.AdminService;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @PutMapping("/approve/customer/{customerId}")
    public ResponseEntity<String> approveCustomer(@PathVariable int customerId) {
        adminService.approveCustomer(customerId);
        return ResponseEntity.ok("Customer approved successfully");
    }

    @PutMapping("/approve/driver/{driverId}")
    public ResponseEntity<String> approveDriver(@PathVariable int driverId) {
        adminService.approveDriver(driverId);
        return ResponseEntity.ok("Driver approved successfully");
    }

    @PutMapping("/reject/customer/{customerId}")
    public ResponseEntity<String> rejectCustomer(@PathVariable int customerId) {
        adminService.rejectCustomer(customerId);
        return ResponseEntity.ok("Customer rejected successfully");
    }

    @PutMapping("/reject/driver/{driverId}")
    public ResponseEntity<String> rejectDriver(@PathVariable int driverId) {
        adminService.rejectDriver(driverId);
        return ResponseEntity.ok("Driver rejected successfully");
    }
}
