package com.resqnet.resqnet_backend.exception;

public class ResourceUnavailableException extends ResourceNotFoundException {
    public ResourceUnavailableException(String message) {
        super(message);
    }
}
