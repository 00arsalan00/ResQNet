package com.resqnet.resqnet_backend.exception;

public class VolunteerAlreadyAssignedToIncidentException extends RuntimeException {
    public VolunteerAlreadyAssignedToIncidentException(String message) {
        super(message);
    }
}
