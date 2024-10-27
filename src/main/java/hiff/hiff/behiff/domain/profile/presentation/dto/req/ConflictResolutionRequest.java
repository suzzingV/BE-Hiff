package hiff.hiff.behiff.domain.profile.presentation.dto.req;

import hiff.hiff.behiff.domain.profile.domain.enums.ConflictResolution;
import hiff.hiff.behiff.global.validation.annotation.ValidConflictResolution;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class ConflictResolutionRequest {

    @ValidConflictResolution
    @NotNull
    private ConflictResolution conflictResolution;
}
