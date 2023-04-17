package br.com.uboard.exceptions;

public class GroupNotFoundException extends Exception {

	private static final long serialVersionUID = 1L;

	public GroupNotFoundException() {

	}

	public GroupNotFoundException(String message) {
		super(message);
	}

	public GroupNotFoundException(String message, Exception e) {
		super(message, e);
	}

}
