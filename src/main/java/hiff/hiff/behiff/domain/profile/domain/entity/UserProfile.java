package hiff.hiff.behiff.domain.profile.domain.entity;

import hiff.hiff.behiff.domain.profile.domain.enums.*;
import hiff.hiff.behiff.global.util.DateCalculator;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Entity
@Getter
@Table(name = "user_profile_TB")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Slf4j
public class UserProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(length = 20)
    private String nickname;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Enumerated(EnumType.STRING)
    private Mbti mbti;

    private LocalDate birth;

    @Min(0)
    @Max(80)
    private Integer age;

    @Enumerated(EnumType.STRING)
    private LookScore lookScore;

    private String mainPhoto;

    private String location;

    @Builder
    private UserProfile(Long userId) {
        this.userId = userId;
    }

    public void changeNickname(String nickname) {
        this.nickname = nickname;
    }

    public void changeBirth(int birthYear, int birthMonth, int birthDay) {
        LocalDate birthDate = LocalDate.of(birthYear, birthMonth, birthDay);
        this.birth = birthDate;
        updateAge();
    }

    public void updateGender(Gender gender) {
        this.gender = gender;
    }

    public void updateMbti(Mbti mbti) {
        this.mbti = mbti;
    }

    public void updateLookScore(LookScore lookScore) {
        this.lookScore = lookScore;
    }

    public void updateMainPhoto(String photoUrl) {
        this.mainPhoto = photoUrl;
    }

    public void updateAge() {
        this.age = DateCalculator.calculateAge(this.birth);
    }

    public void updateLocation(String location) {
        this.location = location;
    }

    public String getGenderText() {
        if (this.gender == null) {
            return null;
        }
        return this.gender.getText();
    }
}
