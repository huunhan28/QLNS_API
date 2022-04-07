package com.example.springapi.dto;

import lombok.Getter;

import com.example.springapi.uploadfile.model.FileDB;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDTO {
	private int id;
	private String name;
	private String description;
	private int imageId;
	private String link;
}
