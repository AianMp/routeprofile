package es.udc.muei.riws.routeprofile.service;

import es.udc.muei.riws.routeprofile.service.impl.RouteProfileServiceIR;

public class RouteProfileServiceFactory {

    public static RouteProfileService createInstance() {
	return (new RouteProfileServiceIR());
    }
}
