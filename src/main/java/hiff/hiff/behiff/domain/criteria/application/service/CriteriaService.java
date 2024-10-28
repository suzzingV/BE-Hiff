package hiff.hiff.behiff.domain.criteria.application.service;

import hiff.hiff.behiff.domain.criteria.domain.enums.Filter;
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
        return Filter.ALL_VALUES.stream()
                .map(filter -> CriteriaResponse.builder()
                            .point(filter.getPoint())
                            .name(filter.getName())
                            .build())
                .toList();
    }
}
