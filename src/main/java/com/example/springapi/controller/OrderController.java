package com.example.springapi.controller;

import java.util.List;

import com.example.springapi.models.Order;
import com.example.springapi.models.ResponseObject;
import com.example.springapi.repositories.OrderResponsitory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path ="/api/v1/Orders")
public class OrderController {
    
    @Autowired
	OrderResponsitory orderResponsitory;
	
	@GetMapping("")
	List<Order> getAllOrders(){
		return orderResponsitory.findAll();
	}
    
    //insert new Order with POST method
    //Postman : Raw, JSON
    @PostMapping("/insert")
    ResponseEntity<ResponseObject> insertOrder(@RequestBody Order newOrder) {
        
        return ResponseEntity.status(HttpStatus.OK).body(
           new ResponseObject("ok", "Insert Order successfully", orderResponsitory.save(newOrder))
        );
    }
}
