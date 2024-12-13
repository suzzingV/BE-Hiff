package hiff.hiff.behiff.domain.plan.presentation.controller;

import hiff.hiff.behiff.domain.plan.application.service.PlanService;
import hiff.hiff.behiff.domain.plan.presentation.dto.res.CouponResponse;
import hiff.hiff.behiff.domain.user.domain.entity.User;
import hiff.hiff.behiff.domain.profile.presentation.dto.res.ProfileUpdateResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Plan", description = "Plan 관련 API")
@RestController
@RequestMapping("/api/v0.2/plan")
@RequiredArgsConstructor
public class PlanControllerV02 {

    private final PlanService planService;

    @Operation(
            summary = "단품 매칭권 구매",
            description = "단품 매칭권을 구매합니다. 토큰 o"
    )
    @ApiResponse(
            responseCode = "200",
            description = "단품 매칭권 구매에 성공하였습니다."
    )
    @PatchMapping("/purchase/unit")
    public ResponseEntity<CouponResponse> purchaseUnit(@AuthenticationPrincipal User user) {
        CouponResponse response = planService.purchaseUnit(user.getId());
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "user plan 조회",
            description = "user 플랜을 조회합니다. 토큰 o"
    )
    @ApiResponse(
            responseCode = "200",
            description = "user plan 조회에 성공하였습니다."
    )
    @GetMapping("/me")
    public ResponseEntity<CouponResponse> getUserPlan(@AuthenticationPrincipal User user) {
        CouponResponse response = planService.getUserPlan(user.getId());
        return ResponseEntity.ok(response);
    }
}
