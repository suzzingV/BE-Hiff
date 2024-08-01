package hiff.hiff.behiff.domain.user.presentation.dto.res;

import hiff.hiff.behiff.domain.user.domain.enums.Education;
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

    private List<String> beliefs;

    private Mbti mbti;

    private String education;

    private String school;

    // TODO: 매칭 점수
}
