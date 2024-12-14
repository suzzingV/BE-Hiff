package hiff.hiff.behiff.domain.criteria.presentation.controller;

import hiff.hiff.behiff.domain.criteria.application.service.CriteriaService;
import hiff.hiff.behiff.domain.criteria.presentation.dto.res.CriteriaResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v0.2/criteria")
@RequiredArgsConstructor
public class CriteriaControllerV02 {

    private final CriteriaService criteriaService;

//    @GetMapping
//    public ResponseEntity<List<CriteriaResponse>> getAllCriteria() {
//        List<CriteriaResponse> response = criteriaService.getAllCriteria();
//        return ResponseEntity.ok(response);
//    }
}
