package com.project.TaxiBookingSystem.service;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import com.project.TaxiBookingSystem.entity.Cab;
import com.project.TaxiBookingSystem.entity.Customer;
import com.project.TaxiBookingSystem.entity.Driver;
import com.project.TaxiBookingSystem.entity.TripBooking;
import com.project.TaxiBookingSystem.enums.ApprovalStatus;
import com.project.TaxiBookingSystem.exception.EntityNotFoundException;
import com.project.TaxiBookingSystem.repository.CabRepository;
import com.project.TaxiBookingSystem.repository.CustomerRepository;
import com.project.TaxiBookingSystem.repository.DriverRepository;
import com.project.TaxiBookingSystem.repository.TripBookingRepository;

import jakarta.validation.constraints.NotEmpty;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;  
@Service
public class TripBookingService {

    @Autowired
    private TripBookingRepository tripBookingRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private DriverRepository driverRepository;

    @Autowired
    private CabRepository cabRepository;

    public TripBooking bookTrip(int customerId, String cabId, String fromLocation, String toLocation, LocalDateTime toDateTime, float distanceInKm) {

        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new EntityNotFoundException("Customer not found"));
        
        Cab cab = cabRepository.findById(cabId)
                .orElseThrow(() -> new EntityNotFoundException("Cab not found"));
        if (!cab.isAvailable()) {
            throw new EntityNotFoundException("Selected cab is not available.");
        }
      
        Driver driver = cab.getDriver();
        
        if (driver == null || driver.getApprovalStatus() != ApprovalStatus.APPROVED) {
            throw new EntityNotFoundException("Driver is not available or not approved.");
        }
        
        TripBooking tripBooking = new TripBooking();
        tripBooking.setCustomer(customer);
        tripBooking.setDriver(driver);
        tripBooking.setFromLocation(fromLocation);
        tripBooking.settoLocation(toLocation);
        tripBooking.setToDateTime(toDateTime);
        tripBooking.setDistanceInKM(distanceInKm);
        


        float bill = distanceInKm * driver.getCab().getPerKMRate();
        tripBooking.setBill(bill);

        return tripBookingRepository.save(tripBooking);
    }


    public boolean  confirmTrip(int tripBookingId,int driverId, boolean accept) {
        TripBooking tripBooking = tripBookingRepository.findById(tripBookingId)
                .orElseThrow(() -> new EntityNotFoundException("Trip not found"));
    
            
        if (tripBooking.getDriver().getDriverId()==driverId) {
                tripBooking.setbookingConfirm(accept);
                    
                    tripBooking.getDriver().getCab().setAvailable(false);
               
                tripBookingRepository.save(tripBooking);    
                return true;
            }
            return false;
    }
       

    public void cancelTrip(int tripBookingId, int customerId) {
//     System.out.println("tripID---"+tripBookingId);
//     System.out.println("custID---"+customerId);
    	
    	TripBooking tripBooking = tripBookingRepository.findById(tripBookingId)
                .orElseThrow(() -> new EntityNotFoundException("Trip not found"));

//System.out.println("databse cust"+tripBooking.getCustomer().getCustomerId());
//System.out.println("input"+customerId);
    	if (tripBooking.getCustomer() == null || 
    		    tripBooking.getCustomer().getCustomerId() != customerId) {
    		    throw new EntityNotFoundException("Unauthorized cancellation attempt or Customer Not Found.");
    		}
   
            LocalDateTime toDateTime = tripBooking.getToDateTime();
            LocalDateTime currentTime = LocalDateTime.now();

        
            if (toDateTime != null && toDateTime.isAfter(currentTime)) {
            	tripBooking.getDriver().getCab().setAvailable(true);
            	tripBookingRepository.deleteById(tripBookingId);
            }
            
        }
    

    	public List<TripBooking> getConfirmedTripsByDriverId(int driverId) {
    		
            Driver driver = driverRepository.findById(driverId)
                    .orElseThrow(() -> new EntityNotFoundException("Driver not found"));

            if (driver.getApprovalStatus() != ApprovalStatus.APPROVED) {
                throw new IllegalArgumentException("Driver is not approved");
            }

            // Retrieve confirmed trips for the driver
            return tripBookingRepository.findByDriverDriverIdAndBookingConfirmTrue(driverId);
        }
    		
    		
    		
    		
     

      
   
 
    public List<TripBooking> viewBookingHistory(int customerId) {
  
    	 Optional<Customer> existingCustByemail = customerRepository.findById(customerId);
    
    	    if (existingCustByemail.isPresent() ) {
    	    
    	    	return tripBookingRepository.findByCustomerCustomerId(customerId);
	
         }
         else {
        	 throw new EntityNotFoundException("Customer ID Not Exist");
        
    }
    
    }
    
    public List<TripBooking> getAvailableRides(int driverId) {
        // Check if the driver exists in the database
        Driver driver = driverRepository.findById(driverId)
                .orElseThrow(() -> new EntityNotFoundException("Driver not found"));

        // Check if the driver's cab is available
        if (!driver.getCab().isAvailable()) {
            throw new IllegalArgumentException("Driver's cab is not available");
        }

        // Retrieve and filter trip bookings
        return tripBookingRepository.findByDriverDriverId(driverId).stream()
                .filter(tripBooking -> tripBooking.getDriver().getCab().isAvailable() && !tripBooking.isbookingConfirm())
                .collect(Collectors.toList());
    }

   
}
