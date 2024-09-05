package hiff.hiff.behiff.domain.matching.presentation.controller;

import hiff.hiff.behiff.domain.matching.application.service.MatchingService;
import hiff.hiff.behiff.domain.matching.presentation.dto.res.MatchingSimpleResponse;
import hiff.hiff.behiff.domain.user.domain.entity.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Matching", description = "Matching 관련 API")
@RestController
@RequestMapping("/api/v0/matching")
@RequiredArgsConstructor
public class MatchingControllerV0 {

    private final MatchingService matchingService;

    @Operation(
        summary = "hiff 단일 매칭",
        description = "hiff 매칭 1회를 수행합니다. 토큰 o"
    )
    @ApiResponse(
        responseCode = "200",
        description = "hiff 매칭에 성공하였습니다."
    )
    @GetMapping("/hiff")
    public ResponseEntity<MatchingSimpleResponse> getSingleHiffMatching(
        @AuthenticationPrincipal User user) {
        MatchingSimpleResponse response = matchingService.getHiffMatching(user);

        return ResponseEntity.ok(response);
    }
}
