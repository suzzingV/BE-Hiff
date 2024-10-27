package hiff.hiff.behiff.domain.user.presentation.dto.res;

import hiff.hiff.behiff.domain.profile.domain.entity.UserProfile;
import hiff.hiff.behiff.domain.profile.application.dto.UserIntroductionDto;
import hiff.hiff.behiff.domain.profile.domain.entity.UserCareer;
import hiff.hiff.behiff.domain.profile.domain.entity.UserGrad;
import hiff.hiff.behiff.domain.profile.domain.entity.UserIncome;
import hiff.hiff.behiff.domain.profile.domain.entity.UserUniversity;
import hiff.hiff.behiff.domain.weighting.domain.entity.Weighting;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserInfoResponse {

    private Long userId;

    private UserProfile userProfile;

    private List<String> photos;

    private List<String> hobbies;

    private List<String> lifeStyles;

    private List<String> fashions;

    private List<UserIntroductionDto> introductions;

    private UserCareer career;

    private UserUniversity university;

    private UserGrad grad;

    private UserIncome income;

    public static UserInfoResponse of(UserProfile userProfile, List<String> hobbies,
        List<String> photos,
        List<String> lifeStyles, Weighting weighting, List<String> fashions,
        List<UserIntroductionDto> introductions, UserCareer userCareer, UserUniversity userUniversity, UserGrad userGrad, UserIncome userIncome) {
        return UserInfoResponse.builder()
            .userId(userProfile.getId())
            .userProfile(userProfile)
            .photos(photos)
            .hobbies(hobbies)
            .lifeStyles(lifeStyles)
            .fashions(fashions)
            .introductions(introductions)
            .career(userCareer)
            .university(userUniversity)
            .grad(userGrad)
            .income(userIncome)
            .build();
    }
}
