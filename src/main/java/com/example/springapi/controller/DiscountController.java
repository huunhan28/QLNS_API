package com.example.springapi.controller;

import java.util.List;
import java.util.Optional;

import com.example.springapi.apputil.AppUtils;
import com.example.springapi.dto.DiscountDTO;
import com.example.springapi.models.Category;
import com.example.springapi.models.Discount;
import com.example.springapi.models.ResponseObject;
import com.example.springapi.repositories.DiscountResponsitory;
import com.example.springapi.requestmodel.DiscountRequest;
import com.example.springapi.service.DiscountDTORepository;
import com.example.springapi.service.UploadFileService;
import com.example.springapi.uploadfile.model.FileDB;
import com.example.springapi.uploadfile.newupload.FileController;
import com.example.springapi.uploadfile.newupload.FileStorageService;
import com.example.springapi.uploadfile.repository.FileDBRepository;
import com.example.springapi.uploadfile.service.FileDBService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping(path = "/api/v1/Discounts")
public class DiscountController {

	@Autowired
	UploadFileService uploadFileService;

	@Autowired
	DiscountResponsitory responsitory;

	@Autowired
	DiscountDTORepository discountDTORepository;

	@CrossOrigin(origins = "http://organicfood.com")
	@GetMapping("")
	List<Discount> getAllDiscounts() {
		return responsitory.findAll();
	}

	@CrossOrigin(origins = "http://organicfood.com")
	@GetMapping("/v2")
	List<DiscountDTO> getDiscountDTOs() {
		return discountDTORepository.getDiscounts();
	}

	@CrossOrigin(origins = "http://organicfood.com")
	@GetMapping("/{id}")
	ResponseEntity<ResponseObject> getDiscount(@PathVariable String id) {
		Optional<Discount> foundDiscount = responsitory.findById(id);
		if (foundDiscount.isPresent()) {
			return ResponseEntity.status(HttpStatus.OK).body(
					new ResponseObject("ok", "Query product sucessfully", foundDiscount));
		} else {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
					new ResponseObject("failed", "Can not find Discount with id=" + id, ""));
		}

	}

	// insert new Discount with POST method
	// Postman : Raw, JSON
	@CrossOrigin(origins = "http://organicfood.com")
	@PostMapping("/insert")
	ResponseEntity<ResponseObject> insertDiscount(@RequestBody Discount newDiscount) {
		return ResponseEntity.status(HttpStatus.OK).body(
				new ResponseObject("ok", "Insert Discount successfully", responsitory.save(newDiscount)));
	}

	@CrossOrigin(origins = "http://organicfood.com")
	@PostMapping(value = "/insert/v2", consumes = {
			MediaType.APPLICATION_JSON_VALUE,
			MediaType.MULTIPART_FORM_DATA_VALUE
	})
	public ResponseEntity<ResponseObject> insertDiscountWithImage(
			@RequestPart("discount") DiscountRequest discountRequest,
			@RequestPart("file") MultipartFile file) {

		System.out.println("vao insert discount");

		Optional<Discount> disOptional = responsitory.findById(discountRequest.getId());
		if (disOptional.isPresent()) {
			return AppUtils.returnJS(HttpStatus.CONFLICT, "Failed",
					"Discount id have taken", null);
		}

		System.out.println("truoc khi ep kieu");
		FileDB fileDB = uploadFileService.uploadFileToLocalAndDB(file);
		System.out.println("sau khi ep kieu");
		Discount discount = new Discount();
		discount.setImageDiscount(fileDB);
		discount.setId(discountRequest.getId());
		discount.setQuantity(discountRequest.getQuantity());
		discount.setPercent(discountRequest.getPercent());
		discount.setStartDate(discountRequest.getStartDate());
		discount.setEndDate(discountRequest.getEndDate());
		try {
			discount = responsitory.save(discount);
		} catch (Exception e) {
			System.out.println(discount.toString());
			// TODO: handle exception
			e.printStackTrace();
		}

		return AppUtils.returnJS(HttpStatus.OK, "Ok",
				"Insert discount with image success", discount);
	}

	// update, upsert = update if found, otherwise insert
	@CrossOrigin(origins = "http://organicfood.com")
	@PutMapping("/{id}")
	ResponseEntity<ResponseObject> updateDiscount(@RequestBody Discount newDiscount, @PathVariable String id) {
		Discount updatedDiscount = responsitory.findById(id)
				.map(discount -> {
					discount.setQuantity(newDiscount.getQuantity());
					discount.setPercent(newDiscount.getPercent());
					discount.setStartDate(newDiscount.getStartDate());
					discount.setEndDate(newDiscount.getEndDate());
					return responsitory.save(discount);
				}).orElseGet(() -> {
					newDiscount.setId(id);
					return responsitory.save(newDiscount);
				});
		return ResponseEntity.status(HttpStatus.OK).body(
				new ResponseObject("ok", "Update Discount successfully", updatedDiscount));
	}

	// Delete a Discount => DELETE method
	@CrossOrigin(origins = "http://organicfood.com")
	@DeleteMapping("/{id}")
	ResponseEntity<ResponseObject> deleteDiscount(@PathVariable String id) {
		boolean exists = responsitory.existsById(id);
		if (exists) {
			responsitory.deleteById(id);
			return ResponseEntity.status(HttpStatus.OK).body(
					new ResponseObject("ok", "Delete Discount successfully", ""));
		}
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
				new ResponseObject("failed", "Cannot find Discount to delete", ""));
	}
}
