package es.udc.muei.riws.routeprofile.dao;

import java.util.Collection;

import es.udc.muei.riws.routeprofile.model.dto.FilterDTO;
import es.udc.muei.riws.routeprofile.model.dto.RouteDTO;
import es.udc.muei.riws.routeprofile.model.dto.UserDTO;
import es.udc.muei.riws.routeprofile.model.exception.IRException;

public interface IRDao {

	public Collection<RouteDTO> findRoutes(Collection<FilterDTO> filters);

	public UserDTO createUser(UserDTO newUser) throws IRException;

	public Collection<UserDTO> findUsers(Collection<String> usernames) throws IRException;

	public Collection<RouteDTO> findRoutesById(Collection<String> routeIds) throws IRException;
}
