package com.example.springapi.uploadfile.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.springapi.uploadfile.model.FileDB;


@Repository
public interface FileDBRepository extends JpaRepository<FileDB, Integer> {
	
}
