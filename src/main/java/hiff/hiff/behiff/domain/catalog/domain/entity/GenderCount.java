package hiff.hiff.behiff.domain.catalog.domain.entity;

import hiff.hiff.behiff.domain.profile.domain.enums.Gender;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "gender_count_TB")
public class GenderCount {

    @Id
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(nullable = false)
    private Integer count;

    @Builder
    private GenderCount(String name) {
        this.count = 0;
    }

    public void addCount() {
        count++;
    }

    public void subtractCount() {
        count--;
    }
}
