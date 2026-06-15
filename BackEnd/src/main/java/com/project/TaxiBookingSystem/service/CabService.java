package com.project.TaxiBookingSystem.service;

import java.util.List;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import com.project.TaxiBookingSystem.entity.Cab;
import com.project.TaxiBookingSystem.exception.EntityNotFoundException;
import com.project.TaxiBookingSystem.repository.CabRepository;


@Service
public class CabService {
    @Autowired
    private CabRepository cabRepository;

    public Cab addCab(Cab cab) {
        return cabRepository.save(cab);
    }
    public List<Cab> viewAvailableCabs() {
        return cabRepository.findByIsAvailableTrue(); 
    }

    public Cab updateCab(String id, Cab cabDetails) {
        Cab cab = cabRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Cab not found with ID: " + id));
        cab.setCarType(cabDetails.getCarType());
        cab.setPerKMRate(cabDetails.getPerKMRate());
        //cab.setAvailable(cabDetails.get());
        return cabRepository.save(cab);
    }
    	
    public Cab getCab(String id) {
    	
    	Optional<Cab> cab= cabRepository.findById(id);
    	
        if (cab.isEmpty()) {
            // Throw a custom exception if the cab is not found
            throw new EntityNotFoundException("Cab not found with ID: " + id);
        }
    	 return cab.get();
    }
    public void deleteCab(String id) {
    	Optional<Cab> cab= cabRepository.findById(id);
    	
        if (cab.isEmpty()) {
           
            throw new EntityNotFoundException("Cab not found with ID: " + id);
        }
        cabRepository.deleteById(id);
    }
}
