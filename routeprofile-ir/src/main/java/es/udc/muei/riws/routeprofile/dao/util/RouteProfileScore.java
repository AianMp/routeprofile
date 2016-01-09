package es.udc.muei.riws.routeprofile.dao.util;

import java.io.IOException;

import org.apache.lucene.index.AtomicReaderContext;
import org.apache.lucene.queries.CustomScoreQuery;
import org.apache.lucene.search.BooleanQuery;

import es.udc.muei.riws.routeprofile.model.dto.RouteProfileDTO;

public class RouteProfileScore extends CustomScoreQuery {

    private RouteProfileDTO routeProfile;

    public RouteProfileScore(BooleanQuery subQuery, RouteProfileDTO routeProfile) {
	super(subQuery);
	this.routeProfile = routeProfile;
    }

    @Override
    protected RouteProfileScoreProvider getCustomScoreProvider(AtomicReaderContext context) throws IOException {
	return new RouteProfileScoreProvider(context, routeProfile);
    }

}
