package es.udc.muei.riws.routeprofile.service;

import java.util.Collection;

import es.udc.muei.riws.routeprofile.model.dto.FilterDTO;
import es.udc.muei.riws.routeprofile.model.dto.RouteDTO;
import es.udc.muei.riws.routeprofile.model.dto.UserDTO;
import es.udc.muei.riws.routeprofile.model.exception.IRException;

public interface RouteProfileService {

	public UserDTO signUp(String username, String password) throws IRException;

	public Collection<UserDTO> findUsers(Collection<String> usernames) throws IRException;

	public void updateUser(UserDTO updatedUser) throws IRException;

	public Collection<RouteDTO> findRoutesById(Collection<String> routeIds) throws IRException;

	public Collection<RouteDTO> findRoutes(Collection<FilterDTO> filters);

}
