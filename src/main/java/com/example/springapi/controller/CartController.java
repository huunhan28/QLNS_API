package com.example.springapi.controller;

import java.util.List;
import java.util.Optional;

import com.example.springapi.dto.CartDTO;
import com.example.springapi.models.Cart;
import com.example.springapi.models.Discount;
import com.example.springapi.models.Product;
import com.example.springapi.models.ResponseObject;
import com.example.springapi.repositories.CartResponsitory;
import com.example.springapi.repositories.ProductResponsitory;
import com.example.springapi.repositories.UserResponsitory;
import com.example.springapi.security.entity.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path ="/api/v1/Carts")
public class CartController {
    @Autowired
	CartResponsitory cartResponsitory;
	
    @Autowired
	ProductResponsitory productResponsitory;

	@Autowired
	UserResponsitory userResponsitory;

	@GetMapping("")
	@PreAuthorize("hasRole('USER') or hasRole ('ADMIN')")
	List<Cart> getAllCarts(){
		return cartResponsitory.findAll();
	}

    @GetMapping("/product/{id}")
	ResponseEntity<ResponseObject> getCart(@PathVariable Long id){
        Optional<Product> foundProduct = productResponsitory.findById(id);
        
		Optional<Cart> foundCart = cartResponsitory.findByProduct(foundProduct.get());
		if(foundCart.isPresent()) {
			return ResponseEntity.status(HttpStatus.OK).body(
					new ResponseObject("ok", "Query cart sucessfully", foundCart));
		}else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
						new ResponseObject("failed", "Can not find cart with id=" + id, ""));
		}
        
	}

	//insert new Cart with POST method
    //Postman : Raw, JSON
    @PostMapping("/insert")
    ResponseEntity<ResponseObject> insertOrderDetail(@RequestBody CartDTO newCartDTO) {
        
        User user=userResponsitory.getById(newCartDTO.getUserId());
        Product product=productResponsitory.getById(newCartDTO.getProductId());
        Cart newCart=new Cart(
								user,
								product,
								newCartDTO.getQuantity());
        return ResponseEntity.status(HttpStatus.OK).body(
           new ResponseObject("ok", "Insert OrderDetail successfully", cartResponsitory.save(newCart))
        );
    }


	// //Delete a Cart  follow User => DELETE method
    // @DeleteMapping("/{id}")
    // ResponseEntity<ResponseObject> deleteCartFollowUser(@PathVariable Long id) {
    //     boolean exists = userResponsitory.existsById( id);
    //     if(exists) {
	// 		while(userResponsitory.existsByUserId( id)){
	// 			cartResponsitory.deleteByUserId( id);
	// 		}    
    //         return ResponseEntity.status(HttpStatus.OK).body(
    //             new ResponseObject("ok", "Delete Cart successfully", "")
    //         );
    //     }
    //     return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
    //         new ResponseObject("failed", "Cannot find user to delete cart", "")
    //     );
    // }
	
}
