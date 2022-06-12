package com.example.springapi.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.springapi.apputil.AppUtils;
import com.example.springapi.models.Comment;
import com.example.springapi.models.ResponseObject;
import com.example.springapi.repositories.CommentRepository;

@RestController
@RequestMapping(path = "/api/v1/Comment/")
public class CommentController {
	
	@Autowired
	CommentRepository repository;

	
	@GetMapping("productId/{id}")
	public List<Comment> getAllCommentByProductId(@PathVariable("id") int id){
		return repository.findByIdOrderByCreateAtDesc(id);
	}
	
	@PostMapping("save")
	public ResponseEntity<ResponseObject> updateAddressShop(@RequestBody Comment comment){
		Comment commented;
		try {
			commented = repository.save(comment);
		} catch (Exception e) {
			//TODO: handle exception
			return AppUtils.returnJS(HttpStatus.NOT_IMPLEMENTED, "Failed", "Save cooment failed", repository.save(comment));
		}
			return AppUtils.returnJS(HttpStatus.OK, "OK", "Save comment success", commented);
	}
	
// 	@DeleteMapping("{id}")
// 	public ResponseEntity<ResponseObject> deleteAddressShop(@PathVariable int id){
// 		return AppUtils.returnJS(HttpStatus.OK, "OK", "Save address success", repository.deleteById(id));
// }
}
