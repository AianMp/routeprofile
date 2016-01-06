package es.udc.muei.riws.routeprofile.web.bean.util;

import java.io.Serializable;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedProperty;
import javax.faces.context.FacesContext;

import es.udc.muei.riws.routeprofile.service.RouteProfileService;
import es.udc.muei.riws.routeprofile.service.RouteProfileServiceFactory;

public class BaseBean implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 5620471093554008119L;
    @ManagedProperty("#{sessionBean}")
    protected SessionBean sessionBean;
    protected RouteProfileService routeProfileService;

    public RouteProfileService getRouteProfileService() {
	if (routeProfileService == null)
	    this.routeProfileService = RouteProfileServiceFactory.createInstance();
	return routeProfileService;
    }

    public void setRouteProfileService(RouteProfileService routeProfileService) {
	this.routeProfileService = routeProfileService;
    }

    public SessionBean getSessionBean() {
	return sessionBean;
    }

    public void setSessionBean(SessionBean sessionBean) {
	this.sessionBean = sessionBean;
    }

    protected void print(String menssage) {
	FacesMessage facesMessage = new FacesMessage(menssage);
	FacesContext.getCurrentInstance().addMessage(null, facesMessage);
    }
}
