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

	private IRDao irDao = null;

	public RouteProfileServiceIR() {
		this.irDao = IRDaoFactory.createInstance();
	}

	@Override
	public Collection<RouteDTO> findRoutes(Collection<FilterDTO> filters) {
		return (irDao.findRoutes(filters));
	}

	@Override
	public UserDTO signUp(String username, String password) throws IRException {
		UserDTO newUser = new UserDTO(username, password);
		try {
			return (irDao.createUser(newUser));
		} catch (IRException e) {
			return (null);
		}
	}

	@Override
	public Collection<UserDTO> findUsers(Collection<String> usernames) throws IRException {
		return (irDao.findUsers(usernames));
	}

	@Override
	public void updateUser(UserDTO updatedUser) throws IRException {
		irDao.updateUser(updatedUser);
	}

	@Override
	public Collection<RouteDTO> findRoutesById(Collection<String> routeIds) throws IRException {
		return (irDao.findRoutesById(routeIds));
	}
}
