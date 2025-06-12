package edu.uscb.csci470sp25.brighten_up_backend.exception;

public class UserPostNotFoundException extends RuntimeException {
	private static final long serialVersionUID = 582237545785868420L;

	public UserPostNotFoundException(Long id) {
		super("Could not find the user post with ID " + id);
	}

	// added for unit testing
	public UserPostNotFoundException(String message) {
		super(message);
	}
} // end class UserPostNotFoundException