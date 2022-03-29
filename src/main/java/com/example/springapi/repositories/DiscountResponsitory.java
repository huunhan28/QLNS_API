package com.example.springapi.repositories;

import com.example.springapi.models.Discount;

import org.springframework.data.jpa.repository.JpaRepository;

public interface DiscountResponsitory extends JpaRepository<Discount, Long>{
    
}
