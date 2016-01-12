package es.udc.muei.riws.routeprofile.util;

public enum FieldsEnum {
	url, PR_DISTANCE, PR_LOOP, PR_ELEVATION_GAIN_UP_HILL, PR_ELEVATION_MAX, PR_ELEVATION_GAIN_DOWN_HILL, PR_ELEVATION_MIN, PR_LATITUDE, PR_LONGITUDE;

	public String getName() {
		if (this == PR_DISTANCE)
			return ("Distancia");
		if (this == PR_LOOP)
			return ("Circular");
		if (this == PR_ELEVATION_GAIN_UP_HILL)
			return ("Desnivel acumulado subiendo");
		if (this == PR_ELEVATION_GAIN_DOWN_HILL)
			return ("Desnivel acumulado bajando");
		if (this == PR_ELEVATION_MIN)
			return ("Altura mínima");
		if (this == PR_ELEVATION_MAX)
			return ("Altura máxima");
		return (null);
	}

}
