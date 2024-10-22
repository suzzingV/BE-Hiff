package hiff.hiff.behiff.domain.user.presentation.dto.req;

import hiff.hiff.behiff.domain.user.domain.enums.Company;
import hiff.hiff.behiff.global.validation.annotation.ValidCompany;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class UserCareerRequest {

    @NotNull
    @ValidCompany
    private Company company;

    @NotNull
    private String field;

    private String verification;
}
