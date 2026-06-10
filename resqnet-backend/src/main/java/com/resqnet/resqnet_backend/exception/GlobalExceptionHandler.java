package com.resqnet.resqnet_backend.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.time.LocalDateTime;
import java.util.UUID;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);
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

        String message = buildParameterErrorMessage(ex);
        return buildError(HttpStatus.BAD_REQUEST, message, request);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ApiError> handleMissingParameter(
            MissingServletRequestParameterException ex,
            HttpServletRequest request) {

        return buildError(HttpStatus.BAD_REQUEST,
                "Missing required parameter: " + ex.getParameterName(),
                request);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ApiError> handleMethodNotSupported(
            HttpRequestMethodNotSupportedException ex,
            HttpServletRequest request) {

        return buildError(HttpStatus.METHOD_NOT_ALLOWED,
                "HTTP method not supported for this endpoint",
                request);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ApiError> handleNoResourceFound(
            NoResourceFoundException ex,
            HttpServletRequest request) {

        return buildError(HttpStatus.NOT_FOUND,
                "Endpoint not found. Check the URL path and ensure all ID path variables are set.",
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

    @ExceptionHandler(InvalidStatusTransitionException.class)
    public ResponseEntity<ApiError> handleInvalidStatusTransition(
            InvalidStatusTransitionException ex,
            HttpServletRequest request
    ){
        return buildError(HttpStatus.BAD_REQUEST, ex.getMessage(), request);
    }

    @ExceptionHandler(VolunteerNotFoundException.class)
    public ResponseEntity<ApiError> handleVolunteerNotFound(
            VolunteerNotFoundException ex,
            HttpServletRequest request
    ){
        return buildError(HttpStatus.NOT_FOUND, ex.getMessage(), request);
    }

    @ExceptionHandler(VolunteerAlreadyAssignedToIncidentException.class)
    public ResponseEntity<ApiError> handleVolunteerAlreadyAssignedToIncident(
            VolunteerAlreadyAssignedToIncidentException ex,
            HttpServletRequest request
    ){
        return buildError(HttpStatus.CONFLICT, ex.getMessage(), request);
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

    @ExceptionHandler(CampNotFoundException.class)
    public ResponseEntity<ApiError> handleCampNotFound(
            CampNotFoundException ex,
            HttpServletRequest request
    ){
        return buildError(HttpStatus.NOT_FOUND, ex.getMessage(), request);
    }

    @ExceptionHandler(InvalidOperationException.class)
    public ResponseEntity<ApiError> handleInvalidOperation(
            InvalidOperationException ex,
            HttpServletRequest request
    ){
        return buildError(HttpStatus.BAD_REQUEST, ex.getMessage(), request);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiError> handleResourceNotFound(
            ResourceNotFoundException ex,
            HttpServletRequest request
    ){
        return buildError(HttpStatus.NOT_FOUND, ex.getMessage(), request);
    }

    @ExceptionHandler(ResourceUnavailableException.class)
    public ResponseEntity<ApiError> handleResourceUnavailable(
            ResourceUnavailableException ex,
            HttpServletRequest request
    ){
        return buildError(HttpStatus.SERVICE_UNAVAILABLE, ex.getMessage(), request);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleException(Exception ex, HttpServletRequest request) {
        log.error("Unhandled exception on {}", request.getRequestURI(), ex);
        return buildError(HttpStatus.INTERNAL_SERVER_ERROR,
                "Internal Server Error",
                request);
    }

    private String buildParameterErrorMessage(MethodArgumentTypeMismatchException ex) {
        String name = ex.getName();
        Object value = ex.getValue();

        if (ex.getRequiredType() == UUID.class
                && (value == null || value.toString().isBlank())) {
            return "Missing or invalid UUID for parameter: " + name
                    + ". Ensure the path variable is set (e.g. incidentId, teamId, campId).";
        }

        return "Invalid value for parameter: " + name;
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
