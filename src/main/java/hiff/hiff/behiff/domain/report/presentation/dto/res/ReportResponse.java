package hiff.hiff.behiff.domain.report.presentation.dto.res;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ReportResponse {

    private Long reportedId;

    public static ReportResponse from(Long reportedId) {
        return ReportResponse.builder()
            .reportedId(reportedId)
            .build();
    }
}
