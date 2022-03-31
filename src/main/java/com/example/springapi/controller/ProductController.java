package com.example.springapi.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.springapi.models.Product;
import com.example.springapi.models.ResponseObject;
import com.example.springapi.repositories.ProductResponsitory;

@RestController
@RequestMapping(path ="/api/v1/Products")
public class ProductController {
	
	
    @Autowired
	ProductResponsitory responsitory;
	
	@GetMapping("")
	List<Product> getAllProducts(){
		return responsitory.findAll();
	}
	
	@GetMapping("/{id}")
	ResponseEntity<ResponseObject> getProduct(@PathVariable Long id){
		Optional<Product> foundProduct = responsitory.findById(id);
		if(foundProduct.isPresent()) {
			return ResponseEntity.status(HttpStatus.OK).body(
					new ResponseObject("ok", "Query product sucessfully", foundProduct));
		}else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
						new ResponseObject("failed", "Can not find product with id=" + id, ""));
		}
        
	}
	//insert new Product with POST method
    //Postman : Raw, JSON
    @PostMapping("/insert")
    ResponseEntity<ResponseObject> insertProduct(@RequestBody Product newProduct) {
        //2 products must not have the same name !
        List<Product> foundProducts = responsitory.findByName(newProduct.getName().trim());
        
        if(foundProducts.size() > 0) {
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                new ResponseObject("failed", "Product name already taken", "")
            );
        }
        return ResponseEntity.status(HttpStatus.OK).body(
           new ResponseObject("ok", "Insert Product successfully", responsitory.save(newProduct))
        );
    }
	//update, upsert = update if found, otherwise insert
    @PutMapping("/{id}")
    ResponseEntity<ResponseObject> updateProduct(@RequestBody Product newProduct, @PathVariable Long id) {
        Product updatedProduct = responsitory.findById(id)
                .map(product -> {
                    product.setCategory(newProduct.getCategory());
					product.setName(newProduct.getName());
					product.setPrice(newProduct.getPrice());
					product.setCalculationUnit(newProduct.getCalculationUnit());
					product.setTotal(newProduct.getTotal());
					product.setDescription(newProduct.getDescription());
					product.setSlug(newProduct.getSlug());
					product.setDisplay(newProduct.isDisplay());
					product.setRate(newProduct.getRate());
					product.setDiscount(newProduct.getDiscount());
					product.setId(newProduct.getId());
					product.setUrl(newProduct.getUrl());
					product.setYear(newProduct.getYear());
                    return responsitory.save(product);
                }).orElseGet(() -> {
                    newProduct.setProductId((long)id);
                    return responsitory.save(newProduct);
                });
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "Update Product successfully", updatedProduct)
        );
    }
	//Delete a Product => DELETE method
    @DeleteMapping("/{id}")
    ResponseEntity<ResponseObject> deleteProduct(@PathVariable Long id) {
        boolean exists = responsitory.existsById( id);
        if(exists) {
            responsitory.deleteById( id);
            return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "Delete Product successfully", "")
            );
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
            new ResponseObject("failed", "Cannot find Product to delete", "")
        );
    }
	

}
