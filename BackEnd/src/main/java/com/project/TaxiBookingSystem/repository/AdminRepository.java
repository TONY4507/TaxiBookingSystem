package com.project.TaxiBookingSystem.repository;

import com.project.TaxiBookingSystem.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Integer> {
    Optional<Admin> findByEmailIgnoreCase(String email);
}
