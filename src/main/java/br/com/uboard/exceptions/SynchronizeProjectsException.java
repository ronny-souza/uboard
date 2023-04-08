package br.com.uboard.exceptions;

public class SynchronizeProjectsException extends Exception {

	private static final long serialVersionUID = 1L;

	public SynchronizeProjectsException() {

	}

	public SynchronizeProjectsException(String message) {
		super(message);
	}

	public SynchronizeProjectsException(String message, Exception e) {
		super(message, e);
	}

}
