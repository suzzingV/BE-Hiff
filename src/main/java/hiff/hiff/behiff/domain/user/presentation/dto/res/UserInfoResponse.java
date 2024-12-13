package hiff.hiff.behiff.domain.user.presentation.dto.res;

import hiff.hiff.behiff.domain.profile.domain.entity.UserProfile;
import hiff.hiff.behiff.domain.profile.application.dto.UserIntroductionDto;

import java.util.List;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserInfoResponse {

    private Long userId;

    private UserProfile userProfile;

    private List<String> photos;

    private List<UserIntroductionDto> introductions;

    public static UserInfoResponse of(UserProfile userProfile,
                                      List<String> photos,
                                      List<UserIntroductionDto> introductions) {
        return UserInfoResponse.builder()
            .userId(userProfile.getId())
            .userProfile(userProfile)
            .photos(photos)
            .introductions(introductions)
            .build();
    }
}
