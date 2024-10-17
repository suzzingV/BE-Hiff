package hiff.hiff.behiff.domain.user.application.service;

import hiff.hiff.behiff.domain.matching.application.dto.NameWithCommonDto;
import hiff.hiff.behiff.domain.user.domain.entity.LifeStyle;
import hiff.hiff.behiff.domain.user.domain.entity.UserLifeStyle;
import hiff.hiff.behiff.domain.user.exception.UserException;
import hiff.hiff.behiff.domain.user.infrastructure.LifeStyleRepository;
import hiff.hiff.behiff.domain.user.infrastructure.LifeStyleSimilarityRepository;
import hiff.hiff.behiff.domain.user.infrastructure.UserLifeStyleRepository;
import hiff.hiff.behiff.domain.user.presentation.dto.res.UserUpdateResponse;
import hiff.hiff.behiff.global.common.redis.RedisService;
import hiff.hiff.behiff.global.response.properties.ErrorCode;
import jakarta.transaction.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class UserLifeStyleService {

    private final LifeStyleRepository lifeStyleRepository;
    private final UserLifeStyleRepository userLifeStyleRepository;
    private final RedisService redisService;
    private final LifeStyleSimilarityRepository lifeStyleSimilarityRepository;

    public static final String LIFESTYLE_PREFIX = "lifestyle_";

    public UserUpdateResponse updateLifeStyle(Long userId, List<Long> lifeStyles) {
        updateUserLifeStyles(userId, lifeStyles);

        return UserUpdateResponse.from(userId);
    }

    public List<String> findNamesByUser(Long userId) {
        return userLifeStyleRepository.findByUserId(userId)
            .stream()
            .map(userLifeStyle -> {
                LifeStyle lifeStyle = findLifeStyleById(userLifeStyle.getLifeStyleId());
                return lifeStyle.getName();
            })
            .toList();
    }

    public List<UserLifeStyle> findByUserId(Long userId) {
        return userLifeStyleRepository.findByUserId(userId);
    }

    public List<LifeStyle> getAllLifeStyles() {
        return lifeStyleRepository.findAll();
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

    public void cacheLifeStyleSimilarity() {
        lifeStyleSimilarityRepository.findAll()
            .forEach(lifeStyleSimilarity -> {
                Long fromlifeStyleId = lifeStyleSimilarity.getId().getFromLifestyleId();
                Long tolifeStyleId = lifeStyleSimilarity.getId().getToLifestyleId();
                int similarity = (int)Math.round(lifeStyleSimilarity.getSimilarity() * 100);
                redisService.setValue(LIFESTYLE_PREFIX + fromlifeStyleId + "_" + tolifeStyleId, String.valueOf(similarity));
            });
    }

//    private void registerNewLifeStyles(Long userId, List<String> newLifeStyles) {
//        for (String lifeStyleName : newLifeStyles) {
//            LifeStyle lifeStyle = createLifeStyle(lifeStyleName);
//            UserLifeStyle userLifeStyle = UserLifeStyle.builder()
//                    .userId(userId)
//                    .lifeStyleId(lifeStyle.getId())
//                    .build();
//            userLifeStyleRepository.save(userLifeStyle);
//        }
//    }

    private void updateUserLifeStyles(Long userId, List<Long> originLifeStyles) {
        userLifeStyleRepository.deleteByUserId(userId);

        for (Long lifeStyleId : originLifeStyles) {
            LifeStyle lifeStyle = findLifeStyleById(lifeStyleId);
            lifeStyle.addCount();
            UserLifeStyle userLifeStyle = UserLifeStyle.builder()
                .userId(userId)
                .lifeStyleId(lifeStyle.getId())
                .build();
            userLifeStyleRepository.save(userLifeStyle);
        }
    }

//    private LifeStyle createLifeStyle(String lifeStyleName) {
//        lifeStyleRepository.findByName(lifeStyleName)
//                .ifPresent(lifeStyle -> {
//                    throw new UserException(ErrorCode.LIFESTYLE_ALREADY_EXISTS);
//                });
//        LifeStyle lifeStyle = LifeStyle.builder()
//                .name(lifeStyleName)
//                .build();
//        lifeStyleRepository.save(lifeStyle);
//        return lifeStyle;
//    }

    private LifeStyle findLifeStyleById(Long lifeStyleId) {
        return lifeStyleRepository.findById(lifeStyleId)
            .orElseThrow(() -> new UserException(ErrorCode.LIFESTYLE_NOT_FOUND));
    }
}
