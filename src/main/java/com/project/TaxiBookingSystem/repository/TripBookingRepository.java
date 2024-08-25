package com.project.TaxiBookingSystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

import com.project.TaxiBookingSystem.entity.TripBooking;

import java.util.List;

@Repository
public interface TripBookingRepository extends JpaRepository<TripBooking, Integer> {
    List<TripBooking> findByCustomerEmail(String customerId);
    List<TripBooking> findByCustomerCustomerId(int customerId);
   // List<TripBooking> findByDriverId(int driverId);
    List<TripBooking> findByDriverDriverIdAndBookingConfirmTrue(int driverId);
    List<TripBooking> findByDriverDriverId(int driverId);
    
    //TripBooking findById(int driverId, boolean status);
}
