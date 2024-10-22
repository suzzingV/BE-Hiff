package hiff.hiff.behiff.domain.user.application.service;

import hiff.hiff.behiff.domain.user.domain.entity.UserCareer;
import hiff.hiff.behiff.domain.user.domain.entity.UserGrad;
import hiff.hiff.behiff.domain.user.domain.entity.UserIncome;
import hiff.hiff.behiff.domain.user.domain.entity.UserUniversity;
import hiff.hiff.behiff.domain.user.exception.UserException;
import hiff.hiff.behiff.domain.user.infrastructure.UserGradRepository;
import hiff.hiff.behiff.domain.user.infrastructure.UserIncomeRepository;
import hiff.hiff.behiff.domain.user.infrastructure.UserUniversityRepository;
import hiff.hiff.behiff.global.response.properties.ErrorCode;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class UserIncomeService {

    private final UserIncomeRepository userIncomeRepository;

    public void createIncome(Long userId, Integer income, String verification) {
        UserIncome userIncome = UserIncome.builder()
            .userId(userId)
            .income(income)
            .verification(verification)
            .build();
        userIncomeRepository.save(userIncome);
    }

    public UserIncome findByUserId(Long userId) {
        return userIncomeRepository.findByUserId(userId)
            .orElse(UserIncome.builder().build());
    }
}
