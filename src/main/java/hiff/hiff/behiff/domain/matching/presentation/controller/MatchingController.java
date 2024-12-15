package hiff.hiff.behiff.domain.matching.presentation.controller;

import hiff.hiff.behiff.domain.matching.application.service.MatchingServiceFacade;
import hiff.hiff.behiff.domain.matching.presentation.dto.res.MatchingDetailResponse;
import hiff.hiff.behiff.domain.matching.presentation.dto.res.MatchingSimpleResponse;
import hiff.hiff.behiff.domain.user.domain.entity.User;
import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Matching", description = "Matching 관련 API")
@RestController
@RequestMapping("/api/v1/matching")
@RequiredArgsConstructor
public class MatchingController {

    private final MatchingServiceFacade matchingServiceFacade;

    @Operation(
            summary = "매칭 목록 조회",
            description = "매칭 목록을 조회합니다. 토큰 o"
    )
    @ApiResponse(
            responseCode = "200",
            description = "매칭 목록 조회에 성공하였습니다."
    )
    @GetMapping("/random")
    public ResponseEntity<List<MatchingSimpleResponse>> getRandomMatching(
        @AuthenticationPrincipal User user) {
        List<MatchingSimpleResponse> responses = matchingServiceFacade.getRandomMatching(
            user.getId());
        return ResponseEntity.ok(responses);
    }

    @Operation(
            summary = "매칭 상세 조회",
            description = "매칭 상세를 조회합니다. 토큰 o"
    )
    @ApiResponse(
            responseCode = "200",
            description = "매칭 상세 조회에 성공하였습니다."
    )
    @GetMapping("/{matchedId}")
    public ResponseEntity<MatchingDetailResponse> getMatchingDetails(
        @AuthenticationPrincipal User user, @PathVariable Long matchedId) {
        MatchingDetailResponse response = matchingServiceFacade.getMatchingDetails(user.getId(),
            matchedId);
        return ResponseEntity.ok(response);
    }
}
