package hiff.hiff.behiff.domain.user.application.service;

import hiff.hiff.behiff.domain.user.domain.entity.UserGrad;
import hiff.hiff.behiff.domain.user.domain.entity.UserUniversity;
import hiff.hiff.behiff.domain.user.exception.UserException;
import hiff.hiff.behiff.domain.user.infrastructure.UserGradRepository;
import hiff.hiff.behiff.domain.user.infrastructure.UserUniversityRepository;
import hiff.hiff.behiff.global.response.properties.ErrorCode;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class UserSchoolService {

    private final UserUniversityRepository userUniversityRepository;
    private final UserGradRepository userGradRepository;

    public void createUniversity(Long userId, String name, String verification) {
        UserUniversity userUniversity = UserUniversity.builder()
            .userId(userId)
            .name(name)
            .verification(verification)
            .build();
        userUniversityRepository.save(userUniversity);
    }

    public UserUniversity findByUniversityUserId(Long userId) {
        return userUniversityRepository.findByUserId(userId)
            .orElse(UserUniversity.builder().build());
    }

    public void createGrad(Long userId, String name, String verification) {
        UserGrad userGrad = UserGrad.builder()
            .userId(userId)
            .name(name)
            .verification(verification)
            .build();
        userGradRepository.save(userGrad);
    }

    public UserGrad findByGradUserId(Long userId) {
        return userGradRepository.findByUserId(userId)
            .orElse(UserGrad.builder().build());
    }
}
