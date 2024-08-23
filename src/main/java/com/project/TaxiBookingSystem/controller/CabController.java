package com.project.TaxiBookingSystem.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.project.TaxiBookingSystem.entity.Cab;
import com.project.TaxiBookingSystem.service.CabService;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;

import java.util.List;

@RestController
@RequestMapping("/api/cabs")
public class CabController {

    @Autowired
    private CabService cabService;

    // Get all available cabs
    @GetMapping("/available")
    public ResponseEntity<List<Cab>> getAvailableCabs() {
        List<Cab> availableCabs = cabService.viewAvailableCabs();
        return ResponseEntity.ok(availableCabs);
    }

    // Add a new cab
    @PostMapping
    public ResponseEntity<Cab> addCab(@Valid @RequestBody Cab cab) {
        Cab newCab = cabService.addCab(cab);
        return ResponseEntity.status(201).body(newCab); // Created
    }

    // Update an existing cab
    @PutMapping("/{cabId}")
    public ResponseEntity<Cab> updateCab(@PathVariable @Min(value = 1, message = "Cab ID must be greater than 0") int cabId,@Valid @RequestBody Cab updatedCab) {
        try {
            Cab cab = cabService.updateCab(cabId, updatedCab);
            return ResponseEntity.ok(cab);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(404).body(null); // Not Found
        }
    }

    // Delete a cab
    @DeleteMapping("/{cabId}")
    public ResponseEntity<Void> deleteCab(@PathVariable @Min(value = 1, message = "Cab ID must be greater than 0") int cabId) {
        try {
            cabService.deleteCab(cabId);
            return ResponseEntity.noContent().build(); // No Content
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(404).body(null); // Not Found
        }
    }

    // Get a cab by ID
    @GetMapping("/{cabId}")
    public ResponseEntity<Cab> getCabById(@PathVariable @Min(value = 1, message = "Cab ID must be greater than 0") int cabId) {
        try {
            Cab cab = cabService.getCab(cabId);
            return ResponseEntity.ok(cab);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(404).body(null); // Not Found
        }
    }
}
