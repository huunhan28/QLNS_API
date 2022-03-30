package com.example.springapi.controller;

import java.util.List;

import com.example.springapi.models.Discount;
import com.example.springapi.models.ResponseObject;
import com.example.springapi.repositories.DiscountResponsitory;

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

@RestController
@RequestMapping(path ="/api/v1/Discounts")
public class DiscountController {
    
    @Autowired
	DiscountResponsitory responsitory;

    @GetMapping("")
	List<Discount> getAllDiscounts(){
		return responsitory.findAll();
	}

	//insert new Discount with POST method
    //Postman : Raw, JSON
    @PostMapping("/insert")
    ResponseEntity<ResponseObject> insertDiscount(@RequestBody Discount newDiscount) {
        return ResponseEntity.status(HttpStatus.OK).body(
           new ResponseObject("ok", "Insert Discount successfully", responsitory.save(newDiscount))
        );
    }

	//update, upsert = update if found, otherwise insert
    @PutMapping("/{id}")
    ResponseEntity<ResponseObject> updateDiscount(@RequestBody Discount newDiscount, @PathVariable String id) {
        Discount updatedDiscount = responsitory.findById(id)
                .map(discount -> {
                    discount.setQuantity(newDiscount.getQuantity());
                    discount.setValues(newDiscount.getValues());
					discount.setStartDate(newDiscount.getStartDate());
					discount.setEndDate(newDiscount.getEndDate());
                    return responsitory.save(discount);
                }).orElseGet(() -> {
                    newDiscount.setId(id);
                    return responsitory.save(newDiscount);
                });
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "Update Discount successfully", updatedDiscount)
        );
    }

	 //Delete a Discount => DELETE method
	 @DeleteMapping("/{id}")
	 ResponseEntity<ResponseObject> deleteDiscount(@PathVariable String id) {
		 boolean exists = responsitory.existsById( id);
		 if(exists) {
			 responsitory.deleteById( id);
			 return ResponseEntity.status(HttpStatus.OK).body(
				 new ResponseObject("ok", "Delete Discount successfully", "")
			 );
		 }
		 return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
			 new ResponseObject("failed", "Cannot find Discount to delete", "")
		 );
	 }
}
