package hiff.hiff.behiff.domain.user.domain.entity;

import hiff.hiff.behiff.domain.user.domain.enums.BodyType;
import hiff.hiff.behiff.domain.user.domain.enums.Buddy;
import hiff.hiff.behiff.domain.user.domain.enums.ConflictResolution;
import hiff.hiff.behiff.domain.user.domain.enums.ContactFrequency;
import hiff.hiff.behiff.domain.user.domain.enums.Drinking;
import hiff.hiff.behiff.domain.user.domain.enums.Gender;
import hiff.hiff.behiff.domain.user.domain.enums.Ideology;
import hiff.hiff.behiff.domain.user.domain.enums.Mbti;
import hiff.hiff.behiff.domain.plan.domain.enums.Plan;
import hiff.hiff.behiff.domain.user.domain.enums.Religion;
import hiff.hiff.behiff.domain.user.domain.enums.Role;
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
import jakarta.validation.constraints.Pattern;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "user_TB")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 20)
    private String nickname;

    @Column(length = 20)
    @Pattern(regexp = "^[0-9]*$")
    private String phoneNum;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Enumerated(EnumType.STRING)
    private Mbti mbti;

    private LocalDate birth;

    @Min(0)
    @Max(80)
    private Integer age;


    private Double evaluatedScore;

    private String mainPhoto;

    private Boolean isSmoking;

    @Enumerated(EnumType.STRING)
    private Drinking drinking;

    @Enumerated(EnumType.STRING)
    private Buddy buddy;

    @Enumerated(EnumType.STRING)
    private Religion religion;

    @Enumerated(EnumType.STRING)
    private Ideology ideology;

    @Enumerated(EnumType.STRING)
    private ContactFrequency contactFrequency;

    @Enumerated(EnumType.STRING)
    private ConflictResolution conflictResolution;

    @Min(0)
    @Max(300)
    private Integer height;

    @Enumerated(EnumType.STRING)
    private BodyType bodyType;

    @CreationTimestamp
    @Column(nullable = false)
    private LocalDateTime createdAt;

    private LocalDateTime deletedAt;

    @Builder
    private User(Role role,
        String phoneNum) {
        this.role = role;
        this.evaluatedScore = 0.0;
        this.phoneNum = phoneNum;
    }

    public void changeNickname(String nickname) {
        this.nickname = nickname;
    }

    public void changeBirth(int birthYear, int birthMonth, int birthDay) {
        LocalDate birthDate = LocalDate.of(birthYear, birthMonth, birthDay);
        this.birth = birthDate;
        updateAge();
    }

    public void changeGender(Gender gender) {
        this.gender = gender;
    }

    public void changeMbti(Mbti mbti) {
        this.mbti = mbti;
    }



    public void changePhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public void updateEvaluatedScoreTmp(Double score) {
        this.evaluatedScore = score;
    }

    public void updateMainPhoto(String photoUrl) {
        this.mainPhoto = photoUrl;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.getText()));
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return this.phoneNum;
    }

    public void updateAge() {
        this.age = DateCalculator.calculateAge(this.birth);
    }

    public void changeIsSmoking(Boolean isSmoking) {
        this.isSmoking = isSmoking;
    }

    public void changeDrinking(Drinking drinking) {
        this.drinking = drinking;
    }

    public void changeBuddy(Buddy buddy) {
        this.buddy = buddy;
    }

    public void changeReligion(Religion religion) {
        this.religion = religion;
    }

    public void changeIdeology(Ideology ideology) {
        this.ideology = ideology;
    }

    public void changeContactFrequency(ContactFrequency contactFrequency) {
        this.contactFrequency = contactFrequency;
    }

    public void changeConflictResolution(ConflictResolution conflictResolution) {
        this.conflictResolution = conflictResolution;
    }

    public void changeHeight(Integer height) {
        this.height = height;
    }

    public void changeBodyType(BodyType bodyType) {
        this.bodyType = bodyType;
    }

    public String getDrinkingText() {
        if (this.drinking == null) {
            return null;
        }
        return this.drinking.getText();
    }

    public String getBuddyText() {
        if (this.buddy == null) {
            return null;
        }
        return this.buddy.getText();
    }

    public String getReligionText() {
        if (this.religion == null) {
            return null;
        }
        return this.religion.getText();
    }

    public String getIdeologyText() {
        if (this.ideology == null) {
            return null;
        }
        return this.ideology.getText();
    }

    public String getContactFrequencyText() {
        if (this.contactFrequency == null) {
            return null;
        }
        return this.contactFrequency.getText();
    }

    public String getConflictResolutionText() {
        if (this.conflictResolution == null) {
            return null;
        }
        return this.conflictResolution.getText();
    }

    public String getBodyTypeText() {
        if (this.bodyType == null) {
            return null;
        }
        return this.bodyType.getText();
    }

    public String getGenderText() {
        if (this.gender == null) {
            return null;
        }
        return this.gender.getText();
    }
}
