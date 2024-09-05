package hiff.hiff.behiff.domain.matching.presentation.controller;

import hiff.hiff.behiff.domain.matching.application.service.MatchingService;
import hiff.hiff.behiff.domain.matching.application.service.MatchingServiceFacade;
import hiff.hiff.behiff.domain.matching.presentation.dto.res.HiffMatchingDetailResponse;
import hiff.hiff.behiff.domain.matching.presentation.dto.res.MatchingSimpleResponse;
import hiff.hiff.behiff.domain.user.domain.entity.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Matching", description = "Matching 관련 API")
@RestController
@RequestMapping("/api/v0/matching")
@RequiredArgsConstructor
public class MatchingControllerV0 {

    private final MatchingServiceFacade matchingServiceFacade;

    @Operation(
        summary = "hiff 단일 매칭",
        description = "hiff 매칭 1회를 수행합니다. 토큰 o"
    )
    @ApiResponse(
        responseCode = "200",
        description = "hiff 매칭에 성공하였습니다."
    )
    @PostMapping("/hiff")
    public ResponseEntity<MatchingSimpleResponse> performHiffMatching(
        @AuthenticationPrincipal User user) {
        MatchingSimpleResponse response = matchingServiceFacade.performHiffMatching(
            user.getId());

        return ResponseEntity.ok(response);
    }

    @Operation(
        summary = "hiff 매칭 목록 조회",
        description = "hiff 매칭 목록을 조회합니다. 토큰 o"
    )
    @ApiResponse(
        responseCode = "200",
        description = "hiff 매칭 목록 조회에 성공하였습니다."
    )
    @GetMapping("/hiff")
    public ResponseEntity<List<MatchingSimpleResponse>> getHiffMatching(
        @AuthenticationPrincipal User user) {
        List<MatchingSimpleResponse> responses = matchingServiceFacade.getHiffMatching(
            user.getId());

        return ResponseEntity.ok(responses);
    }

    @Operation(
        summary = "hiff 매칭 상세 조회",
        description = "hiff 매칭 상세 정보를 조회합니다. 토큰 o"
    )
    @ApiResponse(
        responseCode = "200",
        description = "hiff 매칭 상세 조회에 성공하였습니다."
    )
    @GetMapping("/hiff/{matchedId}")
    public ResponseEntity<HiffMatchingDetailResponse> getHiffMatchingDetails(
        @AuthenticationPrincipal User user, @PathVariable Long matchedId) {
        HiffMatchingDetailResponse response = matchingServiceFacade.getHiffMatchingDetails(user.getId(),
            matchedId);
        return ResponseEntity.ok(response);
    }
}
