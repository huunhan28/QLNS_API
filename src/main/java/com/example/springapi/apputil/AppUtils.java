package com.example.springapi.apputil;

import com.example.springapi.models.ResponseObject;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class AppUtils {
    public static ResponseEntity<ResponseObject> returnJS(HttpStatus httpStatus, String status,
    String message, Object object) {

        return ResponseEntity.status(httpStatus)
                .body(
                        new ResponseObject(
                                "Failed", "Roles do not exists!", object)

                );
    }

}
