package hiff.hiff.behiff.domain.user.application;

import hiff.hiff.behiff.domain.user.domain.entity.Belief;
import hiff.hiff.behiff.domain.user.domain.entity.Hobby;
import hiff.hiff.behiff.domain.user.domain.entity.User;
import hiff.hiff.behiff.domain.user.domain.entity.UserBelief;
import hiff.hiff.behiff.domain.user.domain.entity.UserHobby;
import hiff.hiff.behiff.domain.user.domain.enums.Role;
import hiff.hiff.behiff.domain.user.domain.enums.SocialType;
import hiff.hiff.behiff.domain.user.exception.UserException;
import hiff.hiff.behiff.domain.user.infrastructure.*;
import hiff.hiff.behiff.domain.user.presentation.dto.req.*;
import hiff.hiff.behiff.domain.user.presentation.dto.res.UserRegisterResponse;
import hiff.hiff.behiff.global.auth.jwt.service.JwtService;
import hiff.hiff.behiff.global.response.properties.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final UserPhotoRepository userPhotoRepository;
    private final JobRepository jobRepository;
    private final HobbyRepository hobbyRepository;
    private final UserHobbyRepository userHobbyRepository;
    private final UserBeliefRepository userBeliefRepository;
    private final BeliefRepository beliefRepository;
    private final JwtService jwtService;
//    private final S3Service s3Service;

    public User registerUser(String email, String socialId, SocialType socialType,
                             Role role) {
        User user = User.builder()
                .role(role)
                .socialType(socialType)
                .socialId(socialId)
                .email(email)
                .build();
        return userRepository.save(user);
    }

    public void withdraw(Long userId, Optional<String> accessToken, Optional<String> refreshToken) {
        User user = findUserById(userId);
        user.delete();

        String access = accessToken
                .orElseThrow(() -> new UserException(ErrorCode.SECURITY_INVALID_ACCESS_TOKEN));
        String refresh = refreshToken
                .orElseThrow(() -> new UserException(ErrorCode.REFRESH_TOKEN_REQUIRED));

        jwtService.isTokenValid(refresh);
        jwtService.isTokenValid(access);
        jwtService.deleteRefreshToken(refresh);
        jwtService.invalidAccessToken(access);
    }

