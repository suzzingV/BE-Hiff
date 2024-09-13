package hiff.hiff.behiff.domain.report.application;

import hiff.hiff.behiff.domain.report.domain.entity.Report;
import hiff.hiff.behiff.domain.report.infrastructure.ReportRepository;
import hiff.hiff.behiff.domain.report.presentation.dto.req.ReportRequest;
import hiff.hiff.behiff.domain.report.presentation.dto.res.ReportResponse;
import hiff.hiff.behiff.domain.user.application.UserCRUDService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ReportService {

    private final ReportRepository reportRepository;
    private final UserCRUDService userCRUDService;

    public ReportResponse report(Long userId, ReportRequest request) {
        userCRUDService.findById(userId);

        Report report = Report.builder()
            .reportedId(request.getReportedId())
            .reporterId(userId)
            .content(request.getContent())
            .build();
        reportRepository.save(report);

        return ReportResponse.from(report.getReportedId());
    }
}
