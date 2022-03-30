package com.example.springapi.controller;

import com.example.springapi.dto.OrderDetailDTO;
import com.example.springapi.models.Order;
import com.example.springapi.models.OrderDetail;
import com.example.springapi.models.Product;
import com.example.springapi.models.ResponseObject;
import com.example.springapi.repositories.OrderDetailResponsitory;
import com.example.springapi.repositories.OrderResponsitory;
import com.example.springapi.repositories.ProductResponsitory;
import com.example.springapi.repositories.UserResponsitory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path ="/api/v1/OrderDetails")
public class OrderDetailController {

    @Autowired
	OrderDetailResponsitory orderDetailResponsitory;

    @Autowired
	OrderResponsitory orderResponsitory;

    @Autowired
    ProductResponsitory productResponsitory;

    //insert new OrderDetail with POST method
    //Postman : Raw, JSON
    @PostMapping("/insert")
    ResponseEntity<ResponseObject> insertOrderDetail(@RequestBody OrderDetailDTO newOrderDetailDTO) {
        
        Order order=orderResponsitory.getById(newOrderDetailDTO.getOrderId());
        Product product=productResponsitory.getById(newOrderDetailDTO.getProductId());
        OrderDetail newOrderDetail=new OrderDetail(
                                                    order,
                                                    product,
                                                    newOrderDetailDTO.getQuantity(),
                                                    newOrderDetailDTO.getPrice(),
                                                    newOrderDetailDTO.getDiscount());
        return ResponseEntity.status(HttpStatus.OK).body(
           new ResponseObject("ok", "Insert OrderDetail successfully", orderDetailResponsitory.save(newOrderDetail))
        );
    }
}
