package es.udc.muei.riws.routeprofile.web.bean.user;

import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.management.InstanceNotFoundException;

import es.udc.muei.riws.routeprofile.model.dto.LocationDTO;
import es.udc.muei.riws.routeprofile.model.dto.UserDTO;
import es.udc.muei.riws.routeprofile.model.exception.DuplicateUserException;
import es.udc.muei.riws.routeprofile.model.exception.IRException;
import es.udc.muei.riws.routeprofile.web.bean.util.BaseBean;

@ManagedBean(name = "loginBean")
@ViewScoped
public class LoginBean extends BaseBean {

	private static final long serialVersionUID = -5573857285202274092L;
	private String userName;
	private String password;

	private LocationDTO location;

	@PostConstruct
	public void init() {

	}

	public String login() {
		UserDTO user;
		try {
			user = super.getRouteProfileService().findUser(userName);
		} catch (InstanceNotFoundException e) {
			FacesContext.getCurrentInstance().addMessage("loginForm",
					new FacesMessage("El usuario especificado no existe. Por favor, regístrese previamente"));
			return (null);
		} catch (IRException e) {
			FacesContext.getCurrentInstance().addMessage("loginForm",
					new FacesMessage("Error del servidor: no se ha podido acceder a los usuarios"));
			e.printStackTrace();
			return (null);
		}
		if (user == null)
			return (null);
		if (user.getPassword() == null || !user.getPassword().equals(password)) {
			FacesContext.getCurrentInstance().addMessage("loginForm",
					new FacesMessage("La contraseña no se corresponde con el nombre de usuario"));
			return (null);
		}
		sessionBean.setUser(user);
		Map<String, Object> sessionMap = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
		sessionMap.put("isLogin", true);
		sessionMap.put("isLogin", true);
		sessionBean.setLocation(location);
		return "/route/searchFilter.xhtml?faces-redirect=true";
	}

	public String createUser() {
		UserDTO user;
		try {
			user = super.getRouteProfileService().signUp(userName, password);
			sessionBean.setUser(user);
			Map<String, Object> sessionMap = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
			sessionMap.put("isLogin", true);
			sessionMap.put("isLogin", true);
			sessionBean.setLocation(location);
			return ("/route/searchFilter.xhtml?faces-redirect=true");
		} catch (DuplicateUserException e) {
			FacesContext.getCurrentInstance().addMessage("loginForm", new FacesMessage(e.getMessage()));
			return (null);
		} catch (IRException e) {
			FacesContext.getCurrentInstance().addMessage("loginForm",
					new FacesMessage("Error del servidor: no se ha podido acceder a los usuarios"));
			e.printStackTrace();
			return (null);
		}
	}

	public String logout() {
		FacesContext.getCurrentInstance().getExternalContext().getSessionMap().remove("isLogin");
		return null;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public void saveLocation() {
		double latitude = Double.parseDouble(FacesContext.getCurrentInstance().getExternalContext()
				.getRequestParameterMap().get("latitude"));
		double longitude = Double.parseDouble(FacesContext.getCurrentInstance().getExternalContext()
				.getRequestParameterMap().get("longitude"));
		this.location = new LocationDTO(latitude, longitude);
	}
}
