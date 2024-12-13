package hiff.hiff.behiff.global.auth.infrastructure;

import hiff.hiff.behiff.global.auth.domain.entity.Device;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeviceRepository extends JpaRepository<Device, Long> {

    Optional<Device> findByUserId(Long userId);

    void deleteByUserId(Long userId);
}
