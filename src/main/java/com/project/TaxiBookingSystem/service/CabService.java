package com.project.TaxiBookingSystem.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import com.project.TaxiBookingSystem.entity.Cab;
import com.project.TaxiBookingSystem.repository.CabRepository;

@Service
public class CabService {
    @Autowired
    private CabRepository cabRepository;

    public Cab addCab(Cab cab) {
        return cabRepository.save(cab);
    }
    public List<Cab> viewAvailableCabs() {
        return cabRepository.findByIsAvailableTrue(); // Assuming this repository method exists
    }

    public Cab updateCab(int id, Cab cabDetails) {
        Cab cab = cabRepository.findById(id).orElseThrow();
        cab.setCarType(cabDetails.getCarType());
        cab.setPerKMRate(cabDetails.getPerKMRate());
        //cab.setIsAvailable(cabDetails.getIsAvailable());
        return cabRepository.save(cab);
    }
    	
    public Cab getCab(int id) {
    	
    	Optional<Cab> cab= cabRepository.findById(id);
    	 return cab.get();
    }
    public void deleteCab(int id) {
        cabRepository.deleteById(id);
    }
}
