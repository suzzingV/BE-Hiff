package hiff.hiff.behiff.domain.user.domain.entity;

import hiff.hiff.behiff.domain.user.domain.enums.*;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "user_TB")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 10)
    private String name;

    @Column(nullable = false)
    @Email
    private String email;

    @Column(nullable = false, length = 20)
    private String phoneNum;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SocialType socialType;

    @Column(nullable = false)
    private String socialId;

    @Column(nullable = false)
    private Gender gender;

    @Column(nullable = false)
    private Mbti mbti;

    @Column(nullable = false)
    private int income;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private Education education;

    private String school;

    @Column(nullable = false)
    private LocalDate birth;

    @Column(nullable = false)
    private Gender hopeGender;

    @Column(nullable = false)
    private String job;

    @CreationTimestamp
    @Column(nullable = false)
    private LocalDateTime createdAt;

    private LocalDateTime deletedAt;

    @Builder
    private User(String phoneNum, String name, String email, Role role,
                 String socialId, SocialType socialType) {
        this.name = name;
        this.email = email;
        this.role = role;
        this.socialId = socialId;
        this.socialType = socialType;
        this.phoneNum = phoneNum;
    }

    public void delete() {
        this.deletedAt = LocalDateTime.now();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.getKey()));
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return this.email;
    }
}
