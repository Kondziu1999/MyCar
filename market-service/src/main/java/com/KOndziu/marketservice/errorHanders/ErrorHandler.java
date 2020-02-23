package com.KOndziu.marketservice.errorHanders;


import com.KOndziu.marketservice.exception.MarketCarNotFoundException;
import com.KOndziu.marketservice.exception.MarketDtoParsingException;
import com.KOndziu.marketservice.exception.UserNotFoundException;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class ErrorHandler extends ResponseEntityExceptionHandler {


    @ExceptionHandler(MarketCarNotFoundException.class)
    public ResponseEntity<Object> marketCarNotFoundHandler(RuntimeException e, WebRequest webRequest){
        CustomErrorResponse response=new CustomErrorResponse();

        response.setTimestamp(LocalDateTime.now());
        response.setStatus(HttpStatus.NOT_FOUND.value());
        response.setError(e.getMessage());

        return handleExceptionInternal(e,response, HttpHeaders.EMPTY,HttpStatus.NOT_FOUND,webRequest);

    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Object> handleWebException(RuntimeException e, WebRequest webRequest){
        CustomErrorResponse errors = new CustomErrorResponse();
        errors.setTimestamp(LocalDateTime.now());
        errors.setError(e.getMessage());
        errors.setStatus(HttpStatus.NOT_FOUND.value());

        return handleExceptionInternal(e,errors, HttpHeaders.EMPTY, HttpStatus.NOT_FOUND,webRequest);
    }

    @ExceptionHandler(MarketDtoParsingException.class)
    public ResponseEntity<Object> handleDtoParsingException(RuntimeException e, WebRequest webRequest){
        CustomErrorResponse errors = new CustomErrorResponse();
        errors.setTimestamp(LocalDateTime.now());
        errors.setError(e.getMessage());
        errors.setStatus(HttpStatus.NOT_FOUND.value());

        return handleExceptionInternal(e,errors, HttpHeaders.EMPTY, HttpStatus.BAD_REQUEST,webRequest);
    }
}
