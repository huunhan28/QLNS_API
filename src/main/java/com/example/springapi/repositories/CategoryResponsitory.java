package com.example.springapi.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.springapi.models.Category;

@Repository
public interface CategoryResponsitory extends JpaRepository<Category, Long>{

    List<Category> findByName(String name);

    Optional<Category> findById(int id);

	
}
