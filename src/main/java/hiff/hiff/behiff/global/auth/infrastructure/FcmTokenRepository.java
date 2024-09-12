package hiff.hiff.behiff.global.auth.infrastructure;

import hiff.hiff.behiff.global.auth.domain.FcmToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FcmTokenRepository extends JpaRepository<FcmToken, Long> {

    FcmToken findByUserId(Long userId);
}
