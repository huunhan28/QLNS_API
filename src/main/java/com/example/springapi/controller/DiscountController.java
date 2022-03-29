package com.example.springapi.controller;

import java.util.List;

import com.example.springapi.models.Discount;
import com.example.springapi.repositories.DiscountResponsitory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path ="/api/v1/Discounts")
public class DiscountController {
    
    @Autowired
	DiscountResponsitory responsitory;

    @GetMapping("")
	List<Discount> getAllDiscounts(){
		return responsitory.findAll();
	}
}
