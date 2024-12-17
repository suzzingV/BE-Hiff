package hiff.hiff.behiff.domain.plan.application.service;

import hiff.hiff.behiff.domain.plan.domain.entity.UserPlan;
import hiff.hiff.behiff.domain.plan.exception.PlanException;
import hiff.hiff.behiff.domain.plan.infrastructure.UserPlanRepository;
import hiff.hiff.behiff.domain.plan.presentation.dto.res.CouponResponse;
import hiff.hiff.behiff.domain.plan.presentation.dto.res.PlanUpdateResponse;
import hiff.hiff.behiff.global.common.redis.RedisService;
import hiff.hiff.behiff.global.response.properties.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;

import static hiff.hiff.behiff.global.common.redis.RedisService.NOT_EXIST;

@Service
@RequiredArgsConstructor
@Transactional
public class PlanService {

    private final UserPlanRepository userPlanRepository;
    private final RedisService redisService;

    private static final String MEMBERSHIP_PREFIX = "membership_";
    private static final int UNIT_AMOUNT = 1;
    private static final Duration MEMBERSHIP_DURATION = Duration.ofDays(7);

    public CouponResponse purchaseUnit(Long userId) {
        UserPlan userPlan = findByUserId(userId);
        userPlan.addCoupon(UNIT_AMOUNT);
        return CouponResponse.from(userPlan);
    }

    public UserPlan findByUserId(Long userId) {
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

    public PlanUpdateResponse purchaseMembership(Long userId) {
        if(!isMembership(userId)) {
            throw new PlanException(ErrorCode.MEMBERSHIP_ALREADY);
        }
        redisService.setValue(MEMBERSHIP_PREFIX + userId, null, MEMBERSHIP_DURATION);
        return PlanUpdateResponse.of(userId);
    }

    public boolean isMembership(Long userId) {
        return !redisService.getStrValue(MEMBERSHIP_PREFIX + userId).equals(NOT_EXIST);
    }
}
