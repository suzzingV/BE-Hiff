package hiff.hiff.behiff.domain.user.presentation.dto.res;

import hiff.hiff.behiff.domain.user.domain.entity.User;
import hiff.hiff.behiff.domain.user.domain.entity.UserPos;
import hiff.hiff.behiff.domain.user.domain.enums.Mbti;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class UserDetailResponse {

    private String nickname;

    private Integer age;

    private String posX;

    private String posY;

    private String income;

    private String addr;

    private String job;

    private List<String> photos;

    private List<String> hobbies;

    private List<String> lifeStyles;

    private Mbti mbti;

    private String education;

    private String school;

    // TODO: 매칭 점수

    public static UserDetailResponseBuilder getPosBuilder(UserPos userPos) {
        return UserDetailResponse.builder()
                .posX(userPos.getX())
                .posY(userPos.getY());
    }

    public static UserDetailResponse of(User user, List<String> photos, List<String> hobbies, List<String> lifeStyles, String posX, String posY) {

        return UserDetailResponse.builder()
                .nickname(user.getNickname())
                .age(user.getAge())
                .income(user.getIncome().getKey())
                .addr(user.getAddr())
                .job(user.getJob())
                .photos(photos)
                .lifeStyles(lifeStyles)
                .hobbies(hobbies)
                .mbti(user.getMbti())
                .education(user.getEducation().getKey())
                .school(user.getSchool())
                .posX(posX)
                .posY(posY)
                .build();
    }
}
