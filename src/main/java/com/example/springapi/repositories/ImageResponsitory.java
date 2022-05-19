package com.example.springapi.repositories;

import java.util.List;

import com.example.springapi.models.Image;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageResponsitory extends JpaRepository<Image, Integer> {

    List<Image> findByLink(String link);
    
}
