package hiff.hiff.behiff.domain.bond.presentation.dto.res;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class LikeToUserResponse {

    private Long userId;

    private String nickname;

    private Integer age;

    private String mainPhoto;

    private String matchingDate;

    public static LikeToUserResponse of(Long userId, String nickname, Integer age, String mainPhoto, String matchingDate) {
        return LikeToUserResponse.builder()
                .age(age)
                .userId(userId)
                .nickname(nickname)
                .mainPhoto(mainPhoto)
                .matchingDate(matchingDate)
                .build();
    }
}
