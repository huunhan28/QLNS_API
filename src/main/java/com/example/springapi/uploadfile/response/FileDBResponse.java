package com.example.springapi.uploadfile.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Data
@AllArgsConstructor

public class FileDBResponse {
	private String name;
	private String url;
	private String type;
	private long size;
}
