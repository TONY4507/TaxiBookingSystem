package com.project.TaxiBookingSystem.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.stereotype.Service;

import com.project.TaxiBookingSystem.entity.Admin;
import com.project.TaxiBookingSystem.entity.Customer;
import com.project.TaxiBookingSystem.entity.Driver;
import com.project.TaxiBookingSystem.enums.ApprovalStatus;

import com.project.TaxiBookingSystem.repository.AdminRepository;
import com.project.TaxiBookingSystem.repository.CustomerRepository;
import com.project.TaxiBookingSystem.repository.DriverRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class AdminService {

 
        @Autowired
        private AdminRepository adminRepository;

        @Autowired
        private PasswordEncoder passwordEncoder;

        @Autowired
        private CustomerRepository customerRepository;

        @Autowired
        private DriverRepository driverRepository;

        public Admin login(String email, String password) {
            Admin admin = adminRepository.findByEmailIgnoreCase(email)
                    .orElseThrow(() -> new EntityNotFoundException("Invalid admin email or password"));

            String stored = admin.getPassword();
            boolean matches = false;
            if (stored != null && (stored.startsWith("$2a$") || stored.startsWith("$2b$") || stored.startsWith("$2y$"))) {
                matches = passwordEncoder.matches(password, stored);
            } else {
                matches = password.equals(stored);
                if (matches) {
                    admin.setPassword(passwordEncoder.encode(password));
                    adminRepository.save(admin);
                }
            }

            if (!matches) {
                throw new EntityNotFoundException("Invalid admin email or password");
            }

            return admin;
        }

        public void approveCustomer(int customerId) {
            Customer customer = customerRepository.findById(customerId)
                    .orElseThrow(() -> new RuntimeException("Customer not found"));

            if (customer.getApprovalStatus() == ApprovalStatus.PENDING) {
                customer.setApprovalStatus(ApprovalStatus.APPROVED);
                customerRepository.save(customer); // Save the updated customer
            } else {
                throw new RuntimeException("Customer is already " + customer.getApprovalStatus());
            }
        }

        public void approveDriver(int driverId) {
            Driver driver = driverRepository.findById(driverId)
                    .orElseThrow(() -> new RuntimeException("Driver not found"));

            if (driver.getApprovalStatus() == ApprovalStatus.PENDING) {
                driver.setApprovalStatus(ApprovalStatus.APPROVED);
                driverRepository.save(driver); // Save the updated driver
            } else {
                throw new RuntimeException("Driver is already " + driver.getApprovalStatus());
            }
        }

        public void rejectCustomer(int customerId) {
            Customer customer = customerRepository.findById(customerId)
                    .orElseThrow(() -> new RuntimeException("Customer not found"));

            customer.setApprovalStatus(ApprovalStatus.REJECTED);
            customerRepository.save(customer);
        }

        public void rejectDriver(int driverId) {
            Driver driver = driverRepository.findById(driverId)
                    .orElseThrow(() -> new RuntimeException("Driver not found"));

            driver.setApprovalStatus(ApprovalStatus.REJECTED);
            driverRepository.save(driver);
        }
    }

  

