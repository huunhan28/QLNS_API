package com.example.springapi.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.springapi.apputil.AppUtils;
import com.example.springapi.dto.UserDTOForUpdate;
import com.example.springapi.models.ResponseObject;
import com.example.springapi.security.entity.User;
import com.example.springapi.security.repository.UserRepository;
import com.example.springapi.service.UploadFileService;
import com.example.springapi.uploadfile.model.FileDB;



@RestController
@RequestMapping(value = "/api/v1/Users/")
public class UserController {
	
	@Autowired
    public JavaMailSender emailSender;
	
	@Autowired
	UploadFileService uploadFileService;
	
	@Autowired
	UserRepository userRepository;
	
	@GetMapping("")
	public List<User> getUsers(){
		return userRepository.findAll();
	}
	
	@GetMapping("{username}")
	public ResponseEntity<ResponseObject> getUserByUsername(@PathVariable("username") String username){
		Optional<User> user = userRepository.findByUsername(username);
		if(user.isPresent()) {
			return AppUtils.returnJS(HttpStatus.OK, "Ok", "Find user success fully", user.get());
		}else {
			return AppUtils.returnJS(HttpStatus.NOT_FOUND, "Failed", "Not exist this username", user.get());
		}
	}

	
	
	
	@GetMapping("id/{id}")
	@ResponseBody
	public ResponseEntity<ResponseObject> getUserById(@PathVariable(name="id") int id){
		Optional<User> user = userRepository.findById(id);
		if(user.isPresent()) {
			return AppUtils.returnJS(HttpStatus.OK, "Ok", "Find user success fully", user.get());
		}else {
			return AppUtils.returnJS(HttpStatus.NOT_FOUND, "Failed", "Not exist this username", user.get());
		}
	}
	
//	@GetMapping("{id}")
//	public ResponseEntity<ResponseObject> getUserById(@PathVariable("id") int id){
//		Optional<User> user = userRepository.findById(id);
//		if(user.isPresent()) {
//			return AppUtils.returnJS(HttpStatus.OK, "Ok", "Find user success fully", user.get());
//		}else {
//			return AppUtils.returnJS(HttpStatus.NOT_FOUND, "Failed", "Not exist this username", user.get());
//		}
//	}
	
	
	@PutMapping("update/{id}")
	public ResponseEntity<ResponseObject> updateUser(@PathVariable("id") int id, 
			@RequestPart("user") UserDTOForUpdate userDTO,
			@RequestPart("file") MultipartFile file){
		Optional<User> optionalUser = userRepository.findById(id);
		if(!optionalUser.isPresent()) {
			return AppUtils.returnJS(HttpStatus.NOT_FOUND, "Failed", "IDD user not found", null);
		}
		FileDB fileDB = uploadFileService.uploadFileToLocalAndDB(file);
		User user = optionalUser.get();
		user.setName(userDTO.getName());
		user.setEmail(userDTO.getEmail());
		user.setAddress(userDTO.getAddress());
		user.setImageUser(fileDB);
		User userUpdated;
		try {
			 userUpdated = userRepository.save(user);
				return AppUtils.returnJS(HttpStatus.OK, "Ok", "Update user successfully", userUpdated);
		}catch (Exception e) {
			// TODO: handle exception
			return AppUtils.returnJS(HttpStatus.NOT_IMPLEMENTED, "Failed", "Update user failed", null);
		}
		
	
		
	}
	
	
	@PutMapping("updateNotImage/{id}")
	public ResponseEntity<ResponseObject> updateUserNotImage(@PathVariable("id") int id, 
			@RequestPart("user") UserDTOForUpdate userDTO){
		Optional<User> optionalUser = userRepository.findById(id);
		if(!optionalUser.isPresent()) {
			return AppUtils.returnJS(HttpStatus.NOT_FOUND, "Failed", "IDD user not found", null);
		}
		User user = optionalUser.get();
		user.setName(userDTO.getName());
		user.setEmail(userDTO.getEmail());
		user.setAddress(userDTO.getAddress());
		User userUpdated;
		try {
			 userUpdated = userRepository.save(user);
				return AppUtils.returnJS(HttpStatus.OK, "Ok", "Update user successfully", userUpdated);
		}catch (Exception e) {
			// TODO: handle exception
			return AppUtils.returnJS(HttpStatus.NOT_IMPLEMENTED, "Failed", "Update user failed", null);
		}
		
	
		
	}

	@PutMapping("email/{email}/password/{password}")
    ResponseEntity<ResponseObject> updatePasswordByEmail(@PathVariable String email, @PathVariable String password) {
    	
        Optional<User> optionalUser = userRepository.findByEmail(email);
		if(!optionalUser.isPresent()) {
			return AppUtils.returnJS(HttpStatus.NOT_FOUND, "Failed", "Email user not found", null);
		}
		User user = optionalUser.get();
		user.setPassword(password);
		userRepository.save(user);
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "Update password successfully", "")
        );
    }
	
	

}
