package br.com.uboard.exceptions;

public class SynchronizeGroupsException extends Exception {

	private static final long serialVersionUID = 1L;

	public SynchronizeGroupsException() {

	}

	public SynchronizeGroupsException(String message) {
		super(message);
	}

	public SynchronizeGroupsException(String message, Exception e) {
		super(message, e);
	}

}