//    public UserRegisterResponse registerPhoto(Long userId, List<MultipartFile> photos) {
//        checkPhotoQuantity(photos);
//        findUserById(userId);
//
//        List<String> photoUrls = s3Service.savePhotos(photos);
//        for(String photoUrl : photoUrls) {
//            UserPhoto userPhoto = UserPhoto.builder()
//                    .userId(userId)
//                    .photoUrl(photoUrl)
//                    .build();
//            userPhotoRepository.save(userPhoto);
//        }
//
//        return UserRegisterResponse.builder()
//                .userId(userId)
//                .build();
//    }

    public UserRegisterResponse updateNickname(Long userId, NicknameRequest request) {
        User user = findUserById(userId);
        checkDuplication(request.getNickname());
        user.changeNickname(request.getNickname());
        return UserRegisterResponse.builder()
                .userId(userId)
                .build();
    }

    public UserRegisterResponse updateBirth(Long userId, BirthRequest request) {
        User user = findUserById(userId);
        user.changeBirthYear(request.getBirthYear());
        user.changeBirthMonth(request.getBirthMonth());
        user.changeBirthDay(request.getBirthDay());

        return UserRegisterResponse.builder()
                .userId(userId)
                .build();
    }

    public UserRegisterResponse updateGender(Long userId, GenderRequest request) {
        User user = findUserById(userId);
        user.changeGender(request.getGender());

        return UserRegisterResponse.builder()
                .userId(userId)
                .build();
    }

    public UserRegisterResponse updateMbti(Long userId, MbtiRequest request) {
        User user = findUserById(userId);
        user.changeMbti(request.getMbti());

        return UserRegisterResponse.builder()
                .userId(userId)
                .build();
    }

    public UserRegisterResponse updateIncome(Long userId, IncomeRequest request) {
        User user = findUserById(userId);
        user.changeIncome(request.getIncome());

        return UserRegisterResponse.builder()
                .userId(userId)
                .build();
    }

    public UserRegisterResponse updateAddress(Long userId, AddressRequest request) {
        User user = findUserById(userId);
        user.changeAddress(request.getAddr1(), request.getAddr2(), request.getAddr3());

        return UserRegisterResponse.builder()
                .userId(userId)
                .build();
    }

    public UserRegisterResponse updateEducation(Long userId, EducationRequest request) {
        User user = findUserById(userId);
        user.changeEducation(request.getEducation(), request.getSchool());

        return UserRegisterResponse.builder()
                .userId(userId)
                .build();
    }

    public UserRegisterResponse updateJob(Long userId, JobRequest request) {
        User user = findUserById(userId);
        user.changeJob(request.getJobId());
        isJobExist(request.getJobId());
        return UserRegisterResponse.builder()
                .userId(userId)
                .build();
    }

    public UserRegisterResponse updatePhoneNum(Long userId, PhoneNumRequest request) {
        User user = findUserById(userId);
        user.changePhoneNum(request.getPhoneNum());

        return UserRegisterResponse.builder()
                .userId(userId)
                .build();
    }

    public UserRegisterResponse updateHopeAge(Long userId, HopeAgeRequest request) {
        User user = findUserById(userId);
        user.changeHopeAge(request.getMinAge(), request.getMaxAge());

        return UserRegisterResponse.builder()
                .userId(userId)
                .build();
    }

    public UserRegisterResponse updateHobby(Long userId, HobbyRequest request) {
        findUserById(userId);
        List<Long> originHobbies = request.getOriginHobbies();
        List<String> newHobbies = request.getNewHobbies();

        updateUserHobby(userId, originHobbies);
        registerNewHobby(userId, newHobbies);

        return UserRegisterResponse.builder()
                .userId(userId)
                .build();
    }

    public UserRegisterResponse updateBelief(Long userId, BeliefRequest request) {
        findUserById(userId);
        List<Long> originBeliefs = request.getOriginBeliefs();
        List<String> newBeliefs = request.getNewBeliefs();

        updateUserBelief(userId, originBeliefs);
        registerNewBelief(userId, newBeliefs);

        return UserRegisterResponse.builder()
            .userId(userId)
            .build();
    }

    private void checkDuplication(String nickname) {
        userRepository.findByNickname(nickname)
                .ifPresent(user -> {throw new UserException(ErrorCode.NICKNAME_ALREADY_EXISTS);});
    }

    private User findUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND));
    }

    private void checkPhotoQuantity(List<MultipartFile> photos) {
        if (photos.size() < 2) {
            throw new UserException(ErrorCode.PHOTO_QUANTITY_ERROR);
        }
    }

    private void isJobExist(Long jobId) {
        jobRepository.findById(jobId)
                .orElseThrow(() -> new UserException(ErrorCode.JOB_NOT_FOUND));
    }

    private void registerNewHobby(Long userId, List<String> newHobbies) {
        for(String hobbyName : newHobbies) {
            Hobby hobby = createHobby(hobbyName);
            UserHobby userHobby = UserHobby.builder()
                    .userId(userId)
                    .hobbyId(hobby.getId())
                    .build();
            userHobbyRepository.save(userHobby);
        }
    }

    private void updateUserHobby(Long userId, List<Long> originHobbies) {
        for(Long hobbyId : originHobbies) {
            Hobby hobby = findHobbyById(hobbyId);
            hobby.addCount();
            UserHobby userHobby = UserHobby.builder()
                    .userId(userId)
                    .hobbyId(hobbyId)
                    .build();
            userHobbyRepository.save(userHobby);
        }
    }

    private Hobby createHobby(String hobbyName) {
        hobbyRepository.findByName(hobbyName)
                .ifPresent(hobby -> { throw new UserException(ErrorCode.HOBBY_ALREADY_EXISTS); });
        Hobby hobby = Hobby.builder()
                .name(hobbyName)
                .build();
        hobbyRepository.save(hobby);
        return hobby;
    }

    private Hobby findHobbyById(Long hobbyId) {
        return hobbyRepository.findById(hobbyId)
                .orElseThrow(() -> new UserException(ErrorCode.HOBBY_NOT_FOUND));
    }

    private void registerNewBelief(Long userId, List<String> newBeliefs) {
        for(String beliefName : newBeliefs) {
            Belief belief = createBelief(beliefName);
            UserBelief userBelief = UserBelief.builder()
                .userId(userId)
                .beliefId(belief.getId())
                .build();
            userBeliefRepository.save(userBelief);
        }
    }

    private void updateUserBelief(Long userId, List<Long> originBeliefs) {
        for(Long beliefId : originBeliefs) {
            Belief belief = findBeliefById(beliefId);
            belief.addCount();
            UserBelief userBelief = UserBelief.builder()
                .userId(userId)
                .beliefId(belief.getId())
                .build();
            userBeliefRepository.save(userBelief);
        }
    }

    private Belief createBelief(String beliefName) {
        beliefRepository.findByName(beliefName)
            .ifPresent(belief -> { throw new UserException(ErrorCode.BELIEF_ALREADY_EXISTS); });
        Belief belief = Belief.builder()
            .name(beliefName)
            .build();
        beliefRepository.save(belief);
        return belief;
    }

    private Belief findBeliefById(Long beliefId) {
        return beliefRepository.findById(beliefId)
            .orElseThrow(() -> new UserException(ErrorCode.BELIEF_NOT_FOUND));
    }
}
