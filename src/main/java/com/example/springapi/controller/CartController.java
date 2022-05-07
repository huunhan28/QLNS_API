package com.example.springapi.controller;

import java.util.List;
import java.util.Optional;

// import javax.transaction.Transactional;

import com.example.springapi.dto.CartDTO;
import com.example.springapi.models.Cart;
import com.example.springapi.models.CartKey;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Transactional
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
	List<Cart> getAllCarts(){
		return cartResponsitory.findAll();
	}

    @GetMapping("/product/{id}")
	ResponseEntity<ResponseObject> getCartFollowProduct(@PathVariable int id){
        Optional<Product> foundProduct = productResponsitory.findById(id);
        
		// Optional<Cart> foundCart = cartResponsitory.findByProduct(foundProduct.get());
		return null;
		// if(foundCart.isPresent()) {
		// 	return ResponseEntity.status(HttpStatus.OK).body(
		// 			new ResponseObject("ok", "Query cart sucessfully", foundCart));
		// }else {
		// 	return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
		// 				new ResponseObject("failed", "Can not find cart with id=" + id, ""));
		// }
        
	}

	@GetMapping("/user/{id}")
	List<Cart> getAllCartsFollowUser(@PathVariable int id){
		return cartResponsitory.findAllByUserId(id);
	}

	//insert new Cart with POST method
    //Postman : Raw, JSON
    @PostMapping("/insert")
    ResponseEntity<ResponseObject> insertOrderDetail(@RequestBody CartDTO newCartDTO) {
        
        Optional<User> user=userResponsitory.findById(newCartDTO.getUserId());
        Optional<Product> product=productResponsitory.findById(newCartDTO.getProductId());
        Cart newCart=new Cart(	new CartKey(user.get().getId(), product.get().getId()),
								user.get(),
								product.get(),
								newCartDTO.getQuantity());
        return ResponseEntity.status(HttpStatus.OK).body(
           new ResponseObject("ok", "Insert OrderDetail successfully", cartResponsitory.save(newCart))
        );
    }
	//update, upsert = update if found, otherwise insert
	@PutMapping("")
    ResponseEntity<ResponseObject> updateOrderDetail(@RequestBody CartDTO cartDTO) {
        // Cart updatedCart = cartResponsitory.findById(id)
        //         .map(category -> {
        //             category.setName(newCategory.getName());
        //             category.setDescription(newCategory.getDescription());
        //             return responsitory.save(category);
        //         }).orElseGet(() -> {
        //             newCategory.setId(id);
        //             return responsitory.save(newCategory);
        //         });
		cartResponsitory.updateCart(cartDTO.getUserId(), cartDTO.getProductId(), cartDTO.getQuantity());
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "Update Category successfully", cartDTO)
        );
    }

	//Delete a Cart  follow User => DELETE method
	

    @DeleteMapping("user/{id}")
    ResponseEntity<ResponseObject> deleteCartFollowUser(@PathVariable int id) {
		Optional<User> user=userResponsitory.findById(id);
        boolean exists = cartResponsitory.existsByUser( user.get());
        if(exists) {   
			cartResponsitory.removeUserCart(id);
            return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "Delete Cart successfully", "")
            );
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
            new ResponseObject("failed", "Cannot find user to delete cart", "")
        );
    }

	@DeleteMapping("user/{userId}/product/{productId}")
    ResponseEntity<ResponseObject> deleteCartFollowUserandProduct(@PathVariable int userId,
																	@PathVariable int productId) {
		// Optional<User> user=userResponsitory.findById(id);
        // boolean exists = cartResponsitory.existsByUser( user.get());
        // if(exists) {   
		// 	cartResponsitory.removeUserCart(id);
        //     return ResponseEntity.status(HttpStatus.OK).body(
        //         new ResponseObject("ok", "Delete Cart successfully", "")
        //     );
        // }
		cartResponsitory.removeUserProductCart(userId, productId);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
            new ResponseObject("failed", "Delete Cart successfully", "")
        );
    }
	
}
