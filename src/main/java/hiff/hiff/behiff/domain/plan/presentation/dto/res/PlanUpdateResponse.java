package hiff.hiff.behiff.domain.plan.presentation.dto.res;

import hiff.hiff.behiff.domain.plan.domain.entity.UserPlan;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PlanUpdateResponse {

    private Long userId;

    public static PlanUpdateResponse of(Long userId) {
        return PlanUpdateResponse.builder()
                .userId(userId)
                .build();
    }
}
