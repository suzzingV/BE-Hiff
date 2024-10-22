package hiff.hiff.behiff.domain.user.application.service;

import hiff.hiff.behiff.domain.catalog.application.service.CatalogFieldService;
import hiff.hiff.behiff.domain.catalog.domain.entity.Field;
import hiff.hiff.behiff.domain.user.domain.entity.User;
import hiff.hiff.behiff.domain.user.domain.entity.UserCareer;
import hiff.hiff.behiff.domain.user.domain.enums.Company;
import hiff.hiff.behiff.domain.user.exception.UserException;
import hiff.hiff.behiff.domain.user.infrastructure.UserCareerRepository;
import hiff.hiff.behiff.global.response.properties.ErrorCode;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class UserCareerService {

    private final CatalogFieldService catalogFieldService;
    private final UserCareerRepository userCareerRepository;

    public void updateCareer(Long userId, Long careerId, Company company, String verification) {
        Field field = catalogFieldService.findById(careerId);
        UserCareer userCareer = findByUserId(userId);
        userCareer.changeField(field.getName());
        userCareer.changeCompany(company);
        userCareer.changeVerification(verification);
        field.addCount();
    }

    public void createCareer(Long userId) {
        UserCareer userCareer = UserCareer.builder()
            .userId(userId)
            .build();
        userCareerRepository.save(userCareer);
    }

    public UserCareer findByUserId(Long userId) {
        return userCareerRepository.findByUserId(userId)
            .orElseThrow(() -> new UserException(ErrorCode.USER_CAREER_NOT_FOUND));
    }
}
