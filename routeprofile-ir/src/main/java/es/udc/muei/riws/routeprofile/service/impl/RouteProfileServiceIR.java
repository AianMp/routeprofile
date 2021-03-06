package es.udc.muei.riws.routeprofile.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import javax.management.InstanceNotFoundException;

import org.apache.lucene.queryparser.classic.ParseException;

import es.udc.muei.riws.routeprofile.dao.IRDao;
import es.udc.muei.riws.routeprofile.dao.IRDaoFactory;
import es.udc.muei.riws.routeprofile.model.dto.FilterDTO;
import es.udc.muei.riws.routeprofile.model.dto.LocationDTO;
import es.udc.muei.riws.routeprofile.model.dto.RouteDTO;
import es.udc.muei.riws.routeprofile.model.dto.RouteProfileDTO;
import es.udc.muei.riws.routeprofile.model.dto.UserDTO;
import es.udc.muei.riws.routeprofile.model.exception.DuplicateUserException;
import es.udc.muei.riws.routeprofile.model.exception.IRException;
import es.udc.muei.riws.routeprofile.service.RouteProfileService;

public class RouteProfileServiceIR implements RouteProfileService {

	public RouteProfileServiceIR() {
	}

	@Override
	public Collection<RouteDTO> findRoutes(UserDTO user, Collection<FilterDTO> filters, int count) throws IRException {
		IRDao dao = IRDaoFactory.createInstance();
		return dao.findRoutes(user, filters, count);
	}

	@Override
	public Collection<RouteDTO> findRoutesRouteProfileScore(UserDTO user, Collection<FilterDTO> filters,
			RouteProfileDTO routeProfile, int count) throws IRException {
		IRDao dao = IRDaoFactory.createInstance();
		return dao.findRoutesRouteProfileScore(user, filters, routeProfile, count);
	}

	@Override
	public Collection<RouteDTO> findRoutesByLocationScore(UserDTO user, Collection<FilterDTO> filters,
			LocationDTO location, int count) throws IRException {
		IRDao dao = IRDaoFactory.createInstance();
		return (dao.findRoutesByLocationScore(user, filters, location, count));
	}

	@Override
	public UserDTO signUp(String username, String password) throws IRException, DuplicateUserException {
		IRDao dao = IRDaoFactory.createInstance();
		UserDTO newUser = new UserDTO(username, password);
		return (dao.createUser(newUser));
	}

	@Override
	public Collection<UserDTO> findUsers(Collection<String> usernames) throws IRException {
		IRDao dao = IRDaoFactory.createInstance();
		return dao.findUsers(usernames);
	}

	@Override
	public UserDTO findUser(String username) throws IRException, InstanceNotFoundException {
		Collection<String> usernames = new ArrayList<String>();
		usernames.add(username);
		Collection<UserDTO> result = this.findUsers(usernames);
		if (result.isEmpty())
			throw new InstanceNotFoundException(UserDTO.class + " with username = " + username);
		return result.iterator().next();
	}

	@Override
	public UserDTO updateUser(UserDTO updatedUser) throws IRException, InstanceNotFoundException {
		IRDao dao = IRDaoFactory.createInstance();
		dao.updateUser(updatedUser);
		return findUser(updatedUser.getUsername());
	}

	@Override
	public Collection<RouteDTO> findAllRoutes(UserDTO user, int topCount) throws IRException {
		IRDao dao = IRDaoFactory.createInstance();
		return (dao.findAllRoutes(user, topCount));
	}

	@Override
	public Collection<RouteDTO> findRoutesById(UserDTO user, Collection<String> routeIds) throws IRException {
		IRDao dao = IRDaoFactory.createInstance();
		return dao.findRoutesById(user, routeIds);
	}

	@Override
	public RouteProfileDTO findRouteProfile(UserDTO user) throws IRException {
		Collection<RouteDTO> routesDone = new ArrayList<RouteDTO>();
		routesDone = findRoutesById(user, user.getRouteIds());
		RouteProfileDTO sumRouteDone = new RouteProfileDTO();
		for (RouteDTO r : routesDone) {
			sumRouteDone.plus(r.getRouteProfile());
		}
		sumRouteDone.avg();
		return sumRouteDone;
	}

	public static void main(String[] args) throws IOException, ParseException {
		// System.out.println("TEST RouteProfileServiceIR ");
		// System.out.println("499".compareTo("2.411") + " | " +
		// "499".compareTo("500"));
		// int count = 10;
		// Collection<FilterDTO> filters = new ArrayList<FilterDTO>();
		// FilterDTO fDistance = new FilterRangeDTO(FieldsEnum.PR_DISTANCE,
		// "22", "90");
		// filters.add(fDistance);
		// FilterDTO fElevationUp = new
		// FilterRangeDTO(FieldsEnum.PR_ELEVATION_GAIN_UP_HILL, "499", "2.411");
		// filters.add(fElevationUp);
		// FilterDTO fElevationMax = new
		// FilterRangeDTO(FieldsEnum.PR_ELEVATION_MAX, "885", "2.829");
		// filters.add(fElevationMax);
		// FilterDTO fElevationDown = new
		// FilterRangeDTO(FieldsEnum.PR_ELEVATION_GAIN_DOWN_HILL, "499",
		// "2.411");
		// filters.add(fElevationDown);
		// FilterDTO fElevationMin = new
		// FilterRangeDTO(FieldsEnum.PR_ELEVATION_MIN, "620", "702");
		// filters.add(fElevationMin);
		// FilterDTO fLoop = new FilterValueDTO(FieldsEnum.PR_LOOP, "true");
		// filters.add(fLoop);
		// RouteProfileService routeProfileService = new
		// RouteProfileServiceIR();
		//
		// Collection<String> routesId = new ArrayList<String>();
		// routesId.add("http://es.wikiloc.com/wikiloc/view.do?id=11599162");
		// UserDTO user = new UserDTO("userName", "pass", routesId);
		//
		// try {
		// Collection<RouteDTO> result = routeProfileService.findRoutes(user,
		// filters, count);
		// for (RouteDTO routeDTO : result) {
		// System.out.println(routeDTO.toString());
		// }
		// } catch (IRException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }

	}

}
