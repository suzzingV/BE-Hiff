package hiff.hiff.behiff.domain.report.infrastructure;

import hiff.hiff.behiff.domain.report.domain.entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportRepository extends JpaRepository<Report, Long> {

}
