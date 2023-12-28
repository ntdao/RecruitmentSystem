package com.recruitmentsystem.exception;

import com.recruitmentsystem.response.BaseResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

@RestControllerAdvice
public class ApiExceptionHandler {
//    @Value("${spring.servlet.multipart.max-file-size}")
//    private String maxSize;

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<Object> handleMaxUploadSizeExceededException(MaxUploadSizeExceededException exception) {
        System.out.println("--------------Handle Max Upload Size Exceeded Exception----------------");
        exception.printStackTrace();
//        String err = "You could not upload file bigger than " + maxSize;
        BaseResponse errorMessage = new BaseResponse("400", exception.toString());
        return new ResponseEntity<>(errorMessage, new HttpHeaders(), HttpStatus.PAYLOAD_TOO_LARGE);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Object> handleResourceNotFoundException(ResourceNotFoundException exception) {
        System.out.println("--------------Handle Resource Not Found Exception----------------");
        exception.printStackTrace();
        BaseResponse errorMessage = new BaseResponse("404", exception.toString());
        return new ResponseEntity<>(errorMessage, new HttpHeaders(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ResourceAlreadyExistsException.class)
    public ResponseEntity<Object> handleResourceAlreadyExistsException(ResourceAlreadyExistsException exception) {
        System.out.println("--------------Handle Resource Already Exists Exception----------------");
        exception.printStackTrace();
        BaseResponse errorMessage = new BaseResponse("409", exception.toString());
        return new ResponseEntity<>(errorMessage, new HttpHeaders(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(InputException.class)
    public ResponseEntity<Object> handleInputException(InputException exception) {
        System.out.println("--------------Handle Input Exception----------------");
        exception.printStackTrace();
        BaseResponse errorMessage = new BaseResponse("400", exception.toString());
        return new ResponseEntity<>(errorMessage, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleAnyException(Exception exception) {
        System.out.println("--------------Handle Any Exception----------------");
        exception.printStackTrace();
        BaseResponse errorMessage = new BaseResponse("500", exception.toString());
        return new ResponseEntity<>(errorMessage, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
