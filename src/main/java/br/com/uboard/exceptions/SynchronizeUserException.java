package br.com.uboard.exceptions;

public class SynchronizeUserException extends Exception {

	private static final long serialVersionUID = 1L;

	public SynchronizeUserException() {

	}

	public SynchronizeUserException(String message) {
		super(message);
	}

	public SynchronizeUserException(String message, Exception e) {
		super(message, e);
	}

}
