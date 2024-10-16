package hiff.hiff.behiff.domain.user.application;

import hiff.hiff.behiff.domain.user.domain.entity.Hobby;
import hiff.hiff.behiff.domain.user.domain.entity.UserFashion;
import hiff.hiff.behiff.domain.user.domain.entity.UserHobby;
import hiff.hiff.behiff.domain.user.domain.enums.Fashion;
import hiff.hiff.behiff.domain.user.infrastructure.UserFashionRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class UserFashionService {

    private final UserFashionRepository userFashionRepository;

    public void updateFashion(Long userId, List<Fashion> fashions) {
        userFashionRepository.deleteByUserId(userId);

        for (Fashion fashion : fashions) {
            log.info(fashion + " ");
            UserFashion userFashion = UserFashion.builder()
                .userId(userId)
                .fashion(fashion)
                .build();
            userFashionRepository.save(userFashion);
        }
    }

    public List<String> findNameByUser(Long userId) {
        return userFashionRepository.findByUserId(userId)
            .stream()
            .map(userFashion -> {
                Fashion fashion = userFashion.getFashion();
                return fashion.getText();
            })
            .toList();
    }
}
