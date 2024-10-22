package hiff.hiff.behiff.domain.catalog.domain.compositeKey;

import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;
import lombok.Getter;

@Embeddable
@Getter
public class LifeStyleSimId implements Serializable {

    private Long fromLifestyleId;

    private Long toLifestyleId;

    public LifeStyleSimId() {
    }

    public LifeStyleSimId(Long fromLifestyleId, Long toLifestyleId) {
        this.fromLifestyleId = fromLifestyleId;
        this.toLifestyleId = toLifestyleId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        LifeStyleSimId lifeStyleSimId = (LifeStyleSimId) o;
        return Objects.equals(fromLifestyleId, lifeStyleSimId.fromLifestyleId) &&
            Objects.equals(toLifestyleId, lifeStyleSimId.toLifestyleId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fromLifestyleId, toLifestyleId);
    }
}
