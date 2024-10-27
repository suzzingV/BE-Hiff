package hiff.hiff.behiff.domain.profile.presentation.dto.req;

import hiff.hiff.behiff.domain.profile.domain.enums.ContactFrequency;
import hiff.hiff.behiff.global.validation.annotation.ValidContactFrequency;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ContactFrequencyRequest {

    @ValidContactFrequency
    @NotNull
    private ContactFrequency contactFrequency;
}
