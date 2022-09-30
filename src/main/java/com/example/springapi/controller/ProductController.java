package com.example.springapi.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

import com.example.springapi.apputil.AppUtils;
import com.example.springapi.dto.ProductDTO;
import com.example.springapi.models.Category;
import com.example.springapi.models.Discount;
import com.example.springapi.models.Product;
import com.example.springapi.models.ProductReport;
import com.example.springapi.models.ResponseObject;
import com.example.springapi.repositories.CategoryResponsitory;
import com.example.springapi.repositories.ProductResponsitory;
import com.example.springapi.requestmodel.DiscountRequest;
import com.example.springapi.service.QueryMySql;
import com.example.springapi.service.UploadFileService;
import com.example.springapi.uploadfile.model.FileDB;
import com.example.springapi.uploadfile.repository.FileDBRepository;

@RestController
@RequestMapping(path = "/api/v1/Products")
public class ProductController {
	@Autowired
	UploadFileService uploadFileService;

    @Autowired
    ProductResponsitory responsitory;

    @Autowired
    CategoryResponsitory categoryResponsitory;

    @Autowired
    FileDBRepository fileDBRepository;

    FileDB temp=null;

    @CrossOrigin(origins = "http://organicfood.com")
    @GetMapping("")
    List<Product> getAllProducts() {
        List<Product> products = responsitory.findAll();
        List<Product> result = new ArrayList<Product>();
        for (Product product : products) {
            if (product.isDisplay()) {
                result.add(product);
            }
            
        }
        return result;
    }
    


