package com.project.TaxiBookingSystem.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.TaxiBookingSystem.entity.Admin;
import com.project.TaxiBookingSystem.entity.Customer;
import com.project.TaxiBookingSystem.entity.Driver;
import com.project.TaxiBookingSystem.enums.ApprovalStatus;
import com.project.TaxiBookingSystem.repository.AdminRepository;
import com.project.TaxiBookingSystem.repository.CustomerRepository;
import com.project.TaxiBookingSystem.repository.DriverRepository;

@Service
public class AdminService {

    @Autowired
    private AdminRepository adminRepository;

        @Autowired
        private CustomerRepository customerRepository;

        @Autowired
        private DriverRepository driverRepository;

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
            customerRepository.save(customer); // Save the updated customer
        }

        public void rejectDriver(int driverId) {
            Driver driver = driverRepository.findById(driverId)
                    .orElseThrow(() -> new RuntimeException("Driver not found"));

            driver.setApprovalStatus(ApprovalStatus.REJECTED);
            driverRepository.save(driver); // Save the updated driver
        }
    }

  

