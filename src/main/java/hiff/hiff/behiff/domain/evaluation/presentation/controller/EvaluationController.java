package hiff.hiff.behiff.domain.evaluation.presentation.controller;

import hiff.hiff.behiff.domain.evaluation.application.EvaluationService;
import hiff.hiff.behiff.domain.evaluation.presentation.dto.req.EvaluationRequest;
import hiff.hiff.behiff.domain.evaluation.presentation.dto.res.EvaluatedResponse;
import hiff.hiff.behiff.domain.evaluation.presentation.dto.res.EvaluationResponse;
import hiff.hiff.behiff.domain.user.domain.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/evaluation")
@RequiredArgsConstructor
public class EvaluationController {

    private final EvaluationService evaluationService;

    @GetMapping
    public ResponseEntity<EvaluatedResponse> getEvaluated(@AuthenticationPrincipal User user) {
        EvaluatedResponse response = evaluationService.getEvaluated(user.getId());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/free")
    public ResponseEntity<EvaluationResponse> freeEvaluate(@AuthenticationPrincipal User user, @RequestBody EvaluationRequest request) {
        EvaluationResponse response = evaluationService.freeEvaluate(user.getId(), request);
        return ResponseEntity.ok(response);
    }
}
