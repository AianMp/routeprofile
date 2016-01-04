package es.udc.muei.riws.routeprofile.service.impl;

import java.util.Collection;

import es.udc.muei.riws.routeprofile.dao.IRDao;
import es.udc.muei.riws.routeprofile.dao.IRDaoFactory;
import es.udc.muei.riws.routeprofile.model.dto.FilterDTO;
import es.udc.muei.riws.routeprofile.model.dto.RouteDTO;
import es.udc.muei.riws.routeprofile.model.dto.UserDTO;
import es.udc.muei.riws.routeprofile.model.exception.IRException;
import es.udc.muei.riws.routeprofile.service.RouteProfileService;

public class RouteProfileServiceIR implements RouteProfileService {

	@Override
	public Collection<RouteDTO> findRoutes(Collection<FilterDTO> filters) {
		IRDao dao = IRDaoFactory.createInstance();
		return (dao.findRoutes(filters));
	}

	@Override
	public UserDTO signUp(String username, String password) {
		UserDTO newUser = new UserDTO(username, password);
		try {
			return (IRDaoFactory.createInstance().createUser(newUser));
		} catch (IRException e) {
			return (null);
		}
	}

	@Override
	public boolean updateUser(UserDTO updatedUser) {
		return false;
		// TODO Auto-generated method stub

	}
}
