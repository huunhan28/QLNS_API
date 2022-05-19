package com.example.springapi.service;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.example.springapi.apputil.AppUtils;
import com.example.springapi.uploadfile.model.FileDB;
import com.example.springapi.uploadfile.newupload.FileController;
import com.example.springapi.uploadfile.newupload.FileStorageService;
import com.example.springapi.uploadfile.repository.FileDBRepository;
import com.example.springapi.uploadfile.service.FileDBService;

@Service
public class UploadFileService {
	
	private static final Logger logger = LoggerFactory.getLogger(UploadFileService.class);
//	private final String url = "api/auth/files/";
	
	private final String url = "images/uploads/";

	@Autowired
	FileStorageService fileStorageService;


	@Autowired
	FileDBService fileDBService;
	
	public FileDB uploadFileToLocalAndDB(MultipartFile file) {
		System.out.println("Vao file service");
		   String message = "";
	 		String fileName = fileStorageService.storeFile(file);
	 		System.out.println("done store to local");
	 		String fileDownloadUri = ServletUriComponentsBuilder
	 				.fromCurrentContextPath()
	 				.path(url)
	 				.path(fileName)
	 				.toUriString();
	 		Optional<FileDB> optionalFile = fileDBService.findByName(fileName);
	 		
	 		System.out.println("find by name");
	 		FileDB fileDB = null;
	 		try {
	 			if (optionalFile.isPresent()) {// update new file
	 				System.out.println("in case upddate file");
	 				fileDB = fileDBService.updateFileDB(file, fileDownloadUri, optionalFile.get());
	 				message = "Updated file successfully: " + file.getOriginalFilename();

	 			} else {
	 				fileDB = fileDBService.store(file, fileDownloadUri);
	 				message = "Uploaded file successfully: " + file.getOriginalFilename();
	 			}
	 			
	 		} catch (

	 		Exception e) {
	 			System.out.println("Catch store to db");
	 			// TODO: handle exception
	 			message = "upload file category failed";
	 			e.printStackTrace();
//	 			logger.info("Uploadfile to db: failed");
	 		}
	 		
	 		return fileDB;
	}
	

}
