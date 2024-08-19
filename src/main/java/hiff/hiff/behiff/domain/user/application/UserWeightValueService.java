package hiff.hiff.behiff.domain.user.application;

import hiff.hiff.behiff.domain.user.domain.entity.User;
import hiff.hiff.behiff.domain.user.domain.entity.WeightValue;
import hiff.hiff.behiff.domain.user.domain.enums.Income;
import hiff.hiff.behiff.domain.user.exception.UserException;
import hiff.hiff.behiff.domain.user.infrastructure.WeightValueRepository;
import hiff.hiff.behiff.domain.user.presentation.dto.req.WeightValueRequest;
import hiff.hiff.behiff.global.response.properties.ErrorCode;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class UserWeightValueService {

    private final WeightValueRepository weightValueRepository;
    private final UserCRUDService userCRUDService;

    // TODO: 외모 가중치
    public void updateWeightValue(Long userId, WeightValueRequest request) {
        WeightValue weightValue = weightValueRepository.findByUserId(userId)
            .orElseThrow(() -> new UserException(ErrorCode.WEIGHT_VALUE_NOT_FOUND));
        checkIncomePrivate(userId, weightValue);
        weightValue.changeWeightValue(request.getIncome(), request.getAppearance(),
            request.getHobby(),
            request.getLifeStyle(), request.getMbti());
    }

    public WeightValue createWeightValue(Long userId) {
        WeightValue weightValue = WeightValue.builder()
            .userId(userId)
            .build();
        return weightValueRepository.save(weightValue);
    }

    public WeightValue findByUserId(Long userId) {
        return weightValueRepository.findByUserId(userId)
            .orElseThrow(() -> new UserException(ErrorCode.WEIGHT_VALUE_NOT_FOUND));
    }

    private void checkIncomePrivate(Long userId, WeightValue weightValue) {
        User user = userCRUDService.findById(userId);
        if (user.getIncome() == Income.PRIVATE && weightValue.getIncome() != 0) {
            throw new UserException(ErrorCode.INCOME_WEIGHT_VALUE_PRIVATE);
        }
    }
}
