package es.udc.muei.riws.routeprofile.model.dto;

import java.io.Serializable;

public class LocationDTO implements Serializable {

	private static final long serialVersionUID = -4835666850363463771L;

	private double latitude;
	private double longitude;

	public LocationDTO() {
		super();
	}

	public LocationDTO(Double latitude, Double longitude) {
		super();
		this.latitude = latitude;
		this.longitude = longitude;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(latitude);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(longitude);
		result = prime * result + (int) (temp ^ (temp >>> 32));
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
		LocationDTO other = (LocationDTO) obj;
		if (Double.doubleToLongBits(latitude) != Double.doubleToLongBits(other.latitude))
			return false;
		if (Double.doubleToLongBits(longitude) != Double.doubleToLongBits(other.longitude))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "LocationDTO [latitude=" + latitude + ", longitude=" + longitude + "]";
	}

	public Double distance(LocationDTO location) {
		if (location == null)
			return (null);
		return (Math.sqrt(Math.pow(this.latitude - location.getLatitude(), (double) 2)
				+ Math.pow(this.longitude - location.getLongitude(), (double) 2)));
	}
}
