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
    public static UserWeightValueResponse of(Long userId, Integer appearanceWeight, Integer hobbyWeight, Integer lifeStyleWeight, Integer mbtiWeight) {
        return UserWeightValueResponse.builder()
                .userId(userId)
                .appearanceWeight(appearanceWeight)
                .hobbyWeight(hobbyWeight)
                .lifeStyleWeight(lifeStyleWeight)
                .mbtiWeight(mbtiWeight)
                .build();
    }
}
