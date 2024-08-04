package hiff.hiff.behiff.domain.user.domain.entity;


import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "life_style_TB")
public class LifeStyle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Integer count;

    @Builder
    private LifeStyle(String name) {
        this.name = name;
        this.count = 1;
    }

    public void addCount() {
        count++;
    }
}
