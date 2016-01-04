package es.udc.muei.riws.routeprofile.dao;

import es.udc.muei.riws.routeprofile.dao.impl.LuceneDao;

public class IRDaoFactory {

	public static IRDao createInstance() {
		return (new LuceneDao());
	}
}
