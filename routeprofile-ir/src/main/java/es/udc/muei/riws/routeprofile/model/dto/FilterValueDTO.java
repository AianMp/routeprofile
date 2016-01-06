package es.udc.muei.riws.routeprofile.model.dto;

import es.udc.muei.riws.routeprofile.util.FieldsEnum;

public class FilterValueDTO extends FilterDTO {

    /**
     * 
     */
    private static final long serialVersionUID = 775940031829317192L;
    private String value;

    public FilterValueDTO(FieldsEnum field, String value) {
	super(field);
	this.value = value;
    }

    public String getValue() {
	return value;
    }

    public void setValue(String value) {
	this.value = value;
    }

}
