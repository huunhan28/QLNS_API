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
@RequestMapping(path ="/api/v1/Discounts")
public class DiscountController {
    
    @Autowired
	DiscountResponsitory responsitory;
    
    @Autowired
    DiscountDTORepository discountDTORepository;
    
    private static final Logger logger = LoggerFactory.getLogger(FileController.class);
//	private final String url = "api/auth/files/"; for download resource image
	
	private final String url = "images/uploads/";// only view

	@Autowired
	FileStorageService fileStorageService;

	@Autowired
	FileDBRepository fileDBRepository;

	@Autowired
	FileDBService fileDBService;

    @GetMapping("")
	List<Discount> getAllDiscounts(){
		return responsitory.findAll();
	}
    
    @GetMapping("/v2")
    List<DiscountDTO> getDiscountDTOs(){
    	return discountDTORepository.getDiscounts();
    }

	//insert new Discount with POST method
    //Postman : Raw, JSON
    @PostMapping("/insert")
    ResponseEntity<ResponseObject> insertDiscount(@RequestBody Discount newDiscount) {
        return ResponseEntity.status(HttpStatus.OK).body(
           new ResponseObject("ok", "Insert Discount successfully", responsitory.save(newDiscount))
        );
    }
    
    @PostMapping(value = "/insert/v2", consumes = {
    		MediaType.APPLICATION_JSON_VALUE,
    		MediaType.MULTIPART_FORM_DATA_VALUE
    })
    public ResponseEntity<ResponseObject> insertDiscountWithImage(
    		@RequestPart("discount") DiscountRequest discountRequest,
    		@RequestPart("file") MultipartFile file
    		)
    {
    
    	Optional<Discount> disOptional = responsitory.findById(discountRequest.getId());
    	 if(disOptional.isPresent()) {
    		 return AppUtils.returnJS(HttpStatus.CONFLICT, "Failed",
    	    			"Discount id have taken", null);
         }
    	 
         
         String message = "";
 		String fileName = fileStorageService.storeFile(file);
 		

 		String fileDownloadUri = ServletUriComponentsBuilder
 				.fromCurrentContextPath()
 				.path(url)
 				.path(fileName)
 				.toUriString();
 		
 		Optional<FileDB> optionalFile = fileDBRepository.findByName(fileName);
 		FileDB fileDB;
 		try {
 			
 			if (optionalFile.isPresent()) {// update new file
 				fileDB = fileDBService.updateFileDB(file, fileDownloadUri, optionalFile.get());
 				message = "Updated file successfully: " + file.getOriginalFilename();

 			} else {
 				fileDB = fileDBService.store(file, fileDownloadUri);
 				message = "Uploaded file successfully: " + file.getOriginalFilename();
 			}
 			
 		} catch (

 		Exception e) {
 			// TODO: handle exception
 			message = "upload file category failed";
 			return AppUtils.returnJS(HttpStatus.NOT_IMPLEMENTED, "Failed", message, null);
 		}
 		Discount discount=new Discount();
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

	//update, upsert = update if found, otherwise insert
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
