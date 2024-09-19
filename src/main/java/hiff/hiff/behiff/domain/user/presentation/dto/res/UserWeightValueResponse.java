package hiff.hiff.behiff.domain.user.presentation.dto.res;
import lombok.Builder;
import lombok.Getter;
@Getter
@Builder
public class UserWeightValueResponse {
    private Long userId;
    private Integer appearanceWeight;
    private Integer hobbyWeight;
    private Integer lifeStyleWeight;
    private Integer mbtiWeight;
    private Integer hopeMinAge;
    private Integer hopeMaxAge;
    private Integer minDistance;
    private Integer maxDistance;
    public static UserWeightValueResponse of(Long userId, Integer appearanceWeight, Integer hobbyWeight, Integer lifeStyleWeight, Integer mbtiWeight, Integer hopeMinAge, Integer hopeMaxAge, Integer minDistance, Integer maxDistance) {
        return UserWeightValueResponse.builder()
                .userId(userId)
                .appearanceWeight(appearanceWeight)
                .hobbyWeight(hobbyWeight)
                .lifeStyleWeight(lifeStyleWeight)
                .mbtiWeight(mbtiWeight)
                .hopeMinAge(hopeMinAge)
                .hopeMaxAge(hopeMaxAge)
                .maxDistance(maxDistance)
                .minDistance(minDistance)
                .build();
    }
}
