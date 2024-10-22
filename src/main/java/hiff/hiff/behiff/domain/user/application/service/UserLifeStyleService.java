package hiff.hiff.behiff.domain.user.application.service;

import hiff.hiff.behiff.domain.catalog.application.service.CatalogLifeStyleService;
import hiff.hiff.behiff.domain.catalog.domain.entity.LifeStyle;
import hiff.hiff.behiff.domain.matching.application.dto.NameWithCommonDto;
import hiff.hiff.behiff.domain.user.domain.entity.UserLifeStyle;
import hiff.hiff.behiff.domain.user.infrastructure.UserLifeStyleRepository;
import hiff.hiff.behiff.domain.user.presentation.dto.res.UserUpdateResponse;
import jakarta.transaction.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class UserLifeStyleService {

    private final UserLifeStyleRepository userLifeStyleRepository;
    private final CatalogLifeStyleService catalogLifeStyleService;


    public UserUpdateResponse updateLifeStyle(Long userId, List<Long> lifeStyles) {
        updateUserLifeStyles(userId, lifeStyles);

        return UserUpdateResponse.from(userId);
    }

    public List<String> findNamesByUser(Long userId) {
        return userLifeStyleRepository.findByUserId(userId)
            .stream()
            .map(userLifeStyle -> {
                LifeStyle lifeStyle = catalogLifeStyleService.findLifeStyleById(
                    userLifeStyle.getLifeStyleId());
                return lifeStyle.getName();
            })
            .toList();
    }

    public List<UserLifeStyle> findByUserId(Long userId) {
        return userLifeStyleRepository.findByUserId(userId);
    }

    public List<NameWithCommonDto> getLifeStylesWithCommon(Long matcherId, Long matchedId) {
        List<String> matcherLifeStyles = findNamesByUser(matcherId);
        List<String> matchedLifeStyles = findNamesByUser(matchedId);

        return matchedLifeStyles.stream()
            .map(lifeStyle -> {
                boolean isCommon = matcherLifeStyles.contains(lifeStyle);
                return NameWithCommonDto.builder()
                    .name(lifeStyle)
                    .isCommon(isCommon)
                    .build();
            }).toList();
    }

    private void updateUserLifeStyles(Long userId, List<Long> originLifeStyles) {
        userLifeStyleRepository.deleteByUserId(userId);

        for (Long lifeStyleId : originLifeStyles) {
            LifeStyle lifeStyle = catalogLifeStyleService.findLifeStyleById(lifeStyleId);
            lifeStyle.addCount();
            UserLifeStyle userLifeStyle = UserLifeStyle.builder()
                .userId(userId)
                .lifeStyleId(lifeStyle.getId())
                .build();
            userLifeStyleRepository.save(userLifeStyle);
        }
    }
}
