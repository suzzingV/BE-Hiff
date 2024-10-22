package hiff.hiff.behiff.domain.catalog.application.service;

import hiff.hiff.behiff.domain.catalog.exception.CatalogException;
import hiff.hiff.behiff.domain.catalog.domain.entity.Career;
import hiff.hiff.behiff.domain.catalog.infrastructure.CareerRepository;
import hiff.hiff.behiff.global.response.properties.ErrorCode;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class CatalogCareerService {

    private final CareerRepository careerRepository;

    public Career findById(Long careerId) {
        return careerRepository.findById(careerId)
            .orElseThrow(() -> new CatalogException(ErrorCode.CAREER_NOT_FOUND));
    }

    public List<Career> getAllCareers() {
        return careerRepository.findAll();
    }
}
