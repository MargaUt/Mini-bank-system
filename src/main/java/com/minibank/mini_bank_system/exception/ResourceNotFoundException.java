package com.minibank.mini_bank_system.exception;

/**
 * Exception thrown when a requested resource is not found in the system.
 */
public class ResourceNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public ResourceNotFoundException(String message, Long id) {
        super(String.format("%s ID: %d", message, id));
    }

	public ResourceNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}
}