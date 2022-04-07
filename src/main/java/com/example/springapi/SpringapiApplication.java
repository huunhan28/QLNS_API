package com.example.springapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import com.example.springapi.uploadfile.newupload.FileStorageProperties;

@EnableConfigurationProperties({
    FileStorageProperties.class
})
@SpringBootApplication
public class SpringapiApplication {

	
	public static void main(String[] args) {
		SpringApplication.run(SpringapiApplication.class, args);
	}
	
}
