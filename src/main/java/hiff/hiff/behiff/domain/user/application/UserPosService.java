package hiff.hiff.behiff.domain.user.application;

import hiff.hiff.behiff.domain.matching.exception.MatchingException;
import hiff.hiff.behiff.domain.user.domain.entity.UserPos;
import hiff.hiff.behiff.domain.user.exception.UserException;
import hiff.hiff.behiff.domain.user.infrastructure.UserPosRepository;
import hiff.hiff.behiff.domain.user.presentation.dto.res.UserUpdateResponse;
import hiff.hiff.behiff.global.response.properties.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserPosService {

    private final UserPosRepository userPosRepository;

    public void createPos(Long userId, Double x, Double y) {
        UserPos userPos = UserPos.builder()
            .userId(userId)
            .x(x)
            .y(y)
            .build();

        userPosRepository.save(userPos);
    }

    public UserUpdateResponse updatePos(Long userId, Double x, Double y) {
        UserPos userPos = userPosRepository.findByUserId(userId)
            .orElseThrow(() -> new UserException(ErrorCode.USER_POS_NOT_FOUND));
        userPos.changePos(x, y);

        return UserUpdateResponse.builder()
            .userId(userId)
            .build();
    }

    public UserPos findPosByUserId(Long userId) {
        return userPosRepository.findByUserId(userId)
            .orElseThrow(() -> new MatchingException(ErrorCode.USER_POS_NOT_FOUND));
    }
}
