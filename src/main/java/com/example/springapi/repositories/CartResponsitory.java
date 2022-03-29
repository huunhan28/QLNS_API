package com.example.springapi.repositories;

import java.util.Optional;

import com.example.springapi.models.Cart;
import com.example.springapi.models.Product;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CartResponsitory extends JpaRepository<Cart, Long>{

    Optional<Cart> findByProduct(Product product);
    
}
