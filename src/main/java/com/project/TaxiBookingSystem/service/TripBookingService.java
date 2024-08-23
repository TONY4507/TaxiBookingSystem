package com.project.TaxiBookingSystem.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.TaxiBookingSystem.entity.Cab;
import com.project.TaxiBookingSystem.entity.Customer;
import com.project.TaxiBookingSystem.entity.Driver;
import com.project.TaxiBookingSystem.entity.TripBooking;
import com.project.TaxiBookingSystem.enums.ApprovalStatus;
import com.project.TaxiBookingSystem.repository.CabRepository;
import com.project.TaxiBookingSystem.repository.CustomerRepository;
import com.project.TaxiBookingSystem.repository.DriverRepository;
import com.project.TaxiBookingSystem.repository.TripBookingRepository;

import jakarta.persistence.EntityNotFoundException;

import java.util.List;
import java.util.Optional;

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

    public TripBooking bookTrip(int customerId, int cabId, String fromLocation, String toLocation, float distanceInKm) {
        // Fetch customer details
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new EntityNotFoundException("Customer not found"));
        
        Cab cab = cabRepository.findById(cabId)
                .orElseThrow(() -> new EntityNotFoundException("Cab not found"));
        if (!cab.isAvailable()) {
            throw new RuntimeException("Selected cab is not available.");
        }
        // Find an available driver (example logic: first available driver)
      
        Driver driver = cab.getDriver();
        
        if (driver == null || driver.getApprovalStatus() != ApprovalStatus.APPROVED) {
            throw new RuntimeException("Driver is not available or not approved.");
        }
        
        // Create a new trip booking
        TripBooking tripBooking = new TripBooking();
        tripBooking.setCustomer(customer);
        tripBooking.setDriver(driver);
        tripBooking.setFromLocation(fromLocation);
        tripBooking.settoLocation(toLocation);
        tripBooking.setDistanceInKM(distanceInKm);

        // Calculate the bill based on the cab's per KM rate
        float bill = distanceInKm * driver.getCab().getPerKMRate();
        tripBooking.setBill(bill);

        // Set the trip status as pending (waiting for driver confirmation)
        tripBooking.setStatus(false);

        // Save the trip booking to the database
        return tripBookingRepository.save(tripBooking);
    }

//    // Method for a driver to confirm or reject a trip
//    public void confirmTrip(int tripBookingId, boolean accept) {
//        TripBooking tripBooking = tripBookingRepository.findById(tripBookingId)
//                .orElseThrow(() -> new EntityNotFoundException("Trip not found"));
//
//        if (!accept) {
//            // Handle trip rejection: Mark trip as canceled
//            tripBooking.setStatus(false); // Canceled
//        } else {
//            // Mark trip as confirmed
//            tripBooking.setStatus(true); // Confirmed
//        }
//
//        tripBookingRepository.save(tripBooking);
//    }

    // Method to cancel a booking by the customer
    public void cancelTrip(int tripBookingId, int customerId) {
        TripBooking tripBooking = tripBookingRepository.findById(tripBookingId)
                .orElseThrow(() -> new EntityNotFoundException("Trip not found"));

        if (tripBooking.getCustomer().getCustomerId() != customerId) {
            throw new RuntimeException("Unauthorized cancellation attempt.");
        }

        // Mark trip as canceled
        tripBooking.setStatus(false);
        tripBookingRepository.save(tripBooking);
    }

    // Method to view all available cabs (not currently in use)
    public List<Cab> viewAvailableCabs() {
        return cabRepository.findByIsAvailableTrue(); // Example method
    }

    // Method to view booking history for a customer
    public List<TripBooking> viewBookingHistory(int customerId) {
        return tripBookingRepository.findByDriverDriverId(customerId);
    }
}
