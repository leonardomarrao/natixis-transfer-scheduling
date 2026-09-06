package com.natixis.scheduling.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<StandardErrorDTO> handleResourceNotFound(ResourceNotFoundException e, HttpServletRequest request) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        StandardErrorDTO error = new StandardErrorDTO(
            Instant.now(), 
            status.value(), 
            "Recurso Não Encontrado", 
            e.getMessage(), 
            request.getRequestURI()
        );
        return ResponseEntity.status(status).body(error);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<StandardErrorDTO> handleIllegalArgument(IllegalArgumentException e, HttpServletRequest request) {
        HttpStatus status = HttpStatus.UNPROCESSABLE_ENTITY;
        StandardErrorDTO error = new StandardErrorDTO(
            Instant.now(), 
            status.value(), 
            "Regra de Negócio Violada", 
            e.getMessage(), 
            request.getRequestURI()
        );
        return ResponseEntity.status(status).body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<StandardErrorDTO> handleValidation(MethodArgumentNotValidException e, HttpServletRequest request) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        String firstError = e.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        StandardErrorDTO error = new StandardErrorDTO(
            Instant.now(), 
            status.value(), 
            "Erro de Validação", 
            firstError, 
            request.getRequestURI()
        );
        return ResponseEntity.status(status).body(error);
    }
}