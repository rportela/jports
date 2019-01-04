package jports;

public class ShowStopper extends RuntimeException {

	private static final long serialVersionUID = 4834845300038022934L;

	public ShowStopper(Throwable cause) {
		super(cause);
	}

	public ShowStopper(String message, Throwable cause) {
		super(message, cause);
	}

	public ShowStopper(String message) {
		super(message);
	}

}
