package es.udc.muei.riws.routeprofile.model.dto;

import es.udc.muei.riws.routeprofile.util.FieldsEnum;

public class FilterRangeDTO extends FilterDTO {

    /**
     * 
     */
    private static final long serialVersionUID = 775940031829317192L;

    private Double min;
    private Double max;

    public FilterRangeDTO(FieldsEnum field, Double min, Double max) {
	super(field);
	this.min = min;
	this.max = max;
    }

    public Double getMin() {
	return min;
    }

    public void setMin(Double min) {
	this.min = min;
    }

    public Double getMax() {
	return max;
    }

    public void setMax(Double max) {
	this.max = max;
    }

}
