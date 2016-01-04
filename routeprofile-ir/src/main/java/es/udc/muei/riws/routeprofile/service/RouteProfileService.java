package es.udc.muei.riws.routeprofile.service;

import java.util.Collection;

import es.udc.muei.riws.routeprofile.model.dto.FilterDTO;
import es.udc.muei.riws.routeprofile.model.dto.RouteDTO;
import es.udc.muei.riws.routeprofile.model.dto.UserDTO;

public interface RouteProfileService {

	public UserDTO signUp(String username, String password);

	public boolean updateUser(UserDTO updatedUser);

	public Collection<RouteDTO> findRoutes(Collection<FilterDTO> filters);

}
