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
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.Min;

@Service
public class CustomerService {
    @Autowired
    private CustomerRepository customerRepository;
    
    private CabRepository cabRepository;

    public Customer login(String email, String password) {
        
    	Optional<Customer> customer=customerRepository.findByEmailIgnoreCase(email)
                                  .filter(c -> c.getPassword().equals(password));
    	if (customer.isEmpty()) {
    		 throw new EntityNotFoundException("Customer not found " );
    	}
    	
    	return customer.get();
    }
    
    public CustomerDTO signup(Customer customer) {
    	 Optional<Customer> existingCustomerOpt = customerRepository.findByUsernameIgnoreCase(customer.getUsername());
    	 Optional<Customer> existingCustByemail = customerRepository.findByEmailIgnoreCase(customer.getEmail());
    	 Optional<Customer> existingCustomerByPhoneNumber = customerRepository.findByMobileNumber(customer.getMobileNumber());

    	    // If any of these exist, throw an exception
    	    if (existingCustomerOpt.isPresent() || existingCustByemail.isPresent() || existingCustomerByPhoneNumber.isPresent()) {
        throw new EntityNotFoundException("Customer Name or Email already Exist");
         }
         else {
        	 customerRepository.save(customer);
        	 
        	 
             CustomerDTO customerDTO = new CustomerDTO(
            		 customer.getCustomerId(),
            		 customer.getUsername(),
            		 customer.getPassword(),
            		 customer.getAddress(),
            		 customer.getMobileNumber(),
            		 customer.getEmail()
             );

             return customerDTO;
         }
        	 
         
    }
    
    public Customer updateProfile(int customerId, Customer updatedCustomer) {
    	 Optional<Customer> existingCustomerOpt = customerRepository.findById(customerId);
    	
    	    if (!existingCustomerOpt.isPresent() ) {
    	    	 Customer existingCustomer = existingCustomerOpt.get();
    	            existingCustomer.setAddress(updatedCustomer.getAddress());
    	            existingCustomer.setEmail(updatedCustomer.getEmail());
    	            existingCustomer.setMobileNumber(updatedCustomer.getMobileNumber());
    	            existingCustomer.setUsername(updatedCustomer.getUsername());
    	            return customerRepository.save(existingCustomer);
         }
         else {
        	 throw new EntityNotFoundException("Customer Not Found " + updatedCustomer);
         }
    	
    	
        
     
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
      
      @Transactional
      public String deleteCustomer(String email) {
      	 Optional<Customer> existingCustomerOpt = customerRepository.findByEmailIgnoreCase(email);
      
           if (existingCustomerOpt.isPresent()) {
        	   customerRepository.deleteByEmail(email);
        	   return("Customer delete success");
           }
           else {
        	   throw new EntityNotFoundException("Customer Not Found");
           }
      
      }
}
    
    
    
    
    
    

    // Other customer-related logic

