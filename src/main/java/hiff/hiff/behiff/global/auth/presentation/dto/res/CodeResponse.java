package hiff.hiff.behiff.global.auth.presentation.dto.res;

import lombok.Builder;
import lombok.Getter;
import org.springframework.retry.annotation.Backoff;

@Getter
@Builder
public class CodeResponse {

    private String code;
}
