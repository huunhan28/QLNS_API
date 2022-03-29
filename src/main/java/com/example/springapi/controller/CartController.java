package com.example.springapi.controller;

import java.util.List;
import java.util.Optional;

import com.example.springapi.models.Cart;
import com.example.springapi.models.Product;
import com.example.springapi.models.ResponseObject;
import com.example.springapi.repositories.CartResponsitory;
import com.example.springapi.repositories.ProductResponsitory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path ="/api/v1/Carts")
public class CartController {
    @Autowired
	CartResponsitory cartResponsitory;
	
    @Autowired
	ProductResponsitory productResponsitory;

	@GetMapping("")
	List<Cart> getAllCarts(){
		return cartResponsitory.findAll();
	}

    @GetMapping("/product/{id}")
	ResponseEntity<ResponseObject> getCart(@PathVariable Long id){
        Optional<Product> foundProduct = productResponsitory.findById(id);
        
		Optional<Cart> foundCart = cartResponsitory.findByProduct(foundProduct.get());
		if(foundCart.isPresent()) {
			return ResponseEntity.status(HttpStatus.OK).body(
					new ResponseObject("ok!", "Query cart sucessfully", foundCart));
		}else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
						new ResponseObject("failed!", "Can not find cart with id=" + id, ""));
		}
        
	}
}
