package es.udc.muei.riws.routeprofile.model.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

public class UserDTO implements Serializable {

	private static final long serialVersionUID = 4591410867096449831L;

	private String username = null;
	private String password = null;
	private Collection<String> routeIds = null;

	public UserDTO(String username, String password, Collection<String> routeIds) {
		super();
		this.username = username;
		this.password = password;
		this.routeIds = routeIds;
	}

	public UserDTO(String username, String password) {
		super();
		this.username = username;
		this.password = password;
		this.routeIds = new ArrayList<String>();
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Collection<String> getRouteIds() {
		return routeIds;
	}

	public void setRouteIds(Collection<String> routeIds) {
		this.routeIds = routeIds;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((username == null) ? 0 : username.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UserDTO other = (UserDTO) obj;
		if (username == null) {
			if (other.username != null)
				return false;
		} else if (!username.equals(other.username))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "UserDTO [username=" + username + ", password=" + password + ", routeIds=" + routeIds + "]";
	}

}
