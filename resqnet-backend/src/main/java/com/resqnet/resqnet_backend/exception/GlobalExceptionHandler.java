package com.resqnet.resqnet_backend.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(IncidentNotFoundException.class)
    public ResponseEntity<ApiError> handleIncidentNotFound(
            IncidentNotFoundException ex,
            HttpServletRequest request) {

        return buildError(HttpStatus.NOT_FOUND, ex.getMessage(), request);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiError> handleInvalidId(
            MethodArgumentTypeMismatchException ex,
            HttpServletRequest request) {

        return buildError(HttpStatus.BAD_REQUEST,
                "Invalid value for parameter: " + ex.getName(),
                request);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleException(Exception ex,HttpServletRequest request){
        ex.printStackTrace();

        return buildError(HttpStatus.INTERNAL_SERVER_ERROR,
                "Internal Server Error",
                request);
    }

    @ExceptionHandler(RescueTeamNotFoundException.class)
    public ResponseEntity<ApiError> handleRescueTeamNotFound(
            RescueTeamNotFoundException ex,
            HttpServletRequest request) {

        return buildError(HttpStatus.NOT_FOUND, ex.getMessage(), request);
    }

    @ExceptionHandler(TeamAlreadyAssignedToIncidentException.class)
    public ResponseEntity<ApiError> handleTeamAlreadyAssignToIncident(
            TeamAlreadyAssignedToIncidentException ex,
            HttpServletRequest request
    ){
        return buildError(HttpStatus.CONFLICT, ex.getMessage(), request);
    }

    @ExceptionHandler(CapacityExceededException.class)
    public ResponseEntity<ApiError> handleCapacityExceeded(
            CapacityExceededException ex,
            HttpServletRequest request
    ){
        return buildError(HttpStatus.BAD_REQUEST, ex.getMessage(), request);
    }

    @ExceptionHandler(AssignmentNotFound.class)
    public ResponseEntity<ApiError> handleAssignmentNotFound(
            AssignmentNotFound assignmentNotFound,
            HttpServletRequest request
    ){
        return buildError(HttpStatus.NOT_FOUND, assignmentNotFound.getMessage(), request);
    }

    @ExceptionHandler(org.springframework.web.bind.MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidationException(
            org.springframework.web.bind.MethodArgumentNotValidException ex,
            HttpServletRequest request) {

        String message = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .findFirst()
                .orElse("Validation error");

        return buildError(HttpStatus.BAD_REQUEST, message, request);
    }

    private ResponseEntity<ApiError> buildError(HttpStatus status,
                                                String message,
                                                HttpServletRequest request) {

        ApiError error = ApiError.builder()
                .status(status.value())
                .message(message)
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.status(status).body(error);
    }


}
