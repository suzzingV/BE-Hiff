package hiff.hiff.behiff.domain.user.application;

import hiff.hiff.behiff.domain.evaluation.infrastructure.EvaluatedUserRepository;
import hiff.hiff.behiff.domain.user.domain.entity.GenderCount;
import hiff.hiff.behiff.domain.user.domain.entity.User;
import hiff.hiff.behiff.domain.user.domain.entity.UserPos;
import hiff.hiff.behiff.domain.user.domain.enums.Gender;
import hiff.hiff.behiff.domain.user.exception.UserException;
import hiff.hiff.behiff.domain.user.infrastructure.GenderCountRepository;
import hiff.hiff.behiff.domain.user.infrastructure.UserPosRepository;
import hiff.hiff.behiff.domain.user.infrastructure.UserRepository;
import hiff.hiff.behiff.domain.user.presentation.dto.req.BirthRequest;
import hiff.hiff.behiff.domain.user.presentation.dto.req.DistanceRequest;
import hiff.hiff.behiff.domain.user.presentation.dto.req.EducationRequest;
import hiff.hiff.behiff.domain.user.presentation.dto.req.GenderRequest;
import hiff.hiff.behiff.domain.user.presentation.dto.req.HopeAgeRequest;
import hiff.hiff.behiff.domain.user.presentation.dto.req.MbtiRequest;
import hiff.hiff.behiff.domain.user.presentation.dto.req.NicknameRequest;
import hiff.hiff.behiff.domain.user.presentation.dto.req.SchoolRequest;
import hiff.hiff.behiff.global.response.properties.ErrorCode;
import jakarta.transaction.Transactional;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class UserProfileService {

    private final UserRepository userRepository;
    private final UserPosRepository userPosRepository;
    private final EvaluatedUserRepository evaluatedUserRepository;
    private final GenderCountRepository genderCountRepository;
    private final UserCRUDService userCRUDService;

//    public static final String INCOME_PREFIX = "income_";
    public static final String MBTI_PREFIX = "mbti_";

    public void updateNickname(User user, NicknameRequest request) {
        checkNicknameDuplication(request.getNickname());
        user.changeNickname(request.getNickname());
    }

    public void updateBirth(User user, BirthRequest request) {
        user.changeBirth(request.getBirthYear(), request.getBirthMonth(), request.getBirthDay());
    }

    public void updateGender(User user, GenderRequest request) {
        Gender gender = request.getGender();
        user.changeGender(gender);
        updateGenderCount(gender);
        changeEvaluatedUserGender(user, gender);
    }

    private void updateGenderCount(Gender gender) {
        GenderCount genderCount = genderCountRepository.findById(gender)
            .orElseThrow(() -> new UserException(ErrorCode.GENDER_COUNT_NOT_FOUND));
        genderCount.addCount();
    }

    public void updateMbti(User user, MbtiRequest request) {
        user.changeMbti(request.getMbti());
    }

//    public void updateIncome(User user, IncomeRequest request) {
//        user.changeIncome(request.getIncome());
//    }

    public void updateEducation(User user, EducationRequest request) {
        user.changeEducation(request.getEducation());
    }

    public void updateSchool(User user, SchoolRequest request) {
        user.changeSchool(request.getSchool());
    }

    public void updatePhoneNum(Long userId, String phoneNum) {
        User user = userCRUDService.findById(userId);
        user.changePhoneNum(phoneNum);
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

    public Double getEvaluatedScore(User user) {
        if (user.getEvaluatedCount() < 10) {
            throw new UserException(ErrorCode.EVALUATION_COUNT_NOT_ENOUGH);
        }
        return user.getEvaluatedScore();
    }

    private void changeEvaluatedUserGender(User user, Gender gender) {
        evaluatedUserRepository.findByUserId(user.getId())
            .forEach(evaluatedUser -> {
                evaluatedUser.changeGender(gender);
                evaluatedUserRepository.save(evaluatedUser);
            });
    }
}
