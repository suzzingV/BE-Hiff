package hiff.hiff.behiff.domain.profile.application.service;

import hiff.hiff.behiff.domain.matching.exception.MatchingException;
import hiff.hiff.behiff.domain.profile.domain.entity.UserPos;
import hiff.hiff.behiff.domain.user.exception.UserException;
import hiff.hiff.behiff.domain.profile.infrastructure.UserPosRepository;
import hiff.hiff.behiff.domain.profile.presentation.dto.res.ProfileUpdateResponse;
import hiff.hiff.behiff.global.response.properties.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserPosService {

    private final UserPosRepository userPosRepository;

    public void createPos(Long userId, Double lat, Double lon) {
        UserPos userPos = UserPos.builder()
            .userId(userId)
            .lat(lat)
            .lon(lon)
            .build();

        userPosRepository.save(userPos);
    }

    public ProfileUpdateResponse updatePos(Long userId, Double x, Double y) {
        UserPos userPos = userPosRepository.findByUserId(userId)
            .orElseThrow(() -> new UserException(ErrorCode.USER_POS_NOT_FOUND));
        userPos.changePos(x, y);

        return ProfileUpdateResponse.builder()
            .userId(userId)
            .build();
    }

    public UserPos findPosByUserId(Long userId) {
        return userPosRepository.findByUserId(userId)
            .orElseThrow(() -> new MatchingException(ErrorCode.USER_POS_NOT_FOUND));
    }

    public void deleteByUserId(Long userId) {
        userPosRepository.deleteByUserId(userId);
    }

}
