package br.com.uboard.exceptions;

public class UserNotFoundException extends Exception {

	private static final long serialVersionUID = 1L;

	public UserNotFoundException() {

	}

	public UserNotFoundException(String message) {
		super(message);
	}

	public UserNotFoundException(String message, Exception e) {
		super(message, e);
	}

}
