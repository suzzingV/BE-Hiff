package hiff.hiff.behiff.domain.user.presentation.dto.req;

import hiff.hiff.behiff.domain.user.domain.enums.Income;
import lombok.Getter;

@Getter
public class IncomeRequest {

    private Income income;

    private boolean isOpen;
}
