package hiff.hiff.behiff.global.auth.presentation.dto.req;

import hiff.hiff.behiff.global.auth.domain.enums.OS;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LoginRequest {

    @NotEmpty
    private String code;

    @Size(min = 1, max = 20)
    @NotEmpty
    @Pattern(regexp = "^[0-9]*$")
    private String phoneNum;

    @NotNull
    private Double latitude;

    @NotNull
    private Double longitude;

    private OS os;
}
