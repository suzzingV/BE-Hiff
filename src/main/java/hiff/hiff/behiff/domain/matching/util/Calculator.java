package hiff.hiff.behiff.domain.matching.util;

import hiff.hiff.behiff.domain.user.domain.entity.WeightValue;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class Calculator {

    public static Integer computeTotalScoreByMatcher(WeightValue matcherWV, int mbtiSimilarity, int hobbySimilarity, int lifeStyleSimilarity, int incomeSimilarity) {
        return (matcherWV.getMbti() * mbtiSimilarity + matcherWV.getHobby() * hobbySimilarity + matcherWV.getIncome() * incomeSimilarity + matcherWV.getLifeStyle() * lifeStyleSimilarity) / matcherWV.getTotal();
    }

    public static int computeIntAvg(int sum, int count) {
        return (int) Math.round((double)sum / count);
    }

    public static Double computeDistance(double x1, double y1, double x2, double y2) {
        double distance = Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));

        return Math.round(distance * 100.0) / 100.0;
    }
}
