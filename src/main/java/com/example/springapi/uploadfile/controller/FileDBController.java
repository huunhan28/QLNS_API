package com.example.springapi.uploadfile.controller;

import java.io.IOException;
import java.net.http.HttpRequest;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.example.springapi.apputil.AppUtils;
import com.example.springapi.models.ResponseObject;
import com.example.springapi.service.QueryMySql;
import com.example.springapi.uploadfile.model.FileDB;
import com.example.springapi.uploadfile.newupload.FileController;
import com.example.springapi.uploadfile.newupload.FileStorageService;
import com.example.springapi.uploadfile.repository.FileDBRepository;
import com.example.springapi.uploadfile.response.FileDBResponse;
import com.example.springapi.uploadfile.service.FileDBService;

@RestController
@RequestMapping("/api/auth/files")
@CrossOrigin(origins = "*", maxAge = 3600)
public class FileDBController {

	private static final Logger logger = LoggerFactory.getLogger(FileController.class);
//	private final String url = "api/auth/files/";
	
	private final String url = "images/uploads/";

	@Autowired
	FileStorageService fileStorageService;

	@Autowired
	FileDBRepository fileDBRepository;

	@Autowired
	FileDBService fileDBService;
	
	@Autowired
	QueryMySql<Object> mysql;
	
	@PutMapping("/updateLink")
	public void updateLinkImage(@RequestParam String address) {
		mysql.updateLinkImage(address);
	}

	@PostMapping("/upload")
	public ResponseEntity<ResponseObject> uploadFile(@RequestParam MultipartFile file) {

		String message = "";
		String fileName = fileStorageService.storeFile(file);
		Optional<FileDB> optionalFile = fileDBRepository.findByName(fileName);

		String fileDownloadUri = ServletUriComponentsBuilder
				.fromCurrentContextPath()
				.path(url)
				.path(fileName)
				.toUriString();
		try {
			FileDB fileDB;
			if (optionalFile.isPresent()) {// update new file
				fileDB = fileDBService.updateFileDB(file, fileDownloadUri, optionalFile.get());
				message = "Updated file successfully: " + file.getOriginalFilename();

			} else {
				fileDB = fileDBService.store(file, fileDownloadUri);
				message = "Uploaded file successfully: " + file.getOriginalFilename();
			}
			return AppUtils.returnJS(HttpStatus.OK, "Ok", message, fileDB);
		} catch (

		Exception e) {
			// TODO: handle exception
			return AppUtils.returnJS(HttpStatus.EXPECTATION_FAILED, "Failed", message, null);
		}

	}

	@GetMapping("")
	public ResponseEntity<ResponseObject> getFiles() {
//		List<FileDBResponse> files = fileDBService.getAllFiles().map(dbFile -> {
//			String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath().path(url)
//					.path(String.valueOf(dbFile.getName())).toUriString();
//			return new FileDBResponse(dbFile.getName(), fileDownloadUri, dbFile.getType(), dbFile.getData().length);
//		}).collect(Collectors.toList());

		return AppUtils.returnJS(HttpStatus.OK, "OK", "Get all files success",fileDBRepository.findAll());
	}

	@GetMapping("/get/{id}")
	public ResponseEntity<ResponseObject> getFile(@PathVariable int id) {
		Optional<FileDB> optionalFileDB = fileDBService.findById(id);

		if (optionalFileDB.isPresent()) {
			return ResponseEntity.ok()
					.header(HttpHeaders.CONTENT_DISPOSITION,
							"attachment; filename=\"" + optionalFileDB.get().getName() + "\"")
					.body(new ResponseObject("Ok", "Get file successfully!", optionalFileDB.get()));
		} else {
			return AppUtils.returnJS(HttpStatus.NOT_FOUND, "Failed", "Id not found", null);
		}
	}

	@PostMapping("/uploadMultipleFile")
	public ResponseEntity<ResponseObject> uploadMultipleFile(@RequestParam("files") MultipartFile[] files) {

		List<FileDB> array = Arrays.asList(files).stream().map(file -> {
			String message = "";
			String fileName = fileStorageService.storeFile(file);
			String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath().path(url).path(fileName)
					.toUriString();
			try {
				Optional<FileDB> optionalFileDB = fileDBRepository.findByName(fileName);
				FileDB fileDB = null;
				if (optionalFileDB.isPresent()) {// update new file
					fileDB = fileDBService.updateFileDB(file, fileDownloadUri, optionalFileDB.get());

				} else {
					fileDB = fileDBService.store(file, fileDownloadUri);
				}
				return fileDB;
			} catch (Exception e) {
				// TODO: handle exception
				return new FileDB();
			}
		}).collect(Collectors.toList());
		return AppUtils.returnJS(HttpStatus.OK, "Ok", "Upload multiple file success", array);
	}

	@GetMapping("/download/{fileName:.+}")
	public ResponseEntity<Resource> downloadFile(@PathVariable String fileName, HttpServletRequest request) {
		// Load file as Resource
		System.out.println("file name:" + fileName);
		Resource resource = fileStorageService.loadFileAsResource(fileName);

		// Try to determine file's content type
		String contentType = null;
		try {
			contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
		} catch (IOException ex) {
			logger.info("Could not determine file type.");
		}

		// Fallback to the default content type if type could not be determined
		if (contentType == null) {
			contentType = "application/octet-stream";
		}

		return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType))
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
				.body(resource);
	}
}
