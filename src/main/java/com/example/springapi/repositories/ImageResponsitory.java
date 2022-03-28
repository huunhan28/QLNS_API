package com.example.springapi.repositories;

import java.util.List;

import com.example.springapi.models.Image;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageResponsitory extends JpaRepository<Image, Long> {

    List<Image> findByLink(String link);
    
}
