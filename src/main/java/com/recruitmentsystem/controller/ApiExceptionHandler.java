package com.recruitmentsystem.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

@RestControllerAdvice
public class ApiExceptionHandler {
    @Value("${spring.servlet.multipart.max-file-size}")
    private String maxSize;
    /**
     * MaxUploadSizeExceededException sẽ được xử lý tại đây
     */
    @ExceptionHandler(MaxUploadSizeExceededException.class)
//    @ResponseStatus(value = HttpStatus.PAYLOAD_TOO_LARGE)
    public ResponseEntity<?> handleAllException(Exception ex, WebRequest request) {
        // quá trình kiểm soat lỗi diễn ra ở đây
        return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE).body("You could not upload file bigger than " + maxSize);
    }
}
