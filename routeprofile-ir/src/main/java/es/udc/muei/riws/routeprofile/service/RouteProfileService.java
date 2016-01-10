package es.udc.muei.riws.routeprofile.service;

import java.util.Collection;

import es.udc.muei.riws.routeprofile.model.dto.FilterDTO;
import es.udc.muei.riws.routeprofile.model.dto.LocationDTO;
import es.udc.muei.riws.routeprofile.model.dto.RouteDTO;
import es.udc.muei.riws.routeprofile.model.dto.RouteProfileDTO;
import es.udc.muei.riws.routeprofile.model.dto.UserDTO;
import es.udc.muei.riws.routeprofile.model.exception.IRException;

public interface RouteProfileService {

    public UserDTO signUp(String username, String password);

    public Collection<UserDTO> findUsers(Collection<String> usernames) throws IRException;

    public UserDTO findUser(String username) throws IRException;

    public UserDTO updateUser(UserDTO updatedUser) throws IRException;

    public Collection<RouteDTO> findRoutesById(UserDTO user, Collection<String> routeIds) throws IRException;

    public Collection<RouteDTO> findRoutes(UserDTO user, Collection<FilterDTO> filters, int count) throws IRException;

    public Collection<RouteDTO> findAllRoutes(UserDTO user, int topCount) throws IRException;

    public RouteProfileDTO findRouteProfile(UserDTO user) throws IRException;

    Collection<RouteDTO> findRoutesRouteProfileScore(UserDTO user, Collection<FilterDTO> filters,
	    RouteProfileDTO routeProfile, int count) throws IRException;
    
	public Collection<RouteDTO> findRoutesByLocationScore(UserDTO user, Collection<FilterDTO> filters,
			LocationDTO location, int count) throws IRException;
}
