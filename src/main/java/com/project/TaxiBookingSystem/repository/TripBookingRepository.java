package com.project.TaxiBookingSystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import com.project.TaxiBookingSystem.entity.TripBooking;

import java.util.List;

@Repository
public interface TripBookingRepository extends JpaRepository<TripBooking, Integer> {
    List<TripBooking> findByCustomerCustomerId(int customerId);
   // List<TripBooking> findByDriverId(int driverId);
    // Find all trips by driver ID
    List<TripBooking> findByDriverDriverId(int driverId);
    
    // Find an active trip for a driver (assuming the status is used to mark active trips)
    TripBooking findByDriverDriverIdAndStatus(int driverId, boolean status);
}
