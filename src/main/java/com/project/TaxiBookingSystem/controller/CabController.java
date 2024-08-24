package com.project.TaxiBookingSystem.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import com.project.TaxiBookingSystem.entity.Cab;
import com.project.TaxiBookingSystem.exception.EntityNotFoundException;
import com.project.TaxiBookingSystem.service.CabService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;

import jakarta.validation.constraints.NotEmpty;

import java.util.List;

@Tag(name = "Cab", description = "API for Cab Management")
@RestController
@RequestMapping("/cabs")
public class CabController {

    @Autowired
    private CabService cabService;


    @Operation(summary = "Get all Available Cabs", description = "This is collection of Cab")
    @GetMapping("/available")
    public ResponseEntity<List<Cab>> getAvailableCabs() {
        List<Cab> availableCabs = cabService.viewAvailableCabs();
        return ResponseEntity.ok(availableCabs);
    }

//    @Operation(summary = "Add New Cab", description = "This Endpoint Adds New Cabs")
//    @PostMapping
//    public ResponseEntity<Cab> addCab(@Valid @RequestBody Cab cab) {
//        Cab newCab = cabService.addCab(cab);
//        return ResponseEntity.status(201).body(newCab); // Created
//    }

    @Operation(summary = "Update Existing Cabs", description = "This Endpoint Updates Existing Cabs Data")
    @PutMapping("/{cabId}")
    public ResponseEntity<Object> updateCab(@PathVariable @NotEmpty(message = "Cab ID must required") String cabId,@Valid @RequestBody Cab updatedCab) {
        try {
            Cab cab = cabService.updateCab(cabId, updatedCab);
            return ResponseEntity.ok(cab);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(404).body(e.getMessage()); // Not Found
        }
    }

    @Operation(summary = "Delete Cabs", description = "This Endpoint Updates Deletes Cabs")
    @DeleteMapping("/{cabId}")
    public ResponseEntity<Void> deleteCab(@PathVariable @NotEmpty(message = "Cab ID Required") String cabId) {
        try {
            cabService.deleteCab(cabId);
            return ResponseEntity.noContent().build(); // No Content
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(404).body(null); // Not Found
        }
    }


    @Operation(summary = "Get Single Cab By ID", description = "This Endpoint Gives Single Cab Data")
    @GetMapping("/{cabId}")
    public ResponseEntity<Object> getCabById(@PathVariable @NotEmpty(message="Cab ID Required") String cabId) {
        try {
            Cab cab = cabService.getCab(cabId);
            return ResponseEntity.ok(cab);
        } catch (EntityNotFoundException e) {
            
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            // Handle any other unexpected exceptions
           
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
