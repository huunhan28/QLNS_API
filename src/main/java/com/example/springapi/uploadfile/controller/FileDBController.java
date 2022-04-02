package com.example.springapi.uploadfile.controller;


import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.example.springapi.apputil.AppUtils;
import com.example.springapi.models.ResponseObject;
import com.example.springapi.uploadfile.model.FileDB;
import com.example.springapi.uploadfile.repository.FileDBRepository;
import com.example.springapi.uploadfile.response.FileDBResponse;
import com.example.springapi.uploadfile.service.FileDBService;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*", maxAge = 3600)
public class FileDBController {
	@Autowired
	FileDBRepository fileDBRepository;
	
	@Autowired
	FileDBService fileDBService;
	
	@PostMapping("/upload")
	public ResponseEntity<ResponseObject> uploadFile(@RequestParam MultipartFile file){
		
		String message = "";
		try {
			FileDB fileDB = fileDBService.store(file);
			message = "Uploaded file successfully: " + file.getOriginalFilename();
			return AppUtils.returnJS(HttpStatus.OK, "Ok", message, fileDB );
		} catch (Exception e) {
			// TODO: handle exception
			return AppUtils.returnJS(HttpStatus.EXPECTATION_FAILED, "Failed", message, null);
		}
		
	}
	
	@GetMapping("/files")
	public ResponseEntity<ResponseObject> getFiles(){
		List<FileDBResponse> files  = fileDBService.getAllFiles().map(
				dbFile -> {
					String fileDownloadUri = ServletUriComponentsBuilder
							.fromCurrentContextPath()
							.path("api/auth/files/")
							.path(String.valueOf(dbFile.getId()))
							.toUriString();
					return new FileDBResponse(dbFile.getName(), 
							fileDownloadUri,
							dbFile.getType(),
							dbFile.getData().length);
				}).collect(Collectors.toList());
		
		return AppUtils.returnJS(HttpStatus.OK, "OK", "Get all files success", files);
	}
	
	@GetMapping("/files/{id}")
	public ResponseEntity<ResponseObject> getFile(@PathVariable int id){
		Optional<FileDB> optionalFileDB = fileDBService.findById(id);
		
		if(optionalFileDB.isPresent()) {
			return ResponseEntity.ok()
					.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + optionalFileDB.get().getName() + "\"")
					.body(new ResponseObject(
							"Ok", "Get file successfully!", optionalFileDB.get().getData()));
		}else {
			return AppUtils.returnJS(HttpStatus.NOT_FOUND, "Failed", "Id not found", null);
		}
	}
}
