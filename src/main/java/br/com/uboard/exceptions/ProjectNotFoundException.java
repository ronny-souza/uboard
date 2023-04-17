package br.com.uboard.exceptions;

public class ProjectNotFoundException extends Exception {

	private static final long serialVersionUID = 1L;

	public ProjectNotFoundException() {

	}

	public ProjectNotFoundException(String message) {
		super(message);
	}

	public ProjectNotFoundException(String message, Exception e) {
		super(message, e);
	}

}
