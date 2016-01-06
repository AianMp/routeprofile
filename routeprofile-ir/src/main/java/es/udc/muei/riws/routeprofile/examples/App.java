package es.udc.muei.riws.routeprofile.examples;

import java.util.ArrayList;
import java.util.Collection;

import es.udc.muei.riws.routeprofile.dao.IRDao;
import es.udc.muei.riws.routeprofile.dao.impl.LuceneDao;
import es.udc.muei.riws.routeprofile.model.dto.RouteDTO;
import es.udc.muei.riws.routeprofile.model.exception.IRException;
import es.udc.muei.riws.routeprofile.service.RouteProfileService;
import es.udc.muei.riws.routeprofile.service.impl.RouteProfileServiceIR;

public class App {

	public static void main(String[] args) {

		RouteProfileService rps = new RouteProfileServiceIR();
		try {
			IRDao dao = new LuceneDao();
			System.out.println("buuuu");
			// rps.signUp("jcascas1", "qwerty");
			// Collection<String> b = new ArrayList<String>();
			// b.add("jcascas1");
			// Collection<UserDTO> users = rps.findUsers(b);
			// System.out.println("Created user: " + users);
			//
			// UserDTO updatedUser = null;
			// for (UserDTO user : users) {
			// if (user.getUsername().equals("jcascas1"))
			// updatedUser = user;
			// }
			Collection<String> routeIds = new ArrayList<String>();
			routeIds.add("4376248");
			routeIds.add("7294921");
			routeIds.add("234");
			// updatedUser.setRouteIds(routeIds);
			// rps.updateUser(updatedUser);
			// users = rps.findUsers(b);
			// System.out.println("Updated user: " + users);

			Collection<RouteDTO> userRoutes = rps.findRoutesById(routeIds);
			System.out.println("Routes done by user: " + userRoutes);

		} catch (IRException e) {
			e.printStackTrace();
		}
	}

}
