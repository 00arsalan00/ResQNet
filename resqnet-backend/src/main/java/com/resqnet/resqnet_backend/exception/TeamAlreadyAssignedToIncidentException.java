package com.resqnet.resqnet_backend.exception;

public class TeamAlreadyAssignedToIncidentException extends RuntimeException {
    public TeamAlreadyAssignedToIncidentException(String message) {
        super(message);
    }
}
