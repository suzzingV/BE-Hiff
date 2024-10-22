package hiff.hiff.behiff.domain.user.presentation.dto.req;

import hiff.hiff.behiff.domain.user.domain.enums.Company;
import hiff.hiff.behiff.global.validation.annotation.ValidCompany;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class UserCareerRequest {

    @ValidCompany
    private Company company;

    private String field;

    private String verification;
}
