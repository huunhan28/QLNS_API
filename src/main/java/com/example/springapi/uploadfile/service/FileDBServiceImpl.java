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
	public FileDB store(MultipartFile file, String url) throws IOException {
		String fileName = StringUtils.cleanPath(file.getOriginalFilename());
		FileDB fileDB = new FileDB(fileName, file.getContentType(), file.getBytes(), url);
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

	@Override
	public Optional<FileDB> findByName(String name) {
		// TODO Auto-generated method stub
		return fileDBRepository.findByName(name);
	}

	@Override
	public FileDB updateFileDB(MultipartFile file, String url, FileDB oldFileDB) {
		// TODO Auto-generated method stub
		String fileName = StringUtils.cleanPath(file.getOriginalFilename());
		FileDB fileDB = null;
		try {
			fileDB = new FileDB(oldFileDB.getId(), fileName, file.getContentType(), file.getBytes(), url);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return fileDBRepository.save(fileDB);
	}
	
	
}
