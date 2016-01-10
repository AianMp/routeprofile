package es.udc.muei.riws.routeprofile.dao.util;

import java.io.IOException;

import org.apache.lucene.index.AtomicReaderContext;
import org.apache.lucene.queries.CustomScoreQuery;
import org.apache.lucene.search.Query;

import es.udc.muei.riws.routeprofile.model.dto.LocationDTO;

public class RouteLocationScore extends CustomScoreQuery {

	private LocationDTO location = null;

	public RouteLocationScore(Query subQuery, LocationDTO location) {
		super(subQuery);
		this.location = location;
	}

	@Override
	protected RouteLocationScoreProvider getCustomScoreProvider(AtomicReaderContext context) throws IOException {
		return new RouteLocationScoreProvider(context, location);
	}
}
