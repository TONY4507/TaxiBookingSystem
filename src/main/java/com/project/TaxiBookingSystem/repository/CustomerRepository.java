package com.project.TaxiBookingSystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import com.project.TaxiBookingSystem.entity.Customer;

import java.util.List;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer> {
    // Find a customer by their email for login
	Optional<Customer> findByEmail(String email);
	Optional<Customer> findByUsername(String username);
    //Customer addcustomer(Customer cust);
    // Find all bookings made by a customer
    List<Customer> findByCustomerId(int customerId);

    // Additional query methods can be defined here as needed
}
