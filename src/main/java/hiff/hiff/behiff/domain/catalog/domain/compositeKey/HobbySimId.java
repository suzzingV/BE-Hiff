package hiff.hiff.behiff.domain.catalog.domain.compositeKey;

import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;
import lombok.Getter;

@Embeddable
@Getter
public class HobbySimId implements Serializable {

    private Long fromHobbyId;

    private Long toHobbyId;

    public HobbySimId() {
    }

    public HobbySimId(Long fromHobbyId, Long toHobbyId) {
        this.fromHobbyId = fromHobbyId;
        this.toHobbyId = toHobbyId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        HobbySimId hobbySimId = (HobbySimId) o;
        return Objects.equals(fromHobbyId, hobbySimId.fromHobbyId) &&
            Objects.equals(toHobbyId, hobbySimId.toHobbyId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fromHobbyId, toHobbyId);
    }
}
