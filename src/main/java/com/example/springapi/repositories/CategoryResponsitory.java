package com.example.springapi.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.springapi.models.Category;

public interface CategoryResponsitory extends JpaRepository<Category, Long>{

    List<Category> findByName(String name);

    Optional<Category> findById(int id);

	
}
