package hiff.hiff.behiff.domain.matching.presentation.controller;

import hiff.hiff.behiff.domain.matching.application.MatchingService;
import hiff.hiff.behiff.domain.matching.presentation.dto.res.MatchingDetailResponse;
import hiff.hiff.behiff.domain.matching.presentation.dto.res.MatchingSimpleResponse;
import hiff.hiff.behiff.domain.user.domain.entity.User;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/matching")
@RequiredArgsConstructor
public class MatchingController {

    private final MatchingService matchingService;

    @GetMapping("/daily")
    public ResponseEntity<List<MatchingSimpleResponse>> getDailyMatching(
        @AuthenticationPrincipal User user) {
        List<MatchingSimpleResponse> responses = matchingService.getDailyMatching(user.getId());
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/daily/paid")
    public ResponseEntity<List<MatchingSimpleResponse>> getPaidDailyMatching(
        @AuthenticationPrincipal User user) {
        List<MatchingSimpleResponse> responses = matchingService.getPaidDailyMatching(user.getId());
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/daily/{matchedId}")
    public ResponseEntity<MatchingDetailResponse> getDailyMatchingDetails(
        @AuthenticationPrincipal User user, @PathVariable Long matchedId) {
        MatchingDetailResponse response = matchingService.getDailyMatchingDetails(user.getId(),
            matchedId);
        return ResponseEntity.ok(response);
    }
}
