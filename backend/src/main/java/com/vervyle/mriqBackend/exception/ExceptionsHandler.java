package com.vervyle.mriqBackend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionsHandler {
    @ExceptionHandler(EmptyDirectoryException.class)
    public ResponseEntity<Object> handleEmptyDirectoryException
            (EmptyDirectoryException emptyDirectoryException) {
        MedExException medExException = new MedExException(
                emptyDirectoryException.getMessage(),
                emptyDirectoryException.getCause(),
                HttpStatus.NOT_FOUND
        );
        return new ResponseEntity<>(medExException, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidDirectoryNameException.class)
    public ResponseEntity<Object> handleInvalidDirectoryException
            (InvalidDirectoryNameException invalidDirectoryNameException) {
        MedExException medExException = new MedExException(
                invalidDirectoryNameException.getMessage(),
                invalidDirectoryNameException.getCause(),
                HttpStatus.BAD_REQUEST
        );
        return new ResponseEntity<>(medExException, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IOProcessingException.class)
    public ResponseEntity<Object> handleIOProcessingException
            (IOProcessingException ioProcessingException) {
        MedExException medExException = new MedExException(
                ioProcessingException.getMessage(),
                ioProcessingException.getCause(),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
        return new ResponseEntity<>(medExException, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
