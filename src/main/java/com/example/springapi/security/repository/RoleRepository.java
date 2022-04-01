package com.example.springapi.security.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.springapi.security.common.ERole;
import com.example.springapi.security.entity.Role;
import com.example.springapi.security.entity.User;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
	
	Optional<Role> findByName(String name);
   
}