package hiff.hiff.behiff.domain.catalog.application.service;

import hiff.hiff.behiff.domain.catalog.domain.entity.LifeStyle;
import hiff.hiff.behiff.domain.catalog.exception.CatalogException;
import hiff.hiff.behiff.domain.catalog.infrastructure.LifeStyleRepository;
import hiff.hiff.behiff.global.response.properties.ErrorCode;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class CatalogLifeStyleService {

    private final LifeStyleRepository lifeStyleRepository;

    public static final String LIFESTYLE_PREFIX = "lifestyle_";

    public List<LifeStyle> getAllLifeStyles() {
        return lifeStyleRepository.findAll();
    }

    public LifeStyle findLifeStyleById(Long lifeStyleId) {
        return lifeStyleRepository.findById(lifeStyleId)
            .orElseThrow(() -> new CatalogException(ErrorCode.LIFESTYLE_NOT_FOUND));
    }
}
