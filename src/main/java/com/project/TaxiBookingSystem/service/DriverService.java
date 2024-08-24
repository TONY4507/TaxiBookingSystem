package com.project.TaxiBookingSystem.service;

import java.util.List;

import java.util.Optional;
import java.util.stream.Collectors;  
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.TaxiBookingSystem.dto.DriverDTO;
import com.project.TaxiBookingSystem.entity.Driver;
import com.project.TaxiBookingSystem.entity.TripBooking;
import com.project.TaxiBookingSystem.enums.ApprovalStatus;
import com.project.TaxiBookingSystem.repository.DriverRepository;
import com.project.TaxiBookingSystem.repository.TripBookingRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class DriverService {
	
	  @Autowired
	    private TripBookingRepository tripBookingRepository;

	   @Autowired
	    private DriverRepository driverRepository;

	    public Driver login(String email, String password) {
	        Optional<Driver> driver = driverRepository.findByEmail(email)
	                                  .filter(c -> c.getPassword().equals(password));
       
		if (driver.isEmpty()) {
		 throw new EntityNotFoundException("Driver not found" );
		}

		return driver.get();
}
	   
	    
	    public Driver signup(Driver driver) {
	        // You can add validation logic here (e.g., check if email already exists)
	        return driverRepository.save(driver);
	    }
	    
//
//	public List<TripBooking> viewPendingTripsForDriver(int driverId) {
//	    return tripBookingRepository.findByDriverDriverIdAndStatus(driverId, false); // status = false indicates pending
//	}
//
//	
	
	public void confirmTrip(int tripBookingId, int driverId, boolean accept) {
	    TripBooking tripBooking = tripBookingRepository.findById(tripBookingId)
	            .orElseThrow(() -> new EntityNotFoundException("Trip not found"));

	   
	    if (tripBooking.getDriver().getDriverId() != driverId) {
	        throw new RuntimeException("Unauthorized operation. Trip not assigned to this driver.");
	    }

	    if (accept) {
	        
	        tripBooking.setStatus(true); 
	     
	        tripBooking.setStatus(false); 

	    tripBookingRepository.save(tripBooking);
	}

	}
	  public List<TripBooking> getDriverBookingHistory(int driverId) {
	        return tripBookingRepository.findByDriverDriverId(driverId);
	    }
	  
	  public List<DriverDTO> getPendingDrivers() {
		  return driverRepository.findAll().stream()
			        .filter(driver -> driver.getApprovalStatus() == ApprovalStatus.PENDING)
			        .map(driver -> new DriverDTO(
			                driver.getDriverId(),
			                driver.getUsername(),
			                driver.getPassword(),
			                driver.getAddress(),
			                driver.getMobileNumber(),
			                driver.getEmail(),
			                driver.getLicenseNumber(),
			                driver.getCab()
			        ))
			        .collect(Collectors.toList());

	  
}
//	   public Driver approveDriver(int driverId) {
//	        Driver driver = driverRepository.findById(driverId).orElseThrow();
//	        driver.setApprovalStatus(ApprovalStatus.APPROVED);
//	        return driverRepository.save(driver);
//	    }
//}


}
