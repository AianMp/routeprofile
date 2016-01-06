package es.udc.muei.riws.routeprofile.web.bean.user;

import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import es.udc.muei.riws.routeprofile.model.dto.UserDTO;
import es.udc.muei.riws.routeprofile.model.exception.IRException;
import es.udc.muei.riws.routeprofile.web.bean.util.BaseBean;

@ManagedBean(name = "loginBean")
@ViewScoped
public class LoginBean extends BaseBean {

    /**
     * 
     */
    private static final long serialVersionUID = -5573857285202274092L;
    private String userName;
    private String password;

    @PostConstruct
    public void init() {

    }

    public String login() {
	UserDTO user;
	try {
	    // TODO Comprobar password
	    user = super.getRouteProfileService().findUser(userName);
	} catch (IRException e) {
	    user = super.getRouteProfileService().signUp(userName, password);
	}
	if (user == null) {
	    return null;
	}
	sessionBean.setUser(user);
	Map<String, Object> sessionMap = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
	sessionMap.put("isLogin", true);
	sessionMap.put("isLogin", true);
	return "/route/searchFilter.xhtml?faces-redirect=true";
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

}
