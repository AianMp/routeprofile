package es.udc.muei.riws.routeprofile.web.bean.util;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import es.udc.muei.riws.routeprofile.model.dto.LocationDTO;
import es.udc.muei.riws.routeprofile.model.dto.UserDTO;

@ManagedBean(name = "sessionBean")
@SessionScoped
public class SessionBean implements Serializable {

	/**
     * 
     */
	private static final long serialVersionUID = -6367700316910096939L;

	private UserDTO user;
	private LocationDTO location;

	public SessionBean() {
	}

	public SessionBean(UserDTO user) {
		this.user = user;
	}

	public UserDTO getUser() {
		return user;
	}

	public void setUser(UserDTO user) {
		this.user = user;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public void setLocation(LocationDTO location) {
		this.location = location;
	}

	public LocationDTO getLocation() {
		return (location);
	}
}
