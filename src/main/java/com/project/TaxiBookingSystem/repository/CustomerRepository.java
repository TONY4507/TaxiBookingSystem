package com.project.TaxiBookingSystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;
import java.util.Optional;
import com.project.TaxiBookingSystem.entity.Customer;
import com.project.TaxiBookingSystem.enums.ApprovalStatus;

import java.util.List;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer> {

	   Optional<Customer> findByUsernameIgnoreCase(String username);
	    Optional<Customer> findByEmailIgnoreCase(String email);
	    Optional<Customer> findByMobileNumber(String phoneNumber);
   
	List<Customer> findByApprovalStatus(ApprovalStatus approvalStatus);
    List<Customer> findByCustomerId(int customerId);
    
    void deleteByEmail(String email);
   
}
