package hiff.hiff.behiff.domain.user.application.service;

import hiff.hiff.behiff.domain.catalog.application.service.CatalogHobbyService;
import hiff.hiff.behiff.domain.catalog.domain.entity.Hobby;
import hiff.hiff.behiff.domain.matching.application.dto.NameWithCommonDto;
import hiff.hiff.behiff.domain.user.domain.entity.UserHobby;
import hiff.hiff.behiff.domain.user.infrastructure.UserHobbyRepository;
import hiff.hiff.behiff.domain.user.presentation.dto.res.UserUpdateResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserHobbyService {

    private final CatalogHobbyService catalogHobbyService;
    private final UserHobbyRepository userHobbyRepository;

    public UserUpdateResponse updateHobby(Long userId, List<Long> hobbies) {
        updateUserHobbies(userId, hobbies);
        return UserUpdateResponse.from(userId);
    }

    public List<String> findNameByUser(Long userId) {
        return userHobbyRepository.findByUserId(userId)
            .stream()
            .map(userHobby -> {
                Hobby hobby = catalogHobbyService.findHobbyById(userHobby.getHobbyId());
                return hobby.getName();
            })
            .toList();
    }

    public List<UserHobby> findByUserId(Long userId) {
        return userHobbyRepository.findByUserId(userId);
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

    private void updateUserHobbies(Long userId, List<Long> originHobbies) {
        userHobbyRepository.deleteByUserId(userId);

        for (Long hobbyId : originHobbies) {
            Hobby hobby = catalogHobbyService.findHobbyById(hobbyId);
            hobby.addCount();
            UserHobby userHobby = UserHobby.builder()
                .userId(userId)
                .hobbyId(hobbyId)
                .build();
            userHobbyRepository.save(userHobby);
        }
    }
}
