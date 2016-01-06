package es.udc.muei.riws.routeprofile.web.bean.route;

import java.util.ArrayList;
import java.util.Collection;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import es.udc.muei.riws.routeprofile.model.dto.FilterDTO;
import es.udc.muei.riws.routeprofile.model.dto.FilterRangeDTO;
import es.udc.muei.riws.routeprofile.model.dto.FilterValueDTO;
import es.udc.muei.riws.routeprofile.model.dto.RouteDTO;
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

    private static final int COUNT = 10;

    private Collection<RouteDTO> routes = new ArrayList<RouteDTO>();
    private Collection<FilterDTO> filters = new ArrayList<FilterDTO>();

    private FieldsEnum field;
    private FieldsEnum[] itemsField = FieldsEnum.values();
    private String max;
    private String min;
    private String value;

    @PostConstruct
    public void init() {
	loadList();
    }

    private void loadList() {
	try {
	    routes = super.getRouteProfileService().findRoutes(this.sessionBean.getUser(), filters, COUNT);
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

    public FieldsEnum getField() {
	return field;
    }

    public void setField(FieldsEnum field) {
	this.field = field;
    }

    public String getMax() {
	return max;
    }

    public void setMax(String max) {
	this.max = max;
    }

    public String getMin() {
	return min;
    }

    public void setMin(String min) {
	this.min = min;
    }

    public String getValue() {
	return value;
    }

    public void setValue(String value) {
	this.value = value;
    }

    public FieldsEnum[] getItemsField() {
	return itemsField;
    }

    public void setItemsField(FieldsEnum[] itemsField) {
	this.itemsField = itemsField;
    }

}
