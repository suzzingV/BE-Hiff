package hiff.hiff.behiff.domain.user.application.service;

import hiff.hiff.behiff.domain.user.exception.UserException;
import hiff.hiff.behiff.domain.user.infrastructure.WeightValueRepository;
import hiff.hiff.behiff.domain.weighting.domain.entity.UserWeighting;
import hiff.hiff.behiff.global.response.properties.ErrorCode;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class UserWeightValueService {

    private final WeightValueRepository weightValueRepository;

    public void updateWeightValue(Long userId, Integer appearanceWV, Integer hobbyWV,
        Integer lifeStyleWV, Integer mbtiWV) {
        UserWeighting weighting = weightValueRepository.findByUserId(userId)
            .orElseThrow(() -> new UserException(ErrorCode.WEIGHT_VALUE_NOT_FOUND));
//        checkIncomePrivate(userId, weightValue);
        weighting.changeWeightValue(appearanceWV, hobbyWV, lifeStyleWV, mbtiWV);
    }

    public UserWeighting createWeightValue(Long userId) {
        UserWeighting weighting = UserWeighting.builder()
            .userId(userId)
            .build();
        return weightValueRepository.save(weighting);
    }

    public UserWeighting findByUserId(Long userId) {
        return weightValueRepository.findByUserId(userId)
            .orElse(UserWeighting.builder().build());
    }

//    private void checkIncomePrivate(Long userId, WeightValue weightValue) {
//        User user = userCRUDService.findById(userId);
//        if (user.getIncome() == Income.PRIVATE && weightValue.getIncome() != 0) {
//            throw new UserException(ErrorCode.INCOME_WEIGHT_VALUE_PRIVATE);
//        }
//    }
}
