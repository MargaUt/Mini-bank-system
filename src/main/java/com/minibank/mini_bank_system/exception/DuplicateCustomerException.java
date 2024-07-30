package com.minibank.mini_bank_system.exception;

/**
 * Exception thrown when a customer with the same name, lastname, or email
 * already exists for an account.
 */
public class DuplicateCustomerException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	// Constructor with a message
	public DuplicateCustomerException(String message) {
		super(message);
	}

	// Constructor with a message and a cause
	public DuplicateCustomerException(String message, Throwable cause) {
		super(message, cause);
	}

	// Constructor with a cause
	public DuplicateCustomerException(Throwable cause) {
		super(cause);
	}
}