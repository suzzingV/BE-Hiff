package hiff.hiff.behiff.domain.user.application;

import hiff.hiff.behiff.domain.user.domain.entity.UserPos;
import hiff.hiff.behiff.domain.user.exception.UserException;
import hiff.hiff.behiff.domain.user.infrastructure.UserPosRepository;
import hiff.hiff.behiff.domain.user.presentation.dto.req.PosRequest;
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

    public UserUpdateResponse createPos(Long userId, PosRequest request) {
        UserPos userPos = UserPos.builder()
                .userId(userId)
                .x(request.getX())
                .y(request.getY())
                .build();

        userPosRepository.save(userPos);

        return UserUpdateResponse.builder()
                .userId(userId)
                .build();
    }

    public UserUpdateResponse updatePos(Long userId, PosRequest request) {
        UserPos userPos = userPosRepository.findByUserId(userId)
                .orElseThrow(() -> new UserException(ErrorCode.USER_POS_NOT_FOUND));
        userPos.changePos(request.getX(), request.getY());

        return UserUpdateResponse.builder()
                .userId(userId)
                .build();
    }
}
