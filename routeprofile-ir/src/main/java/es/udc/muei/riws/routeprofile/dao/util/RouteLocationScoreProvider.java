package es.udc.muei.riws.routeprofile.dao.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.index.AtomicReaderContext;
import org.apache.lucene.queries.CustomScoreProvider;
import org.apache.lucene.search.Explanation;

import es.udc.muei.riws.routeprofile.model.dto.LocationDTO;
import es.udc.muei.riws.routeprofile.util.FieldsEnum;

public class RouteLocationScoreProvider extends CustomScoreProvider {

	private LocationDTO location = null;
	private List<LocationDTO> locations = new ArrayList<LocationDTO>();

	public RouteLocationScoreProvider(AtomicReaderContext context, LocationDTO location) throws IOException {
		super(context);
		this.location = location;

		for (int i = 0; i < context.reader().numDocs(); i++) {
			if (context.reader().document(i).get(FieldsEnum.PR_DISTANCE.name()) != null)
				locations.add(new LocationDTO(Double.valueOf(context.reader().document(i)
						.get(FieldsEnum.PR_LATITUDE.name())), Double.valueOf(context.reader().document(i)
						.get(FieldsEnum.PR_LONGITUDE.name()))));
			else
				locations.add(new LocationDTO());
		}
	}

	@Override
	public Explanation customExplain(int doc, Explanation subQueryExpl, Explanation valSrcExpl) throws IOException {
		return (customExplain(doc, subQueryExpl));
	}

	@Override
	public float customScore(int doc, float subQueryScore, float[] valSrcScores) throws IOException {
		return (super.customScore(doc, subQueryScore, valSrcScores));
	}

	@Override
	public float customScore(int doc, float subQueryScore, float valSrcScore) throws IOException {
		return (computeScore(locations.get(doc), valSrcScore));
	}

	private Explanation customExplain(int doc, Explanation subQueryExpl) {
		Explanation expl = new Explanation(computeScore(locations.get(doc), subQueryExpl.getValue()),
				"custom score = sqrt(subQueryScore) * distanceFactor for:");
		Explanation distanceFactorExpl = new Explanation(computeDistanceFactor(locations.get(doc)), "distance factor");
		expl.addDetail(distanceFactorExpl);
		expl.addDetail(subQueryExpl);
		return expl;
	}

	private float computeScore(LocationDTO location, float ratio) {
		float score = (float) Math.sqrt(ratio) * computeDistanceFactor(location);
		System.out.println("Final score:" + score);
		return score;
	}

	private float computeDistanceFactor(LocationDTO location) {
		float distance = computeDistance(location);
		float distanceFactor = 1 / (1 + distance);
		System.out.println("Distance: " + distance + " DistanceFactor: " + distanceFactor);
		return distanceFactor;
	}

	private float computeDistance(LocationDTO location) {
		if (location == null)
			// Null locations will take the lowest score.
			return ((float) Float.MAX_VALUE);
		return (this.location.distance(location).floatValue());
	}
}
