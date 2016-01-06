package es.udc.muei.riws.routeprofile.model.exception;

public class IRException extends Exception {

	private static final long serialVersionUID = 2532242879531298830L;
	private String message = null;
	private Exception e = null;

	public IRException(String message, Exception e) {
		this.message = message;
		this.e = e;
	}

	public String getMessage() {
		return (this.message);
	}

	public Exception getException() {
		return (this.e);
	}

	public void printStackTrace() {
		e.printStackTrace();
	}
}
