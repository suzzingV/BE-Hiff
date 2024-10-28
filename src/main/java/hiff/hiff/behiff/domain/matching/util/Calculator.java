package hiff.hiff.behiff.domain.matching.util;

import hiff.hiff.behiff.domain.weighting.domain.entity.UserWeighting;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Calculator {

    private static final double EARTH_RADIUS = 6371.0;

    public static Integer computeTotalScoreByMatcher(UserWeighting matcherWV, int mbtiSimilarity,
                                                     int hobbySimilarity, int lifeStyleSimilarity, double matchedEvaluatedScore) {
        double evaluatedScore = convertEvaluatedScore(matchedEvaluatedScore);

        return (int) Math.round(
            (matcherWV.getMbti() * mbtiSimilarity + matcherWV.getHobby() * hobbySimilarity
                + matcherWV.getLifeStyle() * lifeStyleSimilarity
                + matcherWV.getAppearance() * evaluatedScore) / matcherWV.getTotal());
    }

    public static int computeIntAvg(int sum, int count) {
        return count == 0 ? 0 : (int) Math.round((double) sum / count);
    }

    public static Double computeDistance(double lat1, double lon1, double lat2, double lon2) {
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);

        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
            + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
            * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        double distance = EARTH_RADIUS * c;

        return Math.round(distance * 100.0) / 100.0;
    }

    private static double convertEvaluatedScore(double evaluatedScore) {
        return evaluatedScore / 5 * 100;
    }
}
