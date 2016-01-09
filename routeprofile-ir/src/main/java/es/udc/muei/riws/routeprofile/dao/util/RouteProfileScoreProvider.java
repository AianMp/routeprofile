package es.udc.muei.riws.routeprofile.dao.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.index.AtomicReaderContext;
import org.apache.lucene.queries.CustomScoreProvider;
import org.apache.lucene.search.Explanation;

import es.udc.muei.riws.routeprofile.model.dto.RouteProfileDTO;
import es.udc.muei.riws.routeprofile.util.FieldsEnum;

public class RouteProfileScoreProvider extends CustomScoreProvider {
    private RouteProfileDTO routeProfile;
    private List<RouteProfileDTO> routesProfile = new ArrayList<RouteProfileDTO>();

    public RouteProfileScoreProvider(AtomicReaderContext context, RouteProfileDTO routeProfile) throws IOException {
	super(context);
	this.routeProfile = routeProfile;

	for (int i = 0; i < context.reader().numDocs(); i++) {
	    if (context.reader().document(i).get(FieldsEnum.PR_DISTANCE.name()) != null)
		routesProfile.add(new RouteProfileDTO(
			Double.valueOf(context.reader().document(i).get(FieldsEnum.PR_DISTANCE.name())),
			Double.valueOf(context.reader().document(i).get(FieldsEnum.PR_ELEVATION_MAX.name())),
			Double.valueOf(context.reader().document(i).get(FieldsEnum.PR_ELEVATION_MIN.name())),
			Double.valueOf(context.reader().document(i).get(FieldsEnum.PR_ELEVATION_GAIN_UP_HILL.name())),
			Double.valueOf(
				context.reader().document(i).get(FieldsEnum.PR_ELEVATION_GAIN_DOWN_HILL.name()))));
	    else
		routesProfile.add(new RouteProfileDTO());
	}
    }

    @Override
    public Explanation customExplain(int doc, Explanation subQueryExpl, Explanation[] valSrcExpls) throws IOException {
	return customExplain(doc, subQueryExpl);
    }

    @Override
    public Explanation customExplain(int doc, Explanation subQueryExpl, Explanation valSrcExpl) throws IOException {
	return customExplain(doc, subQueryExpl);
    }

    private Explanation customExplain(int doc, Explanation subQueryExpl) {
	Explanation expl = new Explanation(computeScore(routesProfile.get(doc).ratio(), subQueryExpl.getValue()),
		"custom score = sqrt(subQueryScore) * distanceFactor for:");
	Explanation distanceFactorExpl = new Explanation(computeDistanceFactor(routesProfile.get(doc).ratio()),
		"distance factor");
	expl.addDetail(distanceFactorExpl);
	expl.addDetail(subQueryExpl);
	return expl;
    }

    private float computeDistanceFactor(float ratio) {
	float distance = computeDistance(ratio);
	float distanceFactor = 1 / (1 + distance);
	System.out.println("Distance: " + distance + " DistanceFactor: " + distanceFactor);
	return distanceFactor;
    }

    private float computeDistance(float ratio) {
	return Math.abs(routeProfile.ratio() - ratio);
    }

    private float computeScore(float subScore, float ratio) {
	float score = (float) Math.sqrt(ratio) * computeDistanceFactor(subScore);
	System.out.println("Final score:" + score);
	return score;
    }

    @Override
    public float customScore(int doc, float subQueryScore, float[] valSrcScores) throws IOException {
	return super.customScore(doc, subQueryScore, valSrcScores);
    }

    @Override
    public float customScore(int doc, float subQueryScore, float valSrcScore) throws IOException {
	return computeScore(routesProfile.get(doc).ratio(), subQueryScore);
    }

}
