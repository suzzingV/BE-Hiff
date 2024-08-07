package hiff.hiff.behiff.domain.user.application;

import hiff.hiff.behiff.domain.user.domain.entity.User;
import hiff.hiff.behiff.domain.user.domain.entity.UserPos;
import hiff.hiff.behiff.domain.user.exception.UserException;
import hiff.hiff.behiff.domain.user.infrastructure.UserPosRepository;
import hiff.hiff.behiff.domain.user.infrastructure.UserRepository;
import hiff.hiff.behiff.domain.user.presentation.dto.req.*;
import hiff.hiff.behiff.global.response.properties.ErrorCode;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserProfileService {

    private final UserRepository userRepository;
    private final UserPosRepository userPosRepository;

    public void updateNickname(User user, NicknameRequest request) {
        checkNicknameDuplication(request.getNickname());
        user.changeNickname(request.getNickname());
    }

    public void updateBirth(User user, BirthRequest request) {
        user.changeBirth(request.getBirthYear(), request.getBirthMonth(), request.getBirthDay());
    }

    public void updateGender(User user, GenderRequest request) {
        user.changeGender(request.getGender());
    }

    public void updateMbti(User user, MbtiRequest request) {
        user.changeMbti(request.getMbti());
    }

    public void updateIncome(User user, IncomeRequest request) {
        user.changeIncome(request.getIncome());
    }

    public void updateAddress(User user, AddressRequest request) {
        user.changeAddress(request.getAddr1(), request.getAddr2(), request.getAddr3());
    }

    public void updateEducation(User user, EducationRequest request) {
        user.changeEducation(request.getEducation(), request.getSchool());
    }

    public void updatePhoneNum(User user, PhoneNumRequest request) {
        user.changePhoneNum(request.getPhoneNum());
    }

    public void updateHopeAge(User user, HopeAgeRequest request) {
        user.changeHopeAge(request.getMinAge(), request.getMaxAge());
    }

    public void updateDistance(User user, DistanceRequest request) {
        checkDistanceRange(request);
        user.changeMaxDistance(request.getMaxDistance());
        user.changeMinDistance(request.getMinDistance());
    }

    public Optional<UserPos> findUserPosByUserId(Long userId) {
        return userPosRepository.findByUserId(userId);
    }

    private void checkNicknameDuplication(String nickname) {
        userRepository.findByNickname(nickname)
                .ifPresent(user -> {
                    throw new UserException(ErrorCode.NICKNAME_ALREADY_EXISTS);
                });
    }

    private static void checkDistanceRange(DistanceRequest request) {
        if (request.getMinDistance() > request.getMaxDistance()) {
            throw new UserException(ErrorCode.DISTANCE_RANGE_REVERSE);
        }
    }

    // TODO: 첫인상 점수 볼 때마다 하트 지불?
    public Double getEvaluatedScore(User user) {
        if (user.getEvaluatedCount() < 10) {
            throw new UserException(ErrorCode.EVALUATION_COUNT_NOT_ENOUGH);
        }
        return user.getEvaluatedScore();
    }
}
