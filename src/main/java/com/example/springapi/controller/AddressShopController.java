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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.springapi.apputil.AppUtils;
import com.example.springapi.models.AddressShop;
import com.example.springapi.models.ResponseObject;
import com.example.springapi.repositories.AddressShopRepository;

@RestController
@RequestMapping(path = "/api/v1/AddressShop/")
public class AddressShopController {
	
	@Autowired
	AddressShopRepository repository;

	
	@GetMapping("")
	public List<AddressShop> getAddressShops(){
		return repository.findAll();
	}
	
	@GetMapping("newAddressShop")
	public ResponseEntity<ResponseObject> getNewAddressShop(){
		Optional<AddressShop> optional = repository.findTopByOrderByIdDesc();
		if(optional.isPresent()) {
			return AppUtils.returnJS(HttpStatus.OK, "OK", "Find address success", optional.get());
		}else {
			return AppUtils.returnJS(HttpStatus.NOT_FOUND, "Failed", "Find new address failed", null);
		}
	}
	
	@GetMapping("STT")
	public ResponseEntity<ResponseObject> getNewAddressShop(@RequestParam("stt") int stt){
		Optional<AddressShop> optional = repository.findByStt(stt);
		if(optional.isPresent()) {
			return AppUtils.returnJS(HttpStatus.OK, "OK", "Find address by stt success", optional.get());
		}else {
			return AppUtils.returnJS(HttpStatus.NOT_FOUND, "Failed", "Find address stt failed", null);
		}
	}
	
	@GetMapping("{id}")
	public ResponseEntity<ResponseObject> getAddressShopById(@PathVariable int id){
		Optional<AddressShop> optional = repository.findById(id);
		if(optional.isPresent()) {
			return AppUtils.returnJS(HttpStatus.OK, "OK", "Find address success", optional.get());
		}else {
			return AppUtils.returnJS(HttpStatus.NOT_FOUND, "Failed", "Find new address failed", null);
		}
	}
	
	@PostMapping("save")
	public ResponseEntity<ResponseObject> updateAddressShop(@RequestBody AddressShop addressShop){
			return AppUtils.returnJS(HttpStatus.OK, "OK", "Save address success", repository.save(addressShop));
	}
	
	@DeleteMapping("{id}")
	public ResponseEntity<ResponseObject> deleteAddressShop(@PathVariable int id){
		return AppUtils.returnJS(HttpStatus.OK, "OK", "Save address success", repository.deleteById(id));
}
}
