package hiff.hiff.behiff.domain.user.domain.compositeKey;

import jakarta.persistence.Embeddable;
import lombok.Getter;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Getter
public class MbtiScoreKey implements Serializable {

    private String mbti1;

    private String mbti2;

    public MbtiScoreKey() {}

    public MbtiScoreKey(String mbti1, String mbti2) {
        this.mbti1 = mbti1;
        this.mbti2 = mbti2;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MbtiScoreKey hobbySimId = (MbtiScoreKey) o;
        return Objects.equals(mbti1, hobbySimId.mbti1) &&
            Objects.equals(mbti2, hobbySimId.mbti2);
    }

    @Override
    public int hashCode() {
        return Objects.hash(mbti1, mbti2);
    }
}
