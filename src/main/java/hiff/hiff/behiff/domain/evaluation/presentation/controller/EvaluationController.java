package hiff.hiff.behiff.domain.evaluation.presentation.controller;

import hiff.hiff.behiff.domain.evaluation.application.EvaluationService;
import hiff.hiff.behiff.domain.evaluation.presentation.dto.req.EvaluationRequest;
import hiff.hiff.behiff.domain.evaluation.presentation.dto.res.EvaluatedResponse;
import hiff.hiff.behiff.domain.evaluation.presentation.dto.res.EvaluationResponse;
import hiff.hiff.behiff.domain.user.domain.entity.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/evaluation")
@RequiredArgsConstructor
public class EvaluationController {

    private final EvaluationService evaluationService;

    @GetMapping
    public ResponseEntity<EvaluatedResponse> getEvaluated(@AuthenticationPrincipal User user) {
        EvaluatedResponse response = evaluationService.getEvaluated(user);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<EvaluationResponse> evaluate(@AuthenticationPrincipal User user,
        @Valid @RequestBody EvaluationRequest request) {
        EvaluationResponse response = evaluationService.evaluate(user.getId(), request);
        return ResponseEntity.ok(response);
    }
}
