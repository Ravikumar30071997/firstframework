package Exceptions;

public class URLMismatchException extends Exception {
	private static final long serialVersionUID = 1L;

	public URLMismatchException() {
		super("invalid URL");
	}
	
}
