package Exceptions;

public class TitleMismatchException extends Exception {
	private static final long serialVersionUID = 1L;

	public TitleMismatchException() {
		super("invalid title");
	}

}
