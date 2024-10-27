package hiff.hiff.behiff.domain.profile.presentation.dto.req;

import hiff.hiff.behiff.domain.profile.domain.enums.Company;
import hiff.hiff.behiff.global.validation.annotation.ValidCompany;
import lombok.Getter;

@Getter
public class UserCareerRequest {

    @ValidCompany
    private Company company;

    private String field;

    private String verification;
}
