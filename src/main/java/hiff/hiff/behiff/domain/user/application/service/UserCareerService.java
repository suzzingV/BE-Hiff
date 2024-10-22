package hiff.hiff.behiff.domain.user.application.service;

import hiff.hiff.behiff.domain.catalog.application.service.CatalogCareerService;
import hiff.hiff.behiff.domain.catalog.domain.entity.Career;
import hiff.hiff.behiff.domain.user.domain.entity.User;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class UserCareerService {

    private final CatalogCareerService catalogCareerService;

    public void updateOriginCareer(User user, Long careerId) {
        Career career = catalogCareerService.findById(careerId);
        user.changeCareer(career.getName());
        career.addCount();
    }
}
