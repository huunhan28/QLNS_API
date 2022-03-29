package com.example.springapi.repositories;

import com.example.springapi.models.Role;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleResponsitory extends JpaRepository<Role, Long> {
    
}
