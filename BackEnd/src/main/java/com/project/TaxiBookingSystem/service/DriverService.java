package com.project.TaxiBookingSystem.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.project.TaxiBookingSystem.dto.CabDTO;
import com.project.TaxiBookingSystem.dto.DriverDTO;
import com.project.TaxiBookingSystem.entity.Cab;
import com.project.TaxiBookingSystem.entity.Driver;
import com.project.TaxiBookingSystem.entity.TripBooking;
import com.project.TaxiBookingSystem.enums.ApprovalStatus;
import com.project.TaxiBookingSystem.repository.CabRepository;
import com.project.TaxiBookingSystem.repository.DriverRepository;
import com.project.TaxiBookingSystem.repository.TripBookingRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class DriverService {

    @Autowired
    private TripBookingRepository tripBookingRepository;

    @Autowired
    private DriverRepository driverRepository;

    @Autowired
    private CabRepository cabRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Driver login(String email, String password) {
        Optional<Driver> driverOpt = driverRepository.findByEmailIgnoreCase(email);
        Driver found = driverOpt.orElseThrow(() -> new EntityNotFoundException("Invalid email or password"));

        String stored = found.getPassword();
        boolean matches = false;
        if (stored != null && (stored.startsWith("$2a$") || stored.startsWith("$2b$") || stored.startsWith("$2y$"))) {
            matches = passwordEncoder.matches(password, stored);
        } else {
            matches = password.equals(stored);
            if (matches) {
                // migrate to BCrypt
                found.setPassword(passwordEncoder.encode(password));
                driverRepository.save(found);
            }
        }

        if (!matches) {
            throw new EntityNotFoundException("Invalid email or password");
        }

        if (found.getApprovalStatus() != ApprovalStatus.APPROVED) {
            throw new EntityNotFoundException("Driver is not approved yet");
        }

        return found;
    }

    public DriverDTO signup(Driver driver) {
        Optional<Driver> existingDriverOpt = driverRepository.findByUsernameIgnoreCase(driver.getUsername());
        Optional<Driver> existingDriverByEmail = driverRepository.findByEmailIgnoreCase(driver.getEmail());
        Optional<Driver> existingDriverByPhoneNumber = driverRepository.findByMobileNumber(driver.getMobileNumber());
        Optional<Driver> existingDriverByLicense = driverRepository.findBylicenseNumber(driver.getLicenseNumber());
        Optional<Cab> existingCabById = cabRepository.findById(driver.getCab().getCabNumber());

        if (existingDriverOpt.isPresent() || existingDriverByEmail.isPresent() || existingDriverByPhoneNumber.isPresent()
                || existingDriverByLicense.isPresent() || existingCabById.isPresent()) {
            throw new EntityNotFoundException("Driver with this username, email, mobile number, license number, or cab already exists.");
        }

        driver.setPassword(passwordEncoder.encode(driver.getPassword()));
        driver.setApprovalStatus(ApprovalStatus.PENDING);
        Driver savedDriver = driverRepository.save(driver);

        Cab savedCab = savedDriver.getCab();
        CabDTO cabDTO = new CabDTO(
            savedCab.getCabNumber(),
            savedCab.getCarType(),
            savedCab.getPerKMRate(),
            savedCab.isAvailable()
        );

        return new DriverDTO(
            savedDriver.getDriverId(),
            savedDriver.getUsername(),
            savedDriver.getPassword(),
            savedDriver.getAddress(),
            savedDriver.getMobileNumber(),
            savedDriver.getEmail(),
            savedDriver.getLicenseNumber(),
            cabDTO
        );
    }

    public void confirmTrip(int tripBookingId, int driverId, boolean accept) {
        TripBooking tripBooking = tripBookingRepository.findById(tripBookingId)
                .orElseThrow(() -> new EntityNotFoundException("Trip not found"));

        if (tripBooking.getDriver().getDriverId() != driverId) {
            throw new RuntimeException("Unauthorized operation. Trip not assigned to this driver.");
        }

        if (accept) {
            tripBooking.setbookingConfirm(true);
            tripBookingRepository.save(tripBooking);
        }
    }

    public List<DriverDTO> getPendingDrivers() {
        return driverRepository.findAll().stream()
                .filter(driver -> driver.getApprovalStatus() == ApprovalStatus.PENDING)
                .map(driver -> {
                    Cab cab = driver.getCab();
                    CabDTO cabDTO = new CabDTO(
                        cab.getCabNumber(),
                        cab.getCarType(),
                        cab.getPerKMRate(),
                        cab.isAvailable()
                    );

                    return new DriverDTO(
                        driver.getDriverId(),
                        driver.getUsername(),
                        driver.getPassword(),
                        driver.getAddress(),
                        driver.getMobileNumber(),
                        driver.getEmail(),
                        driver.getLicenseNumber(),
                        cabDTO
                    );
                })
                .collect(Collectors.toList());
    }

    public Driver approveDriver(int driverId) {
        Driver driver = driverRepository.findById(driverId)
                .orElseThrow(() -> new EntityNotFoundException("Driver not found"));
        driver.setApprovalStatus(ApprovalStatus.APPROVED);
        return driverRepository.save(driver);
    }
}
