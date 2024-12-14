package hiff.hiff.behiff.domain.matching.application.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MatchingDataDto {

    private Long matcherId;

    private Long matchedId;
}
