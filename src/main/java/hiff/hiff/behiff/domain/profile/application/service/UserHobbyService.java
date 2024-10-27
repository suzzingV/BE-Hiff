package hiff.hiff.behiff.domain.profile.application.service;

import hiff.hiff.behiff.domain.catalog.application.service.CatalogHobbyService;
import hiff.hiff.behiff.domain.catalog.domain.entity.Hobby;
import hiff.hiff.behiff.domain.matching.application.dto.NameWithCommonDto;
import hiff.hiff.behiff.domain.profile.domain.entity.UserHobby;
import hiff.hiff.behiff.domain.profile.infrastructure.UserHobbyRepository;
import hiff.hiff.behiff.domain.profile.presentation.dto.res.ProfileUpdateResponse;
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

    public ProfileUpdateResponse updateHobby(Long userId, List<Long> hobbies) {
        updateUserHobbies(userId, hobbies);
        return ProfileUpdateResponse.from(userId);
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

    public void deleteByUserId(Long userId) {
        userHobbyRepository.deleteByUserId(userId);
    }
}
