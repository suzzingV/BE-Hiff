package hiff.hiff.behiff.domain.matching.application.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class NameWithCommonDto {

    private String name;

    private Boolean isCommon;
}
