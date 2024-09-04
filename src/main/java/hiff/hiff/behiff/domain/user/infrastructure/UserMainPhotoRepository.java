package hiff.hiff.behiff.domain.user.infrastructure;

import hiff.hiff.behiff.domain.user.domain.entity.UserMainPhoto;
import hiff.hiff.behiff.domain.user.domain.entity.UserPhoto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserMainPhotoRepository extends JpaRepository<UserMainPhoto, Long> {

    Optional<UserMainPhoto> findByUserId(Long userId);
}
