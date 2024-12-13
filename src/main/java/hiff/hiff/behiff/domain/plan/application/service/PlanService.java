package hiff.hiff.behiff.domain.plan.application.service;

import hiff.hiff.behiff.domain.plan.domain.entity.UserPlan;
import hiff.hiff.behiff.domain.plan.exception.PlanException;
import hiff.hiff.behiff.domain.plan.infrastructure.UserPlanRepository;
import hiff.hiff.behiff.domain.plan.presentation.dto.res.CouponResponse;
import hiff.hiff.behiff.global.response.properties.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class PlanService {

    private final UserPlanRepository userPlanRepository;
    private static final int UNIT_AMOUNT = 1;

    public CouponResponse purchaseUnit(Long userId) {
        UserPlan userPlan = findByUserId(userId);
        userPlan.addCoupon(UNIT_AMOUNT);
        return CouponResponse.from(userPlan);
    }

    private UserPlan findByUserId(Long userId) {
        return userPlanRepository.findByUserId(userId)
            .orElseThrow(() -> new PlanException(ErrorCode.USER_PLAN_NOT_FOUND));
    }

    public void createUserPlan(Long userId) {
        UserPlan userPlan = UserPlan.builder()
            .userId(userId)
            .build();
        userPlanRepository.save(userPlan);
    }

    public CouponResponse getUserPlan(Long userId) {
        UserPlan userPlan = findByUserId(userId);
        return CouponResponse.from(userPlan);
    }
}
