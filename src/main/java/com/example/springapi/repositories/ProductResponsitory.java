package com.example.springapi.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.springapi.models.Product;
@Repository
public interface ProductResponsitory extends JpaRepository<Product, Long> {

	Optional<Product> findByProductId(int id);

    List<Product> findByName(String name);
    

	List<Product> findFirst10ByOrderByName();
	
	List<Product> findProductsByName(String name);
	
}
