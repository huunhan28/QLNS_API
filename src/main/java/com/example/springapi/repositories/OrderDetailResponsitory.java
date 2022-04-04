package com.example.springapi.repositories;

import com.example.springapi.models.OrderDetail;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderDetailResponsitory extends JpaRepository<OrderDetail, Integer>{
    
}
