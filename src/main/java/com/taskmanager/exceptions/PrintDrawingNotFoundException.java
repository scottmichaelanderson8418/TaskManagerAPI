package com.taskmanager.exceptions;


public class PrintDrawingNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L; // Recommended for serializable classes

	
	public PrintDrawingNotFoundException(String message) {
		super(message);
	}
}
