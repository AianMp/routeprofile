package es.udc.muei.riws.routeprofile.model.dto;

import java.io.Serializable;

public class RouteProfileDTO implements Serializable {

    private static final long serialVersionUID = -4281986308299917975L;

    private Double distance = (double) 0;
    private Double ratioElevation = (double) 0;
    private Double ratioGainUp = (double) 0;
    private Double ratioGainDown = (double) 0;
    private int numDone = 0;
    private float ratio;

    public RouteProfileDTO() {
	super();
    }

    public RouteProfileDTO(RouteDTO r) {
	calculate(r.getDistance(), r.getMaxElevation(), r.getMinElevation(), r.getElevationGainUp(),
		r.getElevationGainDown());
	this.ratio = this.ratio();
    }

    public RouteProfileDTO(Double distance, Double maxElevation, Double minElevation, Double elevationGainUp,
	    Double elevationGainDown) {
	calculate(distance, maxElevation, minElevation, elevationGainUp, elevationGainDown);
    }

    public Double getDistance() {
	return distance;
    }

    public void setDistance(Double distance) {
	this.distance = distance;
    }

    public Double getRatioElevation() {
	return ratioElevation;
    }

    public void setRatioElevation(Double ratioElevation) {
	this.ratioElevation = ratioElevation;
    }

    public Double getRatioGainUp() {
	return ratioGainUp;
    }

    public void setRatioGainUp(Double rationGainUp) {
	this.ratioGainUp = rationGainUp;
    }

    public Double getRatioGainDown() {
	return ratioGainDown;
    }

    public void setRatioGainDown(Double ratioGainDown) {
	this.ratioGainDown = ratioGainDown;
    }

    public int getNumDone() {
	return numDone;
    }

    public void setNumDone(int numDone) {
	this.numDone = numDone;
    }

    public float getRatio() {
	return ratio;
    }

    public void setRatio(float ratio) {
	this.ratio = ratio;
    }

    public void calculate(Double distance, Double maxElevation, Double minElevation, Double elevationGainUp,
	    Double elevationGainDown) {
	this.distance = distance;
	this.ratioElevation = maxElevation - minElevation;
	this.ratioGainUp = elevationGainUp / this.distance;
	this.ratioGainDown = elevationGainDown / this.distance;
    }

    public void plus(RouteProfileDTO routeProfile) {
	this.distance += routeProfile.getDistance();
	this.ratioElevation += routeProfile.getRatioElevation();
	this.ratioGainUp += routeProfile.getRatioGainUp();
	this.ratioGainDown += routeProfile.getRatioGainDown();
	this.numDone++;
    }

    public void avg() {
	this.distance = this.distance / this.numDone;
	this.ratioElevation = this.ratioElevation / this.numDone;
	this.ratioGainUp = this.ratioGainUp / this.numDone;
	this.ratioGainDown = this.ratioGainDown / this.numDone;
	this.ratio = this.ratio();
    }

    public float ratio() {
	float auxElevation = (float) Math.abs(ratioElevation + (ratioGainUp - ratioGainDown));
	if (auxElevation <= 1)
	    return distance.floatValue();
	return (float) (distance / auxElevation);
    }

}
