package hiff.hiff.behiff.domain.report.presentation.controller;

import hiff.hiff.behiff.domain.report.application.ReportService;
import hiff.hiff.behiff.domain.report.presentation.dto.req.ReportRequest;
import hiff.hiff.behiff.domain.report.presentation.dto.res.ReportResponse;
import hiff.hiff.behiff.domain.user.domain.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v0/report")
public class ReportController {

    private final ReportService reportService;

    @PostMapping
    public ResponseEntity<ReportResponse> report(@AuthenticationPrincipal User user, @RequestBody ReportRequest request) {
        ReportResponse response = reportService.report(user.getId(), request);

        return ResponseEntity.ok(response);
    }
}
