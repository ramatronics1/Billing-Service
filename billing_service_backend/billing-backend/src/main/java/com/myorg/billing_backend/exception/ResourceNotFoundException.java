package com.myorg.billing_backend.exception;

/**
 * Simple runtime exception used when a requested entity is not found.
 */
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException() { super(); }

    public ResourceNotFoundException(String message) { super(message); }

    public ResourceNotFoundException(String message, Throwable cause) { super(message, cause); }

    /**
     * Convenience constructor used as: new ResourceNotFoundException("Customer", id)
     * Produces message like: "Customer with id 123 not found"
     */
    public ResourceNotFoundException(String resource, Object id) {
        super(formatMessage(resource, id));
    }

    /**
     * Convenience constructor with cause.
     */
    public ResourceNotFoundException(String resource, Object id, Throwable cause) {
        super(formatMessage(resource, id), cause);
    }

    private static String formatMessage(String resource, Object id) {
        return String.format("%s with id %s not found", resource != null ? resource : "Resource",
                id != null ? id.toString() : "null");
    }
}
