package hiff.hiff.behiff.domain.user.application;

import hiff.hiff.behiff.domain.user.domain.entity.*;
import hiff.hiff.behiff.domain.user.domain.enums.*;
import hiff.hiff.behiff.domain.user.exception.UserException;
import hiff.hiff.behiff.domain.user.infrastructure.*;
import hiff.hiff.behiff.domain.user.presentation.dto.req.*;
import hiff.hiff.behiff.domain.user.presentation.dto.res.MyInfoResponse;
import hiff.hiff.behiff.domain.user.presentation.dto.res.UserRegisterResponse;
import hiff.hiff.behiff.global.auth.jwt.service.JwtService;
import hiff.hiff.behiff.global.response.properties.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
    private final WeightValueRepository weightValueRepository;
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

    // TODO: 대표 사진 등록
//    public UserRegisterResponse registerPhoto(Long userId, MultipartFile mainPhoto, List<MultipartFile> photos) {
//        checkPhotoQuantity(mainPhoto, photos);
//        findUserById(userId);
//
//        List<String> photoUrls = s3Service.savePhotos(photo, photos);
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
        user.changeBirth(request.getBirthYear(), request.getBirthMonth(), request.getBirthDay());

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
        if(request.isOpen()) {
            user.changeIncome(request.getIncome());
        }

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

        updateUserHobbies(userId, originHobbies);
        registerNewHobbies(userId, newHobbies);

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

    public UserRegisterResponse updateDistance(Long userId, DistanceRequest request) {
        User user = findUserById(userId);
        checkDistanceRange(request);
        user.changeMaxDistance(request.getMaxDistance());
        user.changeMinDistance(request.getMinDistance());

        return UserRegisterResponse.builder()
            .userId(userId)
            .build();
    }

    public UserRegisterResponse updateWeightValue(Long userId, WeightValueRequest request) {
        findUserById(userId);
        weightValueRepository.findByUserId(userId)
                .ifPresentOrElse(weightValue ->
                        weightValue.changeWeightValue(request.getIncome(), request.getAppearance(), request.getHobby(),
                        request.getBelief(), request.getMbti()), () -> {
                    WeightValue weightValue = WeightValue.builder()
                            .userId(userId)
                                    .income(request.getIncome())
                                    .appearance(request.getAppearance())
                                    .hobby(request.getHobby())
                                    .belief(request.getBelief())
                                    .mbti(request.getMbti())
                                    .build();
                    weightValueRepository.save(weightValue);
                        }
                );
        return UserRegisterResponse.builder()
                .userId(userId)
                .build();
    }

    public MyInfoResponse getMyInfo(Long userId) {
        User user = findUserById(userId);
        List<String> photos = userPhotoRepository.findByUserId(userId)
                .stream()
                .map(UserPhoto::getPhotoUrl)
                .toList();
        List<String> hobbies = userHobbyRepository.findByUserId(userId)
                .stream()
                .map(userHobby -> {
                    Hobby hobby = findHobbyById(userHobby.getHobbyId());
                    return hobby.getName();
                })
                .toList();
        List<String> beliefs = userBeliefRepository.findByUserId(userId)
                .stream()
                .map(userBelief -> {
                    Belief belief = findBeliefById(userBelief.getBeliefId());
                    return belief.getName();
                })
                .toList();
        WeightValue weightValue = weightValueRepository.findByUserId(userId)
                .orElseThrow(() -> new UserException(ErrorCode.WEIGHT_VALUE_NOT_FOUND));
        if(user.getJobId() == null) {
            throw new UserException(ErrorCode.USER_JOB_NOT_EXISTS);
        }
        Job job = jobRepository.findById(user.getJobId())
                .orElseThrow(() -> new UserException(ErrorCode.JOB_NOT_FOUND));

        return MyInfoResponse.builder()
                .email(user.getEmail())
                .nickname(user.getNickname())
                .age(user.getAge())
                .maxDistance(user.getMaxDistance())
                .minDistance(user.getMinDistance())
                .photos(photos)
                .hobbies(hobbies)
                .beliefs(beliefs)
                .incomeWeight(weightValue.getIncome())
                .beliefWeight(weightValue.getBelief())
                .hobbyWeight(weightValue.getHobby())
                .appearanceWeight(weightValue.getAppearance())
                .mbtiWeight(weightValue.getMbti())
                .phoneNum(user.getPhoneNum())
                .gender(user.getGender())
                .mbti(user.getMbti())
                .income(user.getIncome())
                .addr(user.getAddr())
                .education(user.getEducation())
                .school(user.getSchool())
                .job(job.getName())
                .hopeMinAge(user.getHopeMinAge())
                .hopeMaxAge(user.getHopeMaxAge())
                .build();
    }

    private static void checkDistanceRange(DistanceRequest request) {
        if(request.getMinDistance() > request.getMaxDistance()) {
            throw new UserException(ErrorCode.DISTANCE_RANGE_REVERSE);
        }
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

    private void registerNewHobbies(Long userId, List<String> newHobbies) {
        for(String hobbyName : newHobbies) {
            Hobby hobby = createHobby(hobbyName);
            UserHobby userHobby = UserHobby.builder()
                    .userId(userId)
                    .hobbyId(hobby.getId())
                    .build();
            userHobbyRepository.save(userHobby);
        }
    }

    private void updateUserHobbies(Long userId, List<Long> originHobbies) {
        List<UserHobby> oldHobbies = userHobbyRepository.findByUserId(userId);
        userHobbyRepository.deleteAll(oldHobbies);

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
        List<UserBelief> oldBeliefs = userBeliefRepository.findByUserId(userId);
        userBeliefRepository.deleteAll(oldBeliefs);

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
