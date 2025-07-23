package com.diegoehg.onlinestore.exception;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String resourceName, String id) {
        super(resourceName + " with ID " + id + " not found.");
    }
}
