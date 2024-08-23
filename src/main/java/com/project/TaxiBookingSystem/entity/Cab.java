package com.project.TaxiBookingSystem.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;

@Entity
public class Cab {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int cabId;
    
    private String carType;
    private float perKMRate;
    private boolean isAvailable;
  
    @OneToOne(cascade = CascadeType.ALL,mappedBy = "cab")
    private Driver driver;

	// Getters and Setters
    public int getCabId() {
        return cabId;
    }

    public void setCabId(int cabId) {
        this.cabId = cabId;
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
