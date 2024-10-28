package hiff.hiff.behiff.domain.criteria.presentation.dto.res;

import hiff.hiff.behiff.domain.criteria.domain.enums.Filter;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CriteriaResponse {
    private String name;
    private Integer point;

    public static CriteriaResponse from(Filter filter) {
        return CriteriaResponse.builder()
                .name(filter.getName())
                .point(filter.getPoint())
                .build();
    }
}
