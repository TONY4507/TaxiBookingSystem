package com.project.TaxiBookingSystem.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CabDTO {
   


    @NotNull(message = "Cab registration number is required")
    private String registrationNumber;
	
    @NotNull(message = "Cab model is required")
    private String model;


    @NotNull(message = "Per KM rate is required")
    private float perKmRate;

    @NotNull(message = "Availablity required")
    private boolean availability;

    // Constructors, Getters, and Setters
}
