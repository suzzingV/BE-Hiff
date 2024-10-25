package hiff.hiff.behiff.domain.plan.presentation.controller;

import hiff.hiff.behiff.domain.catalog.application.service.CatalogServiceFacade;
import hiff.hiff.behiff.domain.catalog.presentation.dto.res.QuestionResponse;
import hiff.hiff.behiff.domain.catalog.presentation.dto.res.TagResponse;
import hiff.hiff.behiff.domain.plan.application.service.PlanService;
import hiff.hiff.behiff.domain.user.domain.entity.User;
import hiff.hiff.behiff.domain.user.presentation.dto.req.PlanRequest;
import hiff.hiff.behiff.domain.user.presentation.dto.res.UserUpdateResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Plan", description = "Plan 관련 API")
@RestController
@RequestMapping("/api/v0.2/plan")
@RequiredArgsConstructor
public class PlanControllerV02 {

    private final PlanService planService;

    @Operation(
        summary = "user plan 변경",
        description = "user 플랜을 변경합니다. 토큰 o"
    )
    @ApiResponse(
        responseCode = "200",
        description = "user plan 변경에 성공하였습니다."
    )
    @PatchMapping("/me")
    public ResponseEntity<UserUpdateResponse> updatePlan(@AuthenticationPrincipal User user, @Valid @RequestBody PlanRequest request) {
        UserUpdateResponse response = planService.updatePlan(user.getId(), request);
        return ResponseEntity.ok(response);
    }
}
