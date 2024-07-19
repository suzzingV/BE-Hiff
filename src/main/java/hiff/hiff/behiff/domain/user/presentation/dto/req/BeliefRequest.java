package hiff.hiff.behiff.domain.user.presentation.dto.req;

import java.util.List;
import lombok.Getter;

@Getter
public class BeliefRequest {

    private List<Long> originBeliefs;

    private List<String> newBeliefs;
}