package hiff.hiff.behiff.domain.profile.domain.entity;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "user_introduction_TB",
    indexes = {
        @Index(name = "idx_it_user_id", columnList = "userId")
    })
public class UserIntroduction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private Long questionId;

    @Column(nullable = false)
    private String question;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    private String content;

    @Builder
    private UserIntroduction(Long userId, Long questionId, String question, String content) {
        this.userId = userId;
        this.questionId = questionId;
        this.question = question;
        this.content = content;
    }

    public void updateContent(String content) {
        this.content = content;
    }
    public void updateQuestion(String question) {
        this.question = question;
    }
}
