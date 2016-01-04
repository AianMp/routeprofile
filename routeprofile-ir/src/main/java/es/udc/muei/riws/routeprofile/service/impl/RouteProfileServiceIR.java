package es.udc.muei.riws.routeprofile.service.impl;

import java.util.Collection;

import es.udc.muei.riws.routeprofile.dao.IRDao;
import es.udc.muei.riws.routeprofile.dao.IRDaoFactory;
import es.udc.muei.riws.routeprofile.model.dto.FilterDTO;
import es.udc.muei.riws.routeprofile.model.dto.RouteDTO;
import es.udc.muei.riws.routeprofile.service.RouteProfileService;

public class RouteProfileServiceIR implements RouteProfileService {

	@Override
	public Collection<RouteDTO> findRoutes(Collection<FilterDTO> filters) {
		IRDao dao = IRDaoFactory.createInstance();
		return (dao.findRoutes(filters));
	}
}
