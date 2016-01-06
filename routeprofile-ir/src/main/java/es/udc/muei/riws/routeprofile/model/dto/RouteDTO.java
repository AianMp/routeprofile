package es.udc.muei.riws.routeprofile.model.dto;

import java.io.Serializable;

public class RouteDTO implements Serializable {

    private static final long serialVersionUID = -4281986308299917975L;

    private String id = null;
    private String distance = null;
    private Boolean looped = null;
    private String maxElevation = null;
    private String minElevation = null;
    private String elevationGainUp = null;
    private String elevationGainDown = null;
    private Boolean done = null;

    public RouteDTO(String id, String distance, Boolean looped, String maxElevation, String minElevation,
	    String elevationGainUp, String elevationGainDown, boolean done) {
	this.id = id;
	this.distance = distance;
	this.looped = looped;
	this.maxElevation = maxElevation;
	this.minElevation = minElevation;
	this.elevationGainUp = elevationGainUp;
	this.elevationGainDown = elevationGainDown;
	this.done = done;
    }

    public String getId() {
	return id;
    }

    public void setId(String id) {
	this.id = id;
    }

    public String getDistance() {
	return distance;
    }

    public void setDistance(String distance) {
	this.distance = distance;
    }

    public Boolean getLooped() {
	return looped;
    }

    public void setLooped(Boolean looped) {
	this.looped = looped;
    }

    public String getMaxElevation() {
	return maxElevation;
    }

    public void setMaxElevation(String maxElevation) {
	this.maxElevation = maxElevation;
    }

    public String getMinElevation() {
	return minElevation;
    }

    public void setMinElevation(String minElevation) {
	this.minElevation = minElevation;
    }

    public String getElevationGainUp() {
	return elevationGainUp;
    }

    public void setElevationGainUp(String elevationGainUp) {
	this.elevationGainUp = elevationGainUp;
    }

    public String getElevationGainDown() {
	return elevationGainDown;
    }

    public void setElevationGainDown(String elevationGainDown) {
	this.elevationGainDown = elevationGainDown;
    }

    public Boolean getDone() {
	return done;
    }

    public void setDone(Boolean done) {
	this.done = done;
    }

    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + ((id == null) ? 0 : id.hashCode());
	return result;
    }

    @Override
    public boolean equals(Object obj) {
	if (this == obj)
	    return true;
	if (obj == null)
	    return false;
	if (getClass() != obj.getClass())
	    return false;
	RouteDTO other = (RouteDTO) obj;
	if (id == null) {
	    if (other.id != null)
		return false;
	} else if (!id.equals(other.id))
	    return false;
	return true;
    }

    @Override
    public String toString() {
	return "RouteDTO [id=" + id + ", distance=" + distance + ", looped=" + looped + ", maxElevation=" + maxElevation
		+ ", minElevation=" + minElevation + ", elevationGainUp=" + elevationGainUp + ", elevationGainDown="
		+ elevationGainDown + ", done=" + done + "]";
    }

}
