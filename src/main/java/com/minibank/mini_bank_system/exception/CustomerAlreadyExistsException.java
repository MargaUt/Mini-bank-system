package com.minibank.mini_bank_system.exception;

public class CustomerAlreadyExistsException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public CustomerAlreadyExistsException(String message) {
		super(message);
	}

	public CustomerAlreadyExistsException(String message, Throwable cause) {
		super(message, cause);
	}
}