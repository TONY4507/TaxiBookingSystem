package com.project.TaxiBookingSystem.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;


import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;

import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Cab {
    @Id
    private String cabNumber;
    
    private String carType;
    private float perKMRate;
    private boolean isAvailable;
  
    @JsonIgnore
    @OneToOne(cascade = CascadeType.ALL,mappedBy = "cab")
    private Driver driver;


    public String getCabNumber() {
        return cabNumber;
    }

    public void setCabNumber(String cabId) {
        this.cabNumber = cabId;
    }

    public String getCarType() {
        return carType;
    }

    public void setCarType(String carType) {
        this.carType = carType;
    }

    public float getPerKMRate() {
        return perKMRate;
    }

    public void setPerKMRate(float perKMRate) {
        this.perKMRate = perKMRate;
    }
    
    public boolean isAvailable() {
  		return isAvailable;
  	}

  	public void setAvailable(boolean isAvailable) {
  		this.isAvailable = isAvailable;
  	}

	public Driver getDriver() {
		return driver;
	}

	public void setDriver(Driver driver) {
		this.driver = driver;
	}
  	
}
