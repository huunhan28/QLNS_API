package com.example.springapi.controller;

import java.io.Console;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class UploadFileController {
    @Value("${file.upload-dir}")
    String FILE_DIRECTORY;

    @PostMapping("/api/v1/uploadFile")
    public ResponseEntity<Object> fileUpload(@RequestParam("File") MultipartFile file) throws IOException {
        String path_Directory = "D:\\Document PTIT\\Hoc ki 8\\PHAN MEM HUONG DICH VU\\DOAN\\QLNS_API\\src\\main\\resources\\static\\Image";
        System.out.println(FILE_DIRECTORY);
        Files.copy(file.getInputStream(), Paths.get(FILE_DIRECTORY + File.separator + file.getOriginalFilename()),
                StandardCopyOption.REPLACE_EXISTING);
        return new ResponseEntity<Object>("The image up load successfully!", HttpStatus.OK);

    }
}
