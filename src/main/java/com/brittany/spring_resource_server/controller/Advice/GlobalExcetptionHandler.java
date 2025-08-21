package com.brittany.spring_resource_server.controller.Advice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.brittany.spring_resource_server.controller.DTOs.ErrorResponse;

@RestControllerAdvice
public class GlobalExcetptionHandler {
    
     @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleInvalidArguments(MethodArgumentNotValidException exception) {

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("status", HttpStatus.BAD_REQUEST);
        body.put("timestamp", LocalDateTime.now());

        Map<String, String> mapErrors = new HashMap<>();
        BindingResult result = exception.getBindingResult();

        List<FieldError> fieldErrors = result.getFieldErrors();

        fieldErrors.forEach(
                error -> mapErrors.put(error.getField(), error.getDefaultMessage()));

        body.put("message", "Error de validación");
        body.put("errors", mapErrors);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);

    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> requestHandlerEntityNotFoundException(EntityNotFoundException exception){
        ErrorResponse response = new ErrorResponse(HttpStatus.NOT_FOUND.value(), exception.getMessage(), LocalDateTime.now()); 
        
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);   
    }


    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> requestHandlerHttpMessageNotReadableException(HttpMessageNotReadableException exception){
        ErrorResponse response = new ErrorResponse(HttpStatus.BAD_REQUEST.value(),"El campo GrantType solo acepta como valores: password o refreshToken" , LocalDateTime.now()); 

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);

    }

     @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> requestHandlerIllegalArgumentException(IllegalArgumentException exception){
        ErrorResponse response = new ErrorResponse(HttpStatus.BAD_REQUEST.value(),exception.getMessage(), LocalDateTime.now()); 

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);

    }

     @ExceptionHandler(RefreshTokenException.class)
    public ResponseEntity<ErrorResponse> requestHandlerRefreshTokenException(RefreshTokenException exception){
        ErrorResponse response = new ErrorResponse(HttpStatus.UNAUTHORIZED.value(),exception.getMessage(), LocalDateTime.now()); 

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);

    }

}
