package hiff.hiff.behiff.domain.catalog.application.service;

import hiff.hiff.behiff.domain.catalog.domain.entity.Hobby;
import hiff.hiff.behiff.domain.catalog.exception.CatalogException;
import hiff.hiff.behiff.domain.catalog.infrastructure.HobbyRepository;
import hiff.hiff.behiff.domain.catalog.infrastructure.HobbySimilarityRepository;
import hiff.hiff.behiff.global.common.redis.RedisService;
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
public class CatalogHobbyService {

    private final HobbyRepository hobbyRepository;
    private final RedisService redisService;
    private final HobbySimilarityRepository hobbySimilarityRepository;

    public static final String HOBBY_PREFIX = "hobby_";

    @Transactional(readOnly = true)
    public List<Hobby> getAllHobbies() {
        return hobbyRepository.findAll();
    }

    public Hobby findHobbyById(Long hobbyId) {
        return hobbyRepository.findById(hobbyId)
            .orElseThrow(() -> new CatalogException(ErrorCode.HOBBY_NOT_FOUND));
    }

    public void cacheHobbySimilarity() {
        for (long i = 1345L; i <= 1610; i++) {
            hobbySimilarityRepository.findByFromId(i)
                .forEach(hobbySimilarity -> {
                    Long fromHobbyId = hobbySimilarity.getId().getFromHobbyId();
                    Long toHobbyId = hobbySimilarity.getId().getToHobbyId();
                    int similarity = (int) Math.round(hobbySimilarity.getSimilarity() * 100);
                    redisService.setValue(HOBBY_PREFIX + fromHobbyId + "_" + toHobbyId,
                        String.valueOf(
                            similarity));
                });
        }
    }
}
