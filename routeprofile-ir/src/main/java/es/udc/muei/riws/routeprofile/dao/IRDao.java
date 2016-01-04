package es.udc.muei.riws.routeprofile.dao;

import java.util.Collection;

import es.udc.muei.riws.routeprofile.model.dto.FilterDTO;
import es.udc.muei.riws.routeprofile.model.dto.RouteDTO;

public interface IRDao {

	public Collection<RouteDTO> findRoutes(Collection<FilterDTO> filters);
}
