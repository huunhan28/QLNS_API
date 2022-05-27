package com.example.springapi.repositories;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.springapi.models.Category;
import com.example.springapi.models.Product;
import com.example.springapi.models.ProductReport;

@Transactional
@Repository
public interface ProductResponsitory extends JpaRepository<Product, Integer> {

	Optional<Product> findByProductId(int id);

    List<Product> findByName(String name);

    

	List<Product> findFirst10ByOrderByName();
	
	List<Product> findProductsByName(String name);


    List<Product> findAllByCategory(Category category);

	long count();
    
   

}
