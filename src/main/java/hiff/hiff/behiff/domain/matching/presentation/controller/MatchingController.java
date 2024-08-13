package hiff.hiff.behiff.domain.matching.presentation.controller;

import hiff.hiff.behiff.domain.matching.application.MatchingService;
import hiff.hiff.behiff.domain.matching.presentation.dto.req.MatchingRequest;
import hiff.hiff.behiff.domain.matching.presentation.dto.res.MatchingSimpleResponse;
import hiff.hiff.behiff.domain.user.domain.entity.User;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/matching")
@RequiredArgsConstructor
public class MatchingController {

    private final MatchingService matchingService;

    // TODO: daily 매칭으로 이름 변경
    @GetMapping("/normal")
    public ResponseEntity<List<MatchingSimpleResponse>> getGeneralMatching(@AuthenticationPrincipal User user) {
        List<MatchingSimpleResponse> responses = matchingService.getGeneralMatching(user.getId());
        return ResponseEntity.ok(responses);
    }
}
