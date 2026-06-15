package com.project.TaxiBookingSystem.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.project.TaxiBookingSystem.entity.Cab;
import com.project.TaxiBookingSystem.entity.Customer;
import com.project.TaxiBookingSystem.entity.Driver;

@Repository
public interface DriverRepository extends JpaRepository<Driver, Integer> {
    Driver findByLicenseNumber(String licenseNumber);
   // Optional<Driver> findByIsAvailableTrue();
	Optional<Driver> findByEmailIgnoreCase(String Email);
	Optional<Driver> findByUsernameIgnoreCase(String username);
	Optional<Driver> findByMobileNumber(String mobileNumber);
	//Optional<Cab>findByCabCabNumber(String cabid);
	Optional<Driver> findBylicenseNumber(String licenseNumber);
}
