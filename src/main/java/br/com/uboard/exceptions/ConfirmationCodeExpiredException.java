package br.com.uboard.exceptions;

public class ConfirmationCodeExpiredException extends Exception {

	private static final long serialVersionUID = 1L;

	public ConfirmationCodeExpiredException() {

	}

	public ConfirmationCodeExpiredException(String message) {
		super(message);
	}

	public ConfirmationCodeExpiredException(String message, Exception e) {
		super(message, e);
	}

}
