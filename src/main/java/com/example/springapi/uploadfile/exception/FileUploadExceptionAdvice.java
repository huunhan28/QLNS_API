package com.example.springapi.uploadfile.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.example.springapi.apputil.AppUtils;
import com.example.springapi.models.ResponseObject;
import com.example.springapi.security.dto.MessageResponse;

@ControllerAdvice
public class FileUploadExceptionAdvice extends ResponseEntityExceptionHandler {

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<ResponseObject> handleMaxSizeException(MaxUploadSizeExceededException exc) {
    	return AppUtils.returnJS(HttpStatus.EXPECTATION_FAILED, "Failed", "File too large", null);
    }

}
