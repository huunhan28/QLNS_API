package com.example.springapi.controller;

import java.util.List;
import java.util.Optional;

import com.example.springapi.dto.OrderDTO;
import com.example.springapi.models.Discount;
import com.example.springapi.models.Orders;
import com.example.springapi.models.ResponseObject;
import com.example.springapi.repositories.DiscountResponsitory;
import com.example.springapi.repositories.OrderResponsitory;
import com.example.springapi.repositories.UserResponsitory;
import com.example.springapi.security.entity.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path ="/api/v1/Orders")
public class OrderController {
    
    @Autowired
	OrderResponsitory orderResponsitory;

    @Autowired
	UserResponsitory userResponsitory;

    @Autowired
	DiscountResponsitory discountResponsitory;
	
	@GetMapping("")
	List<Orders> getAllOrders(){
		return orderResponsitory.findAll();
	}

    @GetMapping("user/{userId}")
	List<Orders> getAllOrdersByUserId(@PathVariable int userId){
		return orderResponsitory.findAllByUserId(userId);
	}

    @GetMapping("/{id}")
	ResponseEntity<ResponseObject> getProduct(@PathVariable int id){
		Optional<Orders> findOrder = orderResponsitory.findById(id);
		if(findOrder.isPresent()) {
			return ResponseEntity.status(HttpStatus.OK).body(
					new ResponseObject("ok", "Query product sucessfully", findOrder));
		}else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
						new ResponseObject("failed", "Can not find order with id=" + id, ""));
		}
        
	}
    
    //insert new Order with POST method
    //Postman : Raw, JSON
    // @PostMapping("/insert")
    // ResponseEntity<ResponseObject> insertOrder(@RequestBody Orders newOrders) {
        
    //     return ResponseEntity.status(HttpStatus.OK).body(
    //        new ResponseObject("ok", "Insert Order successfully", orderResponsitory.save(newOrders))
    //     );
    // }

    @PostMapping("/insert")
    ResponseEntity<ResponseObject> insertOrder(@RequestBody OrderDTO newOrderDTO) {
        Optional<User> user=userResponsitory.findById(newOrderDTO.getUserId());
        Optional<Discount> discount=null;
        Orders order;
        if(newOrderDTO.getDiscountId()!=null){
             discount=discountResponsitory.findById(newOrderDTO.getDiscountId());

              order = new Orders(
                                user.get(),
                                newOrderDTO.getCreateAt(),
                                discount.get(),
                                newOrderDTO.getState());
        }else{
            order = new Orders(
                                user.get(),
                                newOrderDTO.getCreateAt(),
                                null,
                                newOrderDTO.getState());
        }
        
         
        return ResponseEntity.status(HttpStatus.OK).body(
           new ResponseObject("ok", "Insert Order successfully", orderResponsitory.save(order))
        );
    }
}
