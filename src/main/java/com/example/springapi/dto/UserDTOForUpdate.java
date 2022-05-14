package com.example.springapi.dto;

import org.springframework.web.multipart.MultipartFile;

import com.example.springapi.uploadfile.model.FileDB;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDTOForUpdate {
	private String name="";
	private String email="";
	private String address="";

}
