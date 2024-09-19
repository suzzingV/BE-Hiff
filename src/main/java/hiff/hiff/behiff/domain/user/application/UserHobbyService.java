package hiff.hiff.behiff.domain.user.application;

import hiff.hiff.behiff.domain.matching.application.dto.NameWithCommonDto;
import hiff.hiff.behiff.domain.user.domain.entity.Hobby;
import hiff.hiff.behiff.domain.user.domain.entity.HobbySimilarity;
import hiff.hiff.behiff.domain.user.domain.entity.UserHobby;
import hiff.hiff.behiff.domain.user.exception.UserException;
import hiff.hiff.behiff.domain.user.infrastructure.HobbyRepository;
import hiff.hiff.behiff.domain.user.infrastructure.HobbySimilarityRepository;
import hiff.hiff.behiff.domain.user.infrastructure.UserHobbyRepository;
import hiff.hiff.behiff.domain.user.presentation.dto.res.UserUpdateResponse;
import hiff.hiff.behiff.global.common.redis.RedisService;
import hiff.hiff.behiff.global.response.properties.ErrorCode;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserHobbyService {

    private final HobbyRepository hobbyRepository;
    private final UserHobbyRepository userHobbyRepository;
    private final RedisService redisService;
    private final HobbySimilarityRepository hobbySimilarityRepository;

    public static final String HOBBY_PREFIX = "hobby_";

    public UserUpdateResponse updateHobby(Long userId, List<Long> hobbies) {
//        List<String> newHobbies = request.getNewHobbies();

        updateUserHobbies(userId, hobbies);
//        registerNewHobbies(userId, newHobbies); // TODO : 레디스 캐싱

        return UserUpdateResponse.from(userId);
    }

    public List<String> findNameByUser(Long userId) {
        return userHobbyRepository.findByUserId(userId)
            .stream()
            .map(userHobby -> {
                Hobby hobby = findHobbyById(userHobby.getHobbyId());
                return hobby.getName();
            })
            .toList();
    }

    public List<UserHobby> findByUserId(Long userId) {
        return userHobbyRepository.findByUserId(userId);
    }

    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public List<Hobby> getAllHobbies() {
        return hobbyRepository.findAll();
    }

    public List<NameWithCommonDto> getHobbiesWithCommon(Long matcherId, Long matchedId) {
        List<String> matcherHobbies = findNameByUser(matcherId);
        List<String> matchedHobbies = findNameByUser(matchedId);

        return matchedHobbies.stream()
            .map(hobby -> {
                boolean isCommon = matcherHobbies.contains(hobby);
                return NameWithCommonDto.builder()
                    .name(hobby)
                    .isCommon(isCommon)
                    .build();
            }).toList();
    }

    public void cacheHobbySimilarity() {
        for(long i = 1345L; i <= 1610; i++) {
            hobbySimilarityRepository.findByFromId(i)
                .forEach(hobbySimilarity -> {
                    Long fromHobbyId = hobbySimilarity.getId().getFromHobbyId();
                    Long toHobbyId = hobbySimilarity.getId().getToHobbyId();
                    int similarity = (int)Math.round(hobbySimilarity.getSimilarity() * 100);
                    redisService.setValue(HOBBY_PREFIX + fromHobbyId + "_" + toHobbyId, String.valueOf(
                        similarity));
                });
        }
    }

//    private void registerNewHobbies(Long userId, List<String> newHobbies) {
//        for (String hobbyName : newHobbies) {
//            Hobby hobby = createHobby(hobbyName);
//            UserHobby userHobby = UserHobby.builder()
//                    .userId(userId)
//                    .hobbyId(hobby.getId())
//                    .build();
//            userHobbyRepository.save(userHobby);
//        }
//    }

    private void updateUserHobbies(Long userId, List<Long> originHobbies) {
        List<UserHobby> oldHobbies = userHobbyRepository.findByUserId(userId);
        userHobbyRepository.deleteAll(oldHobbies);

        for (Long hobbyId : originHobbies) {
            Hobby hobby = findHobbyById(hobbyId);
            hobby.addCount();
            UserHobby userHobby = UserHobby.builder()
                .userId(userId)
                .hobbyId(hobbyId)
                .build();
            userHobbyRepository.save(userHobby);
        }
    }

//    private Hobby createHobby(String hobbyName) {
//        hobbyRepository.findByName(hobbyName)
//                .ifPresent(hobby -> {
//                    throw new UserException(ErrorCode.HOBBY_ALREADY_EXISTS);
//                });
//        Hobby hobby = Hobby.builder()
//                .name(hobbyName)
//                .build();
//        hobbyRepository.save(hobby);
//        return hobby;
//    }

    private Hobby findHobbyById(Long hobbyId) {
        return hobbyRepository.findById(hobbyId)
            .orElseThrow(() -> new UserException(ErrorCode.HOBBY_NOT_FOUND));
    }
}
