package com.example.MedhirBackend.exception;

import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ValidationException.class)
    public StatusRuntimeException handleValidationException(ValidationException ex) {
        return Status.INVALID_ARGUMENT
                .withDescription(ex.getField() + ": " + ex.getMessage())
                .asRuntimeException();
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public StatusRuntimeException handleConstraintViolationException(ConstraintViolationException ex) {
        return Status.INVALID_ARGUMENT
                .withDescription("Validation failed: " + ex.getMessage())
                .asRuntimeException();
    }

    @ExceptionHandler(Exception.class)
    public StatusRuntimeException handleGenericException(Exception ex) {
        return Status.INTERNAL
                .withDescription("An unexpected error occurred: " + ex.getMessage())
                .asRuntimeException();
    }
}
