package com.example.springapi.repositories;

import com.example.springapi.models.Orders;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderResponsitory extends JpaRepository<Orders, Integer>{
    
}
