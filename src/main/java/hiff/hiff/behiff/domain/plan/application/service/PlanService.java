package hiff.hiff.behiff.domain.plan.application.service;

import hiff.hiff.behiff.domain.plan.domain.entity.UserPlan;
import hiff.hiff.behiff.domain.plan.domain.enums.Plan;
import hiff.hiff.behiff.domain.plan.exception.PlanException;
import hiff.hiff.behiff.domain.plan.infrastructure.UserPlanRepository;
import hiff.hiff.behiff.domain.user.domain.entity.User;
import hiff.hiff.behiff.domain.user.domain.strategy.PlanStrategy;
import hiff.hiff.behiff.domain.user.presentation.dto.req.PlanRequest;
import hiff.hiff.behiff.domain.user.presentation.dto.res.UserUpdateResponse;
import hiff.hiff.behiff.global.response.properties.ErrorCode;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class PlanService {

    private final UserPlanRepository userPlanRepository;

    private static final int MATCHING_LIMIT = 3;
    private static final int REJECTION_LIMIT = 2;

    public UserUpdateResponse updatePlan(Long userId, PlanRequest request) {
        UserPlan userPlan = findByUserId(userId);
        Plan plan = request.getPlan();
        PlanStrategy planStrategy = plan.getPlanStrategy();

        userPlan.changePlan(plan);
        userPlan.updatePoint(planStrategy.getPoints());
        userPlan.updateMatchingCnt(MATCHING_LIMIT);
        userPlan.updateRejectionCnt(REJECTION_LIMIT);

        return UserUpdateResponse.from(userId);
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
}
