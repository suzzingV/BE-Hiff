package hiff.hiff.behiff.domain.matching.presentation.controller;

import hiff.hiff.behiff.domain.matching.application.service.MatchingService;
import hiff.hiff.behiff.domain.matching.presentation.dto.res.MatchingDetailResponse;
import hiff.hiff.behiff.domain.matching.presentation.dto.res.MatchingSimpleResponse;
import hiff.hiff.behiff.domain.user.domain.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v0/matching")
@RequiredArgsConstructor
public class MatchingControllerV0 {

    private final MatchingService matchingService;

    @GetMapping("/hiff")
    public ResponseEntity<MatchingSimpleResponse> getSingleHiffMatching(@AuthenticationPrincipal User user) {
        MatchingSimpleResponse response = matchingService.getHiffMatching(user);

        return ResponseEntity.ok(response);
    }
}
