package hiff.hiff.behiff.domain.user.infrastructure;

import hiff.hiff.behiff.domain.user.domain.entity.UserPhoto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserPhotoRepository extends JpaRepository<UserPhoto, Long> {

}
