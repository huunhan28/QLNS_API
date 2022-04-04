package com.example.springapi.repositories;

import java.util.List;
import java.util.Optional;

import com.example.springapi.models.Cart;
import com.example.springapi.models.Product;
import com.example.springapi.security.entity.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface CartResponsitory extends JpaRepository<Cart, Integer>{

    Optional<Cart> findByProduct(Product product);

    boolean existsByUserId(Integer id);
    
    void deleteByUserId(Integer id);

    List<Cart> findAllByUserId(Integer id);

    
}
