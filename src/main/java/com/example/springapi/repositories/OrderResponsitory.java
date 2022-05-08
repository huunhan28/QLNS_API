package com.example.springapi.repositories;

import com.example.springapi.models.Orders;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderResponsitory extends JpaRepository<Orders, Integer>{
    List<Orders> findAllByState(String state);

	List<Orders> findAllByStateAndCreateAtBetween(String state, Date startDate, Date endDate);
}
