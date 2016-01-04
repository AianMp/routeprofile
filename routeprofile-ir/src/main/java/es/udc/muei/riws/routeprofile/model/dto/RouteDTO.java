package es.udc.muei.riws.routeprofile.model.dto;

import java.io.Serializable;

public class RouteDTO implements Serializable {

	private static final long serialVersionUID = -4281986308299917975L;

	private String id = null;
	private Double distance = null;
	private Boolean looped = null;
	private Double maxElevation = null;
	private Double minElevation = null;
	private Double elevationGainUp = null;
	private Double elevationGainDown = null;
	private Boolean done = null;

	public RouteDTO(String id, Double distance, Boolean looped, Double maxElevation, Double minElevation,
			Double elevationGainUp, Double elevationGainDown) {
		this.id = id;
		this.distance = distance;
		this.looped = looped;
		this.maxElevation = maxElevation;
		this.minElevation = minElevation;
		this.elevationGainUp = elevationGainUp;
		this.elevationGainDown = elevationGainDown;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Double getDistance() {
		return distance;
	}

	public void setDistance(Double distance) {
		this.distance = distance;
	}

	public Boolean getLooped() {
		return looped;
	}

	public void setLooped(Boolean looped) {
		this.looped = looped;
	}

	public Double getMaxElevation() {
		return maxElevation;
	}

	public void setMaxElevation(Double maxElevation) {
		this.maxElevation = maxElevation;
	}

	public Double getMinElevation() {
		return minElevation;
	}

	public void setMinElevation(Double minElevation) {
		this.minElevation = minElevation;
	}

	public Double getElevationGainUp() {
		return elevationGainUp;
	}

	public void setElevationGainUp(Double elevationGainUp) {
		this.elevationGainUp = elevationGainUp;
	}

	public Double getElevationGainDown() {
		return elevationGainDown;
	}

	public void setElevationGainDown(Double elevationGainDown) {
		this.elevationGainDown = elevationGainDown;
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

}
