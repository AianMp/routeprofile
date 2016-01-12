package es.udc.muei.riws.routeprofile.model.exception;

public class DuplicateUserException extends Exception {

	private static final long serialVersionUID = 7064562564581497945L;
	private String username = null;

	public DuplicateUserException(String username) {
		this.username = username;
	}

	@Override
	public String getMessage() {
		return ("User with username " + username + " already exists");
	}
}
