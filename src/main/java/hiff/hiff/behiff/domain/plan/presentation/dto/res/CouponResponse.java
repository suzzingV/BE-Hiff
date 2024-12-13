package hiff.hiff.behiff.domain.plan.presentation.dto.res;

import hiff.hiff.behiff.domain.plan.domain.entity.UserPlan;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CouponResponse {

    private Long userId;

    private Integer coupon;

    public static CouponResponse from(UserPlan plan) {
        return CouponResponse.builder()
                .userId(plan.getUserId())
                .coupon(plan.getCoupon())
                .build();
    }
}
