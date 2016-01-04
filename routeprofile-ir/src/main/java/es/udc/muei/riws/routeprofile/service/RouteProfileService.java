package es.udc.muei.riws.routeprofile.service;

import java.util.Collection;

import es.udc.muei.riws.routeprofile.model.dto.FilterDTO;
import es.udc.muei.riws.routeprofile.model.dto.RouteDTO;

public interface RouteProfileService {

	public Collection<RouteDTO> findRoutes(Collection<FilterDTO> filters);

}
