package es.udc.muei.riws.routeprofile.model.dto;

import es.udc.muei.riws.routeprofile.util.FieldsEnum;

public class FilterRangeDTO extends FilterDTO {

    /**
     * 
     */
    private static final long serialVersionUID = 775940031829317192L;

    private String min;
    private String max;

    public FilterRangeDTO(FieldsEnum field, String min, String max) {
	super(field);
	this.min = min;
	this.max = max;
    }

    public String getMin() {
	return min;
    }

    public void setMin(String min) {
	this.min = min;
    }

    public String getMax() {
	return max;
    }

    public void setMax(String max) {
	this.max = max;
    }

}