    @CrossOrigin(origins = "http://organicfood.com")
    @GetMapping("/{id}")
    ResponseEntity<ResponseObject> getProduct(@PathVariable int id) {
        Optional<Product> foundProduct = responsitory.findById(id);
        if (foundProduct.isPresent()) {
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("ok", "Query product sucessfully", foundProduct));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    new ResponseObject("failed", "Can not find product with id=" + id, ""));
        }

    }

    @CrossOrigin(origins = "http://organicfood.com")
    @GetMapping("category/{id}")
    List<Product> getProductByCategory(@PathVariable int id) {
        Optional<Category> category = categoryResponsitory.findById(id);
        List<Product> products = responsitory.findAllByCategory(category.get());
        List<Product> result = new ArrayList<Product>();
        for (Product product : products) {
            if (product.isDisplay()) {
                result.add(product);
            }
            
        }
        return result;

    }

    // insert new Product with POST method
    // Postman : Raw, JSON

    @CrossOrigin(origins = "http://organicfood.com")
	@PostMapping(value = "/insert/v2", consumes = {
			MediaType.APPLICATION_JSON_VALUE,
			MediaType.MULTIPART_FORM_DATA_VALUE
	})
	public ResponseEntity<ResponseObject> insertDiscountWithImage(
			@RequestPart("product") ProductDTO newProductDTO,
			@RequestPart("file") MultipartFile file) {

    	  Optional<Category> category = categoryResponsitory.findById(newProductDTO.getCategoryId());
          List<Product> foundProducts = responsitory.findByName(newProductDTO.getName().trim());

          if (foundProducts.size() > 0) {
              return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                      new ResponseObject("failed", "Product name already taken", ""));
          }
          Product newProduct = new Product(newProductDTO.getProductId(),
                  category.isPresent() ? category.get() : null,
                  newProductDTO.getName(),
                  newProductDTO.getPrice(),
                  newProductDTO.getCalculationUnit(),
                  newProductDTO.getTotal(),
                  newProductDTO.getDescription(),
                  newProductDTO.getSlug(),
                  newProductDTO.isDisplay(),
                  newProductDTO.getRate(),
                  newProductDTO.getDiscount(),
                  newProductDTO.getId(),
                  newProductDTO.getUrl(),
                  newProductDTO.getYear());
   
		FileDB fileDB = uploadFileService.uploadFileToLocalAndDB(file);
		System.out.println("sau khi ep kieu");
		Discount discount = new Discount();
		newProduct.setImage(fileDB);
		try {
			newProduct = responsitory.save(newProduct);
		} catch (Exception e) {
			return AppUtils.returnJS(HttpStatus.BAD_REQUEST, "Failed",
					"Insert product", null);
		}

		return AppUtils.returnJS(HttpStatus.OK, "Ok",
				"Insert discount with image success", newProduct);
	}
    @PostMapping("/insert")
    ResponseEntity<ResponseObject> insertProduct2(@RequestBody ProductDTO newProductDTO) {
        // 2 products must not have the same name !
        Optional<Category> category = categoryResponsitory.findById(newProductDTO.getCategoryId());
        List<Product> foundProducts = responsitory.findByName(newProductDTO.getName().trim());

        if (foundProducts.size() > 0) {
            return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED).body(
                    new ResponseObject("failed", "Product name already taken", ""));
        }
        Product newProduct = new Product(newProductDTO.getProductId(),
                category.isPresent() ? category.get() : null,
                newProductDTO.getName(),
                newProductDTO.getPrice(),
                newProductDTO.getCalculationUnit(),
                newProductDTO.getTotal(),
                newProductDTO.getDescription(),
                newProductDTO.getSlug(),
                newProductDTO.isDisplay(),
                newProductDTO.getRate(),
                newProductDTO.getDiscount(),
                newProductDTO.getId(),
                newProductDTO.getUrl(),
                newProductDTO.getYear());
        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "Insert Product successfully", responsitory.save(newProduct)));
    }

    // update, upsert = update if found, otherwise insert
    @CrossOrigin(origins = "http://organicfood.com")

    @PutMapping(value = "/{id}", consumes = {
        MediaType.APPLICATION_JSON_VALUE,
        MediaType.MULTIPART_FORM_DATA_VALUE
})
    ResponseEntity<ResponseObject> updateProduct(@RequestPart("product") ProductDTO newProductDTO,
    @RequestPart("file") MultipartFile file,
     @PathVariable int id) {
     
        Optional<Category> category = categoryResponsitory.findById(newProductDTO.getCategoryId());
        Product newProduct = new Product(newProductDTO.getProductId(),
                category.isPresent() ? category.get() : null,
                newProductDTO.getName(),
                newProductDTO.getPrice(),
                newProductDTO.getCalculationUnit(),
                newProductDTO.getTotal(),
                newProductDTO.getDescription(),
                newProductDTO.getSlug(),
                newProductDTO.isDisplay(),
                newProductDTO.getRate(),
                newProductDTO.getDiscount(),
                newProductDTO.getId(),
                newProductDTO.getUrl(),
                newProductDTO.getYear());
        Product updatedProduct = responsitory.findById(id)
                .map(product -> {
                    product.setCategory(category.isPresent() ? category.get() : null);
                    product.setName(newProductDTO.getName());
                    product.setPrice(newProductDTO.getPrice());
                    product.setCalculationUnit(newProductDTO.getCalculationUnit());
                    product.setTotal(newProductDTO.getTotal());
                    product.setDescription(newProductDTO.getDescription());
                    product.setSlug(newProductDTO.getSlug());
                    product.setDisplay(newProductDTO.isDisplay());
                    product.setRate(newProductDTO.getRate());
                    product.setDiscount(newProductDTO.getDiscount());
                    product.setId(newProductDTO.getId());
                    product.setUrl(newProductDTO.getUrl());
                    product.setYear(newProductDTO.getYear());
                    // delete old file
                    temp = product.getImage();
                    FileDB fileDB = uploadFileService.uploadFileToLocalAndDB(file);
                    System.out.println("sau khi ep kieu");
                    product.setImage(fileDB);
                    Product productTemp = responsitory.save(product);
                    fileDBRepository.deleteById(temp.getId());
                    return productTemp;
                    
                }).orElseGet(() -> {
                    return responsitory.save(newProduct);
                });

        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseObject("ok", "Update Product successfully", updatedProduct));
    }

    // Delete a Product => DELETE method
    @CrossOrigin(origins = "http://organicfood.com")
    @DeleteMapping("/{id}")
    ResponseEntity<ResponseObject> deleteProduct(@PathVariable int id) {
        boolean exists = responsitory.existsById(id);
        if (exists) {
            responsitory.deleteById(id);
            return ResponseEntity.status(HttpStatus.OK).body(
                    new ResponseObject("ok", "Delete Product successfully", ""));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ResponseObject("failed", "Cannot find Product to delete", ""));
    }

    @CrossOrigin(origins = "http://organicfood.com")
    @GetMapping("/top10")
    public List<Product> getTop10Products() {
        List<Product> list = null;
        List<Product> result = new ArrayList<Product>();
        try {
            list = responsitory.findFirst10ByOrderByName();
            for (Product product : list) {
                if (product.isDisplay()) {
                    result.add(product);
                }
                
            }
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        return result;
    }

    @CrossOrigin(origins = "http://organicfood.com")
    @GetMapping("/searchProductsByName/{productName}")
    public ResponseEntity<ResponseObject> searchProductsByName(@PathVariable("productName") String productName) {
        List<Product> products = responsitory.findProductsByName(productName);
        if (products == null || products.size() == 0) {
            return AppUtils.returnJS(HttpStatus.NOT_FOUND, "Failed", "Dont have any product matches name", null);

        } else {
            return AppUtils.returnJS(HttpStatus.OK, "Ok", "Search by name product successfully", products);
        }
    }

}
