package com.project.TaxiBookingSystem.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.project.TaxiBookingSystem.enums.ApprovalStatus;


import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Driver extends User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonIgnore
    private int driverId;
    
    @NotBlank(message = "{driver.license.required}")
    @Size(min = 5, max = 15, message = "{driver.license.size}")
    private String licenseNumber;
    
    @NotNull(message = "{driver.cab.required}")
    @OneToOne(cascade = CascadeType.ALL)
    private Cab cab;
  
    @JsonIgnore
    @Min(value = 0, message = "{driver.rating.range}")
    @Max(value = 5, message = "{driver.rating.range}")
    private float rating;
    

//    @Enumerated(EnumType.STRING)
//    private Role role;
    @JsonIgnore
    @Enumerated(EnumType.STRING)
    private ApprovalStatus approvalStatus = ApprovalStatus.PENDING;

    // Getters and Setters
    public int getDriverId() {
        return driverId;
    }

    public void setDriverId(int driverId) {
        this.driverId = driverId;
    }

    public String getLicenseNumber() {
        return licenseNumber;
    }

    public void setLicenseNumber(String licenseNumber) {
        this.licenseNumber = licenseNumber;
    }

    public Cab getCab() {
        return cab;
    }

    public void setCab(Cab cab) {
        this.cab = cab;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }


//	public Role getRole() {
//		return role;
//	}
//
//	public void setRole(Role role) {
//		this.role = role;
//	}

	public ApprovalStatus getApprovalStatus() {
		return approvalStatus;
	}

	public void setApprovalStatus(ApprovalStatus approvalStatus) {
		this.approvalStatus = approvalStatus;
	}
	
    
}
