package com.example.springapi.security.repository;

import java.lang.StackWalker.Option;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.springapi.security.entity.User;

public interface UserRepository extends JpaRepository<User, Integer>  {
	 /**
     * Find user by user name
     * @param username
     * @return User
     */
    Optional<User> findByUsername (String username);
    /**
     * Check exists an user by user name
     * @param username
     * @return Boolean
     */
    
    Boolean existsByUsername (String username);
    /**
     * Check exists an user email
     * @param email
     * @return Boolean
     */
    Boolean existsByEmail(String email);
    Optional<User> findByEmail(String email);
}
