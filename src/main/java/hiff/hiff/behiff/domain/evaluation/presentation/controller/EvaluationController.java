package hiff.hiff.behiff.domain.evaluation.presentation.controller;

import hiff.hiff.behiff.domain.evaluation.application.EvaluationService;
import hiff.hiff.behiff.domain.evaluation.presentation.dto.res.EvaluationResponse;
import hiff.hiff.behiff.domain.user.domain.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/evaluation")
@RequiredArgsConstructor
public class EvaluationController {

    private final EvaluationService evaluationService;

    @GetMapping
    public ResponseEntity<EvaluationResponse> getEvaluation(@AuthenticationPrincipal User user) {
        EvaluationResponse response = evaluationService.getEvaluation(user.getId());
        return ResponseEntity.ok(response);
    }
}
