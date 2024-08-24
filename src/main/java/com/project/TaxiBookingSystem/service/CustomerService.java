package com.project.TaxiBookingSystem.service;

import java.util.List;
import java.util.stream.Collectors;  

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.TaxiBookingSystem.dto.CustomerDTO;
import com.project.TaxiBookingSystem.entity.Cab;
import com.project.TaxiBookingSystem.entity.Customer;
import com.project.TaxiBookingSystem.enums.ApprovalStatus;
import com.project.TaxiBookingSystem.repository.CabRepository;
import com.project.TaxiBookingSystem.repository.CustomerRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class CustomerService {
    @Autowired
    private CustomerRepository customerRepository;
    
    private CabRepository cabRepository;

    public Customer login(String email, String password) {
        
    	Optional<Customer> customer=customerRepository.findByEmail(email)
                                  .filter(c -> c.getPassword().equals(password));
    	if (customer.isEmpty()) {
    		 throw new EntityNotFoundException("Customer not found " );
    	}
    	
    	return customer.get();
    }
    
    public Customer signup(Customer customer) {
        // You can add validation logic here (e.g., check if email already exists)
        return customerRepository.save(customer);
    }
    
    public Customer updateProfile(int customerId, Customer updatedCustomer) {
        Optional<Customer> existingCustomerOpt = customerRepository.findById(customerId);
        if (existingCustomerOpt.isPresent()) {
            Customer existingCustomer = existingCustomerOpt.get();
            existingCustomer.setAddress(updatedCustomer.getAddress());
            existingCustomer.setEmail(updatedCustomer.getEmail());
            existingCustomer.setMobileNumber(updatedCustomer.getMobileNumber());
            existingCustomer.setUsername(updatedCustomer.getUsername());
       

            return customerRepository.save(existingCustomer);
        }       
      throw new EntityNotFoundException("Customer not found with id: " + customerId);
    }
      public List<Cab> viewAvailableCabs() {
          return cabRepository.findByIsAvailableTrue();
      }

      public List<CustomerDTO> getPendingCustomers() {
    	  return customerRepository.findByApprovalStatus(ApprovalStatus.PENDING)
                  .stream()
                  .map(customer -> new CustomerDTO(
                      customer.getCustomerId(),
                      customer.getUsername(),
                      customer.getPassword(),
                      customer.getAddress(),
                      customer.getMobileNumber(),
                      customer.getEmail()
                  ))
                  .collect(Collectors.toList());
}
      
      }
    
    
    
    
    
    
    

    // Other customer-related logic

