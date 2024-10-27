package hiff.hiff.behiff.domain.plan.presentation.dto.req;

import hiff.hiff.behiff.domain.plan.domain.enums.Plan;
import hiff.hiff.behiff.global.validation.annotation.ValidPlan;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class PlanRequest {

    @ValidPlan
    @NotNull
    private Plan plan;
}
