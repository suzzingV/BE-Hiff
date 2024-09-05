package hiff.hiff.behiff.domain.matching.presentation.controller;

import hiff.hiff.behiff.domain.matching.application.service.MatchingServiceFacade;
import hiff.hiff.behiff.domain.matching.presentation.dto.res.DailyMatchingDetailResponse;
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

    private final MatchingServiceFacade matchingServiceFacade;

    @GetMapping("/daily")
    public ResponseEntity<List<MatchingSimpleResponse>> getDailyMatching(
        @AuthenticationPrincipal User user) {
        List<MatchingSimpleResponse> responses = matchingServiceFacade.getDailyMatching(user.getId());
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/daily/paid")
    public ResponseEntity<List<MatchingSimpleResponse>> getPaidDailyMatching(
        @AuthenticationPrincipal User user) {
        List<MatchingSimpleResponse> responses = matchingServiceFacade.getPaidDailyMatching(user.getId());
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/daily/{matchedId}")
    public ResponseEntity<DailyMatchingDetailResponse> getDailyMatchingDetails(
        @AuthenticationPrincipal User user, @PathVariable Long matchedId) {
        DailyMatchingDetailResponse response = matchingServiceFacade.getDailyMatchingDetails(user.getId(),
            matchedId);
        return ResponseEntity.ok(response);
    }
}
