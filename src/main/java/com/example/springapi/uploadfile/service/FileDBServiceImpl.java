package com.example.springapi.uploadfile.service;

import java.io.IOException;
import java.util.Optional;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.example.springapi.uploadfile.model.FileDB;
import com.example.springapi.uploadfile.repository.FileDBRepository;

@Service
public class FileDBServiceImpl implements FileDBService {
	
	@Autowired
	FileDBRepository fileDBRepository;

	@Override
	public FileDB store(MultipartFile file) throws IOException {
		String fileName = StringUtils.cleanPath(file.getOriginalFilename());
		FileDB fileDB = new FileDB(fileName, file.getContentType(), file.getBytes());
		return fileDBRepository.save(fileDB);
	}

	@Override
	public Stream<FileDB> getAllFiles() {
		// TODO Auto-generated method stub
		return fileDBRepository.findAll().stream();
	}

	@Override
	public Optional<FileDB> findById(int id) {
		// TODO Auto-generated method stub
		return fileDBRepository.findById(id);
	}
	
	
}
