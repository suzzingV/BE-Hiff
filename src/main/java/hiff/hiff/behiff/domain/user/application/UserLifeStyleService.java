package hiff.hiff.behiff.domain.user.application;

import hiff.hiff.behiff.domain.user.domain.entity.LifeStyle;
import hiff.hiff.behiff.domain.user.domain.entity.UserLifeStyle;
import hiff.hiff.behiff.domain.user.exception.UserException;
import hiff.hiff.behiff.domain.user.infrastructure.LifeStyleRepository;
import hiff.hiff.behiff.domain.user.infrastructure.UserLifeStyleRepository;
import hiff.hiff.behiff.domain.user.presentation.dto.req.LifeStyleRequest;
import hiff.hiff.behiff.domain.user.presentation.dto.res.UserUpdateResponse;
import hiff.hiff.behiff.global.response.properties.ErrorCode;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class UserLifeStyleService {

    private final LifeStyleRepository lifeStyleRepository;
    private final UserLifeStyleRepository userLifeStyleRepository;

    public UserUpdateResponse updateLifeStyle(Long userId, LifeStyleRequest request) {
        List<Long> originLifeStyles = request.getOriginLifeStyles();
//        List<String> newLifeStyles = request.getNewLifeStyles();

        updateUserLifeStyles(userId, originLifeStyles);
//        registerNewLifeStyles(userId, newLifeStyles);

        return UserUpdateResponse.from(userId);
    }

    public List<String> findLifeStylesByUser(Long userId) {
        return userLifeStyleRepository.findByUserId(userId)
                .stream()
                .map(userLifeStyle -> {
                    LifeStyle lifeStyle = findLifeStyleById(userLifeStyle.getLifeStyleId());
                    return lifeStyle.getName();
                })
                .toList();
    }

    public List<LifeStyle> getAllLifeStyles() {
        return lifeStyleRepository.findAll();
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
        List<UserLifeStyle> oldLifeStyles = userLifeStyleRepository.findByUserId(userId);
        userLifeStyleRepository.deleteAll(oldLifeStyles);

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
