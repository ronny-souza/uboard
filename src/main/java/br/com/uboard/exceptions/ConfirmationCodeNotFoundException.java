package br.com.uboard.exceptions;

public class ConfirmationCodeNotFoundException extends Exception {

	private static final long serialVersionUID = 1L;

	public ConfirmationCodeNotFoundException() {

	}

	public ConfirmationCodeNotFoundException(String message) {
		super(message);
	}

	public ConfirmationCodeNotFoundException(String message, Exception e) {
		super(message, e);
	}

}
