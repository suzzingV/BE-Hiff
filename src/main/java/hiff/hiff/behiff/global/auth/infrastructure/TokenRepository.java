package hiff.hiff.behiff.global.auth.infrastructure;

import hiff.hiff.behiff.global.auth.domain.Token;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenRepository extends JpaRepository<Token, Long> {

    Optional<Token> findByUserId(Long userId);
}
