package com.example.springapi.controller;

import java.util.List;
import java.util.Optional;

import com.example.springapi.models.ResponseObject;
//import com.example.springapi.models.Role;
//import com.example.springapi.repositories.RoleResponsitory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController

@RequestMapping(path ="/api/v1/Roles")
public class RoleController {
    
//    @Autowired
//	RoleResponsitory responsitory;
//
//    @GetMapping("")
//	List<Role> getAllRoles(){
//		return responsitory.findAll();
//	}
//
//    @GetMapping("/{id}")
//	ResponseEntity<ResponseObject> getRole(@PathVariable Long id){
//		Optional<Role> foundRole = responsitory.findById(id);
//		if(foundRole.isPresent()) {
//			return ResponseEntity.status(HttpStatus.OK).body(
//					new ResponseObject("ok!", "Query role sucessfully", foundRole));
//		}else {
//			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
//						new ResponseObject("failed!", "Can not find role with id=" + id, ""));
//		}
//        
//	}
    
}
