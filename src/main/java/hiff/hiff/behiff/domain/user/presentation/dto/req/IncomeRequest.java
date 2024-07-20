package hiff.hiff.behiff.domain.user.presentation.dto.req;

import jakarta.validation.constraints.Min;
import lombok.Getter;

@Getter
public class IncomeRequest {

    @Min(0)
    private Integer income;

    private boolean isOpen;
}
