package com.example.springapi.apputil;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;

import com.example.springapi.models.ResponseObject;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class AppUtils {
    public static ResponseEntity<ResponseObject> returnJS(HttpStatus httpStatus, String status,
    String message, Object object) {

        return ResponseEntity.status(httpStatus)
                .body(
                        new ResponseObject(
                                status, message, object)

                );
    }

    public static String getExceptionSql(ConstraintViolationException e){
        Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
        String errorMessage = "";
        if (!violations.isEmpty()) {
            StringBuilder builder = new StringBuilder();
            for (ConstraintViolation<?> o : violations) {
                builder.append(" Column: " + o.getPropertyPath() + " " + o.getMessage())
                        .append(System.getProperty("line.separator"));
            }
            errorMessage = builder.toString();
        } else {
            errorMessage = "ConstraintViolationException occured.";

        }
        return errorMessage;
    }
}
