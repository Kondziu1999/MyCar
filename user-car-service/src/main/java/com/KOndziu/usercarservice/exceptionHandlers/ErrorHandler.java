package com.KOndziu.usercarservice.exceptionHandlers;

import com.KOndziu.usercarservice.exceptions.UserAlreadyExists;
import com.KOndziu.usercarservice.exceptions.UserNotFoundException;
import lombok.extern.java.Log;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;

@Log
@ControllerAdvice
public class ErrorHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Object> handleWebException(RuntimeException e, WebRequest webRequest){
        log.severe(e.getMessage());
        CustomErrorResponse errors = new CustomErrorResponse();
        errors.setTimestamp(LocalDateTime.now());
        errors.setError(e.getMessage());
        errors.setStatus(HttpStatus.NOT_FOUND.value());

        return handleExceptionInternal(e,errors, HttpHeaders.EMPTY, HttpStatus.NOT_FOUND,webRequest);
    }

    @ExceptionHandler(UserAlreadyExists.class)
    public ResponseEntity<Object> handleUserAlreadyExistsException(RuntimeException e, WebRequest request){
        log.severe(e.getMessage());

        CustomErrorResponse errors = new CustomErrorResponse();
        errors.setTimestamp(LocalDateTime.now());
        errors.setError(e.getMessage());
        errors.setStatus(HttpStatus.NOT_ACCEPTABLE.value());
        return handleExceptionInternal(e,errors, HttpHeaders.EMPTY, HttpStatus.NOT_FOUND,request);
        //return new ResponseEntity<>(errors,HttpStatus.NOT_ACCEPTABLE);
    }

}
