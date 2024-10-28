package hiff.hiff.behiff.domain.criteria.presentation.dto.res;

import hiff.hiff.behiff.domain.criteria.domain.enums.Criteria;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CriteriaResponse {
    private String name;
    private Integer point;

    public static CriteriaResponse from(Criteria criteria) {
        return CriteriaResponse.builder()
                .name(criteria.getName())
                .point(criteria.getPoint())
                .build();
    }
}
