package hiff.hiff.behiff.domain.criteria.application.service;

import hiff.hiff.behiff.domain.criteria.domain.enums.Criteria;
import hiff.hiff.behiff.domain.criteria.presentation.dto.res.CriteriaResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class CriteriaService {

    public List<CriteriaResponse> getAllCriteria() {
        return Criteria.ALL_VALUES.stream()
                .map(criteria -> CriteriaResponse.builder()
                            .point(criteria.getPoint())
                            .name(criteria.getName())
                            .build())
                .toList();
    }
}
