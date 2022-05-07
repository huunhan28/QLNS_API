package com.example.springapi.controller;

import java.util.Optional;

import com.example.springapi.dto.OrderDetailDTO;
import com.example.springapi.models.Orders;
import com.example.springapi.models.OrderDetail;
import com.example.springapi.models.OrderDetailKey;
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
        Optional<Orders> order=orderResponsitory.findById(newOrderDetailDTO.getOrderId());
        System.out.println(order.get().getId());
        Optional<Product> product=productResponsitory.findById(newOrderDetailDTO.getProductId());
        OrderDetail newOrderDetail=new OrderDetail(new OrderDetailKey(
                                                                        order.get().getId(),
                                                                        product.get().getId()),
                                                    order.get(),
                                                    product.get(),
                                                    newOrderDetailDTO.getQuantity(),
                                                    newOrderDetailDTO.getPrice(),
                                                    newOrderDetailDTO.getDiscount());
        return ResponseEntity.status(HttpStatus.OK).body(
           new ResponseObject("ok", "Insert OrderDetail successfully", orderDetailResponsitory.save(newOrderDetail))
        );
    }

    // @PostMapping("/insert")
    // ResponseEntity<ResponseObject> insertOrderDetail2(@RequestBody OrderDetailDTO newOrderDetailDTO) {
        
    //     Optional<Orders> order=orderResponsitory.findById(newOrderDetailDTO.getOrderId());
    //     Optional<Product> product=productResponsitory.findById(newOrderDetailDTO.getProductId());
    //     Cart newCart=new Cart(	new CartKey(user.get().getId(), product.get().getId()),
    //                             user.get(),
	// 							product.get(),
	// 							newCartDTO.getQuantity());
    //     return ResponseEntity.status(HttpStatus.OK).body(
    //        new ResponseObject("ok", "Insert OrderDetail successfully", cartResponsitory.save(newCart))
    //     );
    // }
}
