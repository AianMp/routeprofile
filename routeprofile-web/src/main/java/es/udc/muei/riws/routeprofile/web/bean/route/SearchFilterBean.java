package es.udc.muei.riws.routeprofile.web.bean.route;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ValueChangeEvent;
import javax.management.InstanceNotFoundException;

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

	private static final long serialVersionUID = 9004691753635927414L;

	private static final int COUNT = 100;
	private static final Integer SCORE_BY_LOCATION = 0;
	private static final Integer SCORE_BY_ROUTEPROFILE = 1;

	private String resultSize;
	private Integer topNumber;
	private Collection<RouteDTO> routes = new ArrayList<RouteDTO>();
	private Collection<FilterDTO> filters = new ArrayList<FilterDTO>();
	private RouteProfileDTO routeProfile = new RouteProfileDTO();

	private FieldsEnum field;
	// private FieldsEnum[] itemsField = FieldsEnum.values();
	private Map<FieldsEnum, String> itemsField;
	private Double max;
	private Double min;
	private String value;

	// Geolocation ranking
	private Map<Integer, String> scoreTypes;
	private Integer scoreType;

	@PostConstruct
	public void init() {
		itemsField = new LinkedHashMap<FieldsEnum, String>();
		itemsField.put(FieldsEnum.PR_DISTANCE, FieldsEnum.PR_DISTANCE.getName());
		itemsField.put(FieldsEnum.PR_LOOP, FieldsEnum.PR_LOOP.getName());
		itemsField.put(FieldsEnum.PR_ELEVATION_MIN, FieldsEnum.PR_ELEVATION_MIN.getName());
		itemsField.put(FieldsEnum.PR_ELEVATION_MAX, FieldsEnum.PR_ELEVATION_MAX.getName());
		itemsField.put(FieldsEnum.PR_ELEVATION_GAIN_UP_HILL, FieldsEnum.PR_ELEVATION_GAIN_UP_HILL.getName());
		itemsField.put(FieldsEnum.PR_ELEVATION_GAIN_DOWN_HILL, FieldsEnum.PR_ELEVATION_GAIN_DOWN_HILL.getName());

		scoreTypes = new LinkedHashMap<Integer, String>();
		scoreTypes.put(SCORE_BY_LOCATION, "Proximidad");
		scoreTypes.put(SCORE_BY_ROUTEPROFILE, "Perfil de ruta");

		resultSize = "Todavía no se ha efectuado ninguna búsqueda";

		try {
			routeProfile = super.getRouteProfileService().findRouteProfile(super.getSessionBean().getUser());
			if (routeProfile.getNumDone() > 0)
				scoreType = SCORE_BY_ROUTEPROFILE;
			else
				scoreType = SCORE_BY_LOCATION;
		} catch (IRException e) {
			scoreType = SCORE_BY_LOCATION;
		}
		topNumber = 20;
		loadList();
	}

	public void loadList() {
		try {
			field = null;
			max = null;
			min = null;
			value = null;
			routeProfile = super.getRouteProfileService().findRouteProfile(super.getSessionBean().getUser());

			if (routeProfile.getNumDone() == 0 && sessionBean.getLocation() == null)
				routes = super.getRouteProfileService().findAllRoutes(this.sessionBean.getUser(), getCount());
			else {
				if (routeProfile.getNumDone() > 0 && scoreType.equals(SCORE_BY_ROUTEPROFILE)) {
					routes = super.getRouteProfileService().findRoutesRouteProfileScore(this.sessionBean.getUser(),
							filters, routeProfile, getCount());
					// setScoreType(RouteProfileScore.class);
				} else {
					routes = super.getRouteProfileService().findRoutesByLocationScore(this.sessionBean.getUser(),
							filters, sessionBean.getLocation(), getCount());
					// setScoreType(RouteLocationScore.class);
				}
			}
			resultSize = "Se han encontrado " + routes.size() + " resultados";
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
			UserDTO updatedUser = super.getRouteProfileService().updateUser(user);
			super.getSessionBean().setUser(updatedUser);
			loadList();
		} catch (IRException e) {
			super.print("No se ha podido añadir la ruta");
		} catch (InstanceNotFoundException e) {
			e.printStackTrace();
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

	public Map<FieldsEnum, String> getItemsField() {
		return (itemsField);
	}

	public void setItemsField(Map<FieldsEnum, String> itemsField) {
		this.itemsField = itemsField;
	}

	public Map<Integer, String> getScoreTypes() {
		return scoreTypes;
	}

	public void setScoreTypes(Map<Integer, String> scoreTypes) {
		this.scoreTypes = scoreTypes;
	}

	public Integer getScoreType() {
		return scoreType;
	}

	public void setScoreType(Integer scoreType) {
		this.scoreType = scoreType;
	}

	public void scoreTypeChanged(ValueChangeEvent e) {
		// assign new value to localeCode
		this.scoreType = Integer.parseInt(e.getNewValue().toString());
		loadList();
	}

	public String getResultSize() {
		return resultSize;
	}

	public void setResultSize(String resultSize) {
		this.resultSize = resultSize;
	}

	public Integer getTopNumber() {
		return topNumber;
	}

	public void setTopNumber(Integer topNumber) {
		this.topNumber = topNumber;
	}

	public void topNumberChanged(ValueChangeEvent e) {
		// assign new value to localeCode
		this.topNumber = Integer.parseInt(e.getNewValue().toString());
		loadList();
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

	public int getCount() {
		return (topNumber == null ? COUNT : topNumber);
	}

}
