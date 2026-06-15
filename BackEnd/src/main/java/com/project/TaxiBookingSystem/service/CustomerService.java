package com.project.TaxiBookingSystem.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.project.TaxiBookingSystem.dto.CustomerDTO;
import com.project.TaxiBookingSystem.entity.Cab;
import com.project.TaxiBookingSystem.entity.Customer;
import com.project.TaxiBookingSystem.enums.ApprovalStatus;
import com.project.TaxiBookingSystem.repository.CabRepository;
import com.project.TaxiBookingSystem.repository.CustomerRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
public class CustomerService {
    @Autowired
    private CustomerRepository customerRepository;
    
    @Autowired
    private CabRepository cabRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Customer login(String email, String password) {
        Optional<Customer> customerOpt = customerRepository.findByEmailIgnoreCase(email);
        Customer found = customerOpt.orElseThrow(() -> new EntityNotFoundException("Invalid email or password"));

        String stored = found.getPassword();
        boolean matches = false;
        if (stored != null && (stored.startsWith("$2a$") || stored.startsWith("$2b$") || stored.startsWith("$2y$"))) {
            matches = passwordEncoder.matches(password, stored);
        } else {
            // Stored password doesn't look like BCrypt — support legacy plaintext passwords by checking equality
            matches = password.equals(stored);
            if (matches) {
                // migrate to BCrypt for future logins
                found.setPassword(passwordEncoder.encode(password));
                customerRepository.save(found);
            }
        }

        if (!matches) {
            throw new EntityNotFoundException("Invalid email or password");
        }

        if (found.getApprovalStatus() != ApprovalStatus.APPROVED) {
            throw new EntityNotFoundException("Customer is not approved yet");
        }

        return found;
    }
    
    public CustomerDTO signup(Customer customer) {
         Optional<Customer> existingCustomerOpt = customerRepository.findByUsernameIgnoreCase(customer.getUsername());
         Optional<Customer> existingCustByemail = customerRepository.findByEmailIgnoreCase(customer.getEmail());
         Optional<Customer> existingCustomerByPhoneNumber = customerRepository.findByMobileNumber(customer.getMobileNumber());

            if (existingCustomerOpt.isPresent() || existingCustByemail.isPresent() || existingCustomerByPhoneNumber.isPresent()) {
                throw new EntityNotFoundException("Customer name, email or phone already exists");
            }

            customer.setPassword(passwordEncoder.encode(customer.getPassword()));
            customer.setApprovalStatus(ApprovalStatus.PENDING);
            Customer savedCustomer = customerRepository.save(customer);

            return new CustomerDTO(
                    savedCustomer.getCustomerId(),
                    savedCustomer.getUsername(),
                    savedCustomer.getPassword(),
                    savedCustomer.getAddress(),
                    savedCustomer.getMobileNumber(),
                    savedCustomer.getEmail()
            );
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
        } else {
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

