package es.udc.muei.riws.routeprofile.model.dto;

import java.io.Serializable;

import es.udc.muei.riws.routeprofile.util.FieldsEnum;

public abstract class FilterDTO implements Serializable {

    private static final long serialVersionUID = -3047487266560703572L;
    protected FieldsEnum field;

    public FilterDTO(FieldsEnum field) {
	this.field = field;
    }

    public FieldsEnum getField() {
	return field;
    }

    public void setField(FieldsEnum field) {
	this.field = field;
    }

}
