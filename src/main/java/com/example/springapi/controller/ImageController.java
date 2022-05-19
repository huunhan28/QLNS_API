package com.example.springapi.controller;

import java.util.List;
import java.util.Optional;

import com.example.springapi.dto.ImageDTO;
import com.example.springapi.models.Image;
import com.example.springapi.models.Product;
import com.example.springapi.models.ResponseObject;
import com.example.springapi.repositories.ImageResponsitory;
import com.example.springapi.repositories.ProductResponsitory;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
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

@RequestMapping(path ="/api/v1/Images")
public class ImageController {


    @Autowired
	ImageResponsitory responsitory;

    @Autowired
    ProductResponsitory productResponsitory;
	
	@GetMapping("")
	List<Image> getAllImages(){
		return responsitory.findAll();
	}

    @GetMapping("/{id}")
	ResponseEntity<ResponseObject> getImage(@PathVariable int id){
		Optional<Image> foundImage = responsitory.findById(id);
		if(foundImage.isPresent()) {
			return ResponseEntity.status(HttpStatus.OK).body(
					new ResponseObject("ok", "Query image sucessfully", foundImage));
		}else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
						new ResponseObject("failed", "Can not find image with id=" + id, ""));
		}
        
	}

    //insert new Image with POST method
    //Postman : Raw, JSON
    @PostMapping("/insert")
    ResponseEntity<ResponseObject> insertImage(@RequestBody ImageDTO newImage) {
        Optional<Product> p=productResponsitory.findById(newImage.getProductId());
        Image image =new Image(p.get(),newImage.getLink());

        return ResponseEntity.status(HttpStatus.OK).body(
           new ResponseObject("ok", "Insert image successfully", responsitory.save(image))
        );
    }

    //update
    //update, upsert = update if found, otherwise insert
    @PutMapping("/{id}")
    ResponseEntity<ResponseObject> updateImage(@RequestBody ImageDTO imageDTO, @PathVariable int id) {
        Optional<Product> p=productResponsitory.findById(imageDTO.getProductId());
        Image newImage =new Image(p.get(),imageDTO.getLink());

        Image updatedImage = responsitory.findById(id)
                .map(image -> {
                    image.setProduct(newImage.getProduct());
                    image.setLink(newImage.getLink());
                    return responsitory.save(image);
                }).orElseGet(() -> {
                    newImage.setId((long)id);
                    return responsitory.save(newImage);
                });
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "Update Image successfully", updatedImage)
        );
    }

    //Delete a Image => DELETE method
    @DeleteMapping("/{id}")
    ResponseEntity<ResponseObject> deleteImage(@PathVariable int id) {
        boolean exists = responsitory.existsById( id);
        if(exists) {
            responsitory.deleteById( id);
            return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "Delete image successfully", "")
            );
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
            new ResponseObject("failed", "Cannot find image to delete", "")
        );
    }
    
}
