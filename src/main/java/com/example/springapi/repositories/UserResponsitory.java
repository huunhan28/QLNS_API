package com.example.springapi.repositories;

import com.example.springapi.security.entity.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserResponsitory extends JpaRepository<User, Integer>{
    
}
