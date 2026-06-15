package com.project.TaxiBookingSystem.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.TaxiBookingSystem.entity.Cab;

@Repository
public interface CabRepository extends JpaRepository<Cab, String> {
    List<Cab> findByCarType(String carType);
    List<Cab> findByIsAvailableTrue();
}
