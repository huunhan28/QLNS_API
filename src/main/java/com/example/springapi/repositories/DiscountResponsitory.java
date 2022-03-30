package com.example.springapi.repositories;

import java.util.Optional;

import com.example.springapi.models.Discount;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DiscountResponsitory extends JpaRepository<Discount, String>{

    Optional<Discount> findById(String id);
    
}
