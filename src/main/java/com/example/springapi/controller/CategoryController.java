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

import com.example.springapi.apputil.AppUtils;
import com.example.springapi.models.Category;
import com.example.springapi.models.ResponseObject;
import com.example.springapi.repositories.CategoryResponsitory;

@RestController
@RequestMapping(path ="/api/v1/Categories")
public class CategoryController {

	@Autowired
	CategoryResponsitory responsitory;
	
	@GetMapping("")
	List<Category> getAllCategorys(){
		return responsitory.findAll();
	}
	
	@GetMapping("/v2")
	List<Category> getAllCategories(){
		return responsitory.findAll();
	}
	

	
	
	@GetMapping("/{id}")
	public ResponseEntity<ResponseObject> getCategory(@PathVariable int id){
		Optional<Category> cateOptional = responsitory.findById(id);
		if(cateOptional.isPresent()) {
			return AppUtils.returnJS(HttpStatus.OK, "Ok", "Get category successfully", cateOptional.get());
		}else {
			return AppUtils.returnJS(HttpStatus.NOT_FOUND, "Failed", "Category dont exist (by id)", null);
		}
	}
	//insert new Category with POST method
    //Postman : Raw, JSON
    @PostMapping("/insert")
    ResponseEntity<ResponseObject> insertCategory(@RequestBody Category newCategory) {
        //2 categories must not have the same name !
        List<Category> foundCategories = responsitory.findByName(newCategory.getName().trim());
        
        if(foundCategories.size() > 0) {
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                new ResponseObject("failed", "Category name already taken", "")
            );
        }
        return ResponseEntity.status(HttpStatus.OK).body(
           new ResponseObject("ok", "Insert Category successfully", responsitory.save(newCategory))
        );
    }
    //update, upsert = update if found, otherwise insert
    @PutMapping("/{id}")
    ResponseEntity<ResponseObject> updateCategory(@RequestBody Category newCategory, @PathVariable int id) {
        Category updatedCategory = responsitory.findById(id)
                .map(category -> {
                    category.setName(newCategory.getName());
                    category.setDescription(newCategory.getDescription());
                    return responsitory.save(category);
                }).orElseGet(() -> {
                    newCategory.setId(id);
                    return responsitory.save(newCategory);
                });
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "Update Category successfully", updatedCategory)
        );
    }
    //Delete a Category => DELETE method
    @DeleteMapping("/{id}")
    ResponseEntity<ResponseObject> deleteCategory(@PathVariable int id) {
        boolean exists = responsitory.existsById( id);
        if(exists) {
            responsitory.deleteById( id);
            return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "Delete category successfully", "")
            );
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
            new ResponseObject("failed", "Cannot find category to delete", "")
        );
    }
}
