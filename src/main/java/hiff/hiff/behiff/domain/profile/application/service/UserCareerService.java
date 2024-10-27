package hiff.hiff.behiff.domain.profile.application.service;

import hiff.hiff.behiff.domain.catalog.application.service.CatalogFieldService;
import hiff.hiff.behiff.domain.catalog.domain.entity.Field;
import hiff.hiff.behiff.domain.profile.domain.entity.UserCareer;
import hiff.hiff.behiff.domain.profile.domain.enums.Company;
import hiff.hiff.behiff.domain.profile.infrastructure.UserCareerRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class UserCareerService {

    private final CatalogFieldService catalogFieldService;
    private final UserCareerRepository userCareerRepository;

    public void updateCareer(Long userId, String fieldName, Company company, String verification) {
        Field field = catalogFieldService.findByName(fieldName);
        UserCareer userCareer = findByUserId(userId);
        userCareer.changeField(fieldName);
        userCareer.changeCompany(company);
        userCareer.changeVerification(verification);
        field.addCount();
    }

    public void createCareer(Long userId, Company company, String field, String verification) {
        UserCareer userCareer = UserCareer.builder()
            .userId(userId)
            .company(company)
            .field(field)
            .verification(verification)
            .build();
        userCareerRepository.save(userCareer);
    }

    public UserCareer findByUserId(Long userId) {
        return userCareerRepository.findByUserId(userId)
            .orElse(UserCareer.builder().build());
    }

    public void deleteByUserId(Long userId) {
        userCareerRepository.deleteByUserId(userId);
    }
}
