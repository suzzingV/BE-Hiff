package hiff.hiff.behiff.domain.user.application;

import hiff.hiff.behiff.domain.user.domain.entity.Hobby;
import hiff.hiff.behiff.domain.user.domain.entity.UserHobby;
import hiff.hiff.behiff.domain.user.exception.UserException;
import hiff.hiff.behiff.domain.user.infrastructure.HobbyRepository;
import hiff.hiff.behiff.domain.user.infrastructure.UserHobbyRepository;
import hiff.hiff.behiff.domain.user.presentation.dto.req.HobbyRequest;
import hiff.hiff.behiff.domain.user.presentation.dto.res.UserUpdateResponse;
import hiff.hiff.behiff.global.response.properties.ErrorCode;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class UserHobbyService {

    private final HobbyRepository hobbyRepository;
    private final UserHobbyRepository userHobbyRepository;

    public UserUpdateResponse updateHobby(Long userId, HobbyRequest request) {
        List<Long> originHobbies = request.getOriginHobbies();
        List<String> newHobbies = request.getNewHobbies();

        updateUserHobbies(userId, originHobbies);
        registerNewHobbies(userId, newHobbies);

        return UserUpdateResponse.from(userId);
    }

    public List<String> getHobbiesOfUser(Long userId) {
        return userHobbyRepository.findByUserId(userId)
                .stream()
                .map(userHobby -> {
                    Hobby hobby = findHobbyById(userHobby.getHobbyId());
                    return hobby.getName();
                })
                .toList();
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
}
