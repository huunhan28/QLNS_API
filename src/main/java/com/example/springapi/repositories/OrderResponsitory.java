package com.example.springapi.repositories;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import com.example.springapi.dto.OrderWithProducts;
import com.example.springapi.models.Orders;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface OrderResponsitory extends JpaRepository<Orders, Integer>{

    List<Orders> findAllByState(String state);
	List<Orders> findAllByStateAndCreateAtBetweenOrderByIdDesc(String state, Date startDate, Date endDate);
    List<Orders> findAllByUserIdOrderByIdDesc(int userId);
    Optional<Orders> findTopByOrderByIdDesc();
    

//    @Query(value = "select new com.example.springapi.dto.OrderWithProducts(a.id,"
//    		+ " a.orderDetails[0].getProduct().getName(), a.orderDetails[0].getQuantity(), a.orderDetails.getPrice(), a.orderDetails.getDiscount()) "
//    		+ " from Orders a where a.id= :id inner join a.id")
//    List<OrderWithProducts> findOrderWithProducts(@Param("id") int id);
    
}
