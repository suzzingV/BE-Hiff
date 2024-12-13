package hiff.hiff.behiff.domain.profile.infrastructure;

import hiff.hiff.behiff.domain.profile.domain.entity.UserPhoto;
import hiff.hiff.behiff.domain.profile.domain.entity.VerificationPhoto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface VerificationPhotoRepository extends JpaRepository<VerificationPhoto, Long> {

    Optional<VerificationPhoto> findByUserId(Long userId);
}
