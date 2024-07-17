package hiff.hiff.behiff.global.auth.presentation.dto.req;

import hiff.hiff.behiff.domain.user.domain.enums.SocialType;
import hiff.hiff.behiff.global.validation.annotation.ValidSocialType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LoginRequest {

    @Email
    @NotEmpty
    private String email;

    @NotEmpty
    private String socialId;

    @ValidSocialType
    private SocialType socialType;
}
