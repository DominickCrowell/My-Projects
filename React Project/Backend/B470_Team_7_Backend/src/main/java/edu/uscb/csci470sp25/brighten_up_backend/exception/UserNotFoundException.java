package edu.uscb.csci470sp25.brighten_up_backend.exception;

public class UserNotFoundException extends RuntimeException {
	private static final long serialVersionUID = 582237545785868420L;

	public UserNotFoundException(Long id) {
		super("Could not find the user with ID " + id);
	}

	// added for unit testing
	public UserNotFoundException(String message) {
		super(message);
	}
} // end class UserNotFoundException