package hiff.hiff.behiff.domain.plan.presentation.dto.res;

import hiff.hiff.behiff.domain.plan.domain.entity.UserPlan;
import hiff.hiff.behiff.domain.plan.domain.enums.Plan;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserPlanResponse {

    private Long userId;

    private Integer point;

    private Plan plan;

    public static UserPlanResponse from(UserPlan plan) {
        return UserPlanResponse.builder()
                .userId(plan.getUserId())
                .plan(plan.getPlan())
                .point(plan.getPoint())
                .build();
    }
}
