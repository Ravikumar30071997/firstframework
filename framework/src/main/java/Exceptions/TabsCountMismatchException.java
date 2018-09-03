package Exceptions;

public class TabsCountMismatchException extends Exception {
	private static final long serialVersionUID = 1L;

	public TabsCountMismatchException() {
		super("Tabs count did not match");
	}

}
