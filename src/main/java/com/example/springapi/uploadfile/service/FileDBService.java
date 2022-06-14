package com.example.springapi.uploadfile.service;

import java.io.IOException;
import java.util.Optional;
import java.util.stream.Stream;

import org.springframework.web.multipart.MultipartFile;

import com.example.springapi.uploadfile.model.FileDB;

public interface FileDBService {
	FileDB store(MultipartFile file, String url) throws IOException;
	
	Stream<FileDB> getAllFiles();

	Optional<FileDB> findById(int id);
	
	Optional<FileDB> findByName(String name);
	
	FileDB updateFileDB(MultipartFile file, String url, FileDB oldFileDB);

	void deleteFile(FileDB fileDB);
}
