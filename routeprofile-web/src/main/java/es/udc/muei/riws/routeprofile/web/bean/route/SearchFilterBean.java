package es.udc.muei.riws.routeprofile.web.bean.route;

import java.util.ArrayList;
import java.util.Collection;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import es.udc.muei.riws.routeprofile.model.dto.FilterDTO;
import es.udc.muei.riws.routeprofile.model.dto.FilterRangeDTO;
import es.udc.muei.riws.routeprofile.model.dto.FilterValueDTO;
import es.udc.muei.riws.routeprofile.model.dto.RouteDTO;
import es.udc.muei.riws.routeprofile.model.dto.RouteProfileDTO;
import es.udc.muei.riws.routeprofile.model.dto.UserDTO;
import es.udc.muei.riws.routeprofile.model.exception.IRException;
import es.udc.muei.riws.routeprofile.util.FieldsEnum;
import es.udc.muei.riws.routeprofile.web.bean.util.BaseBean;

@ManagedBean(name = "searchFilterBean")
@ViewScoped
public class SearchFilterBean extends BaseBean {

	/**
     * 
     */
	private static final long serialVersionUID = 9004691753635927414L;

	private static final int COUNT = 100;

	private Collection<RouteDTO> routes = new ArrayList<RouteDTO>();
	private Collection<FilterDTO> filters = new ArrayList<FilterDTO>();
	private RouteProfileDTO routeProfile = new RouteProfileDTO();

	private FieldsEnum field;
	private FieldsEnum[] itemsField = FieldsEnum.values();
	private Double max;
	private Double min;
	private String value;

	@PostConstruct
	public void init() {
		field = FieldsEnum.PR_DISTANCE;
		min = (double) 20;
		max = (double) 90;
		addFilterRange();
		// loadList();
	}

	private void loadList() {
		try {
			field = null;
			max = null;
			min = null;
			value = null;
			routeProfile = super.getRouteProfileService().findRouteProfile(
					super.getSessionBean().getUser());
			if (routeProfile.getNumDone() == 0)
				routes = super.getRouteProfileService().findRoutes(
						this.sessionBean.getUser(), filters, COUNT);
			else
				routes = super.getRouteProfileService()
						.findRoutesRouteProfileScore(
								this.sessionBean.getUser(), filters,
								routeProfile, COUNT);
			System.out.println("Load " + routes.size() + " routes");
		} catch (IRException e) {
			super.print("No se ha podido realizar la búsqueda, comprueba los filtros y/o recargue la página");
		}
	}

	public void addFilterRange() {
		FilterDTO newFilter = null;
		newFilter = new FilterRangeDTO(field, min, max);
		filters.add(newFilter);
		loadList();
	}

	public void addFilterValue() {
		FilterDTO newFilter = null;
		newFilter = new FilterValueDTO(field, value);
		filters.add(newFilter);
		loadList();
	}

	public void doneRoute(String id) {
		try {
			UserDTO user = super.getSessionBean().getUser();
			user.addRoute(id);
			super.getSessionBean().setUser(
					super.getRouteProfileService().updateUser(user));
			loadList();
		} catch (IRException e) {
			super.print("No se ha podido añadir la ruta");
		}

	}

	public Collection<RouteDTO> getRoutes() {
		return routes;
	}

	public void setRoutes(Collection<RouteDTO> routes) {
		this.routes = routes;
	}

	public Collection<FilterDTO> getFilters() {
		return filters;
	}

	public void setFilters(Collection<FilterDTO> filters) {
		this.filters = filters;
	}

	public RouteProfileDTO getRouteProfile() {
		return routeProfile;
	}

	public void setRouteProfile(RouteProfileDTO routeProfile) {
		this.routeProfile = routeProfile;
	}

	public FieldsEnum getField() {
		return field;
	}

	public void setField(FieldsEnum field) {
		this.field = field;
	}

	public FieldsEnum[] getItemsField() {
		return itemsField;
	}

	public void setItemsField(FieldsEnum[] itemsField) {
		this.itemsField = itemsField;
	}

	public Double getMax() {
		return max;
	}

	public void setMax(Double max) {
		this.max = max;
	}

	public Double getMin() {
		return min;
	}

	public void setMin(Double min) {
		this.min = min;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public static int getCount() {
		return COUNT;
	}

	public void saveLocation() {
		String latitude = FacesContext.getCurrentInstance()
				.getExternalContext().getRequestParameterMap().get("latitude");
		String longitude = FacesContext.getCurrentInstance()
				.getExternalContext().getRequestParameterMap().get("longitude");
		System.out.println("Latitud = " + latitude);
		System.out.println("Longitude = " + longitude);
	}

}
