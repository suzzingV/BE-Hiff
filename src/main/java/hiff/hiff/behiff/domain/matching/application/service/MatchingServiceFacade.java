package hiff.hiff.behiff.domain.matching.application.service;

import hiff.hiff.behiff.domain.matching.presentation.dto.res.DailyMatchingDetailResponse;
import hiff.hiff.behiff.domain.matching.presentation.dto.res.HiffMatchingDetailResponse;
import hiff.hiff.behiff.domain.matching.presentation.dto.res.MatchingSimpleResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class MatchingServiceFacade {

    private final DailyMatchingService dailyMatchingService;

    private final HiffMatchingService hiffMatchingService;

    public List<MatchingSimpleResponse> getDailyMatching(Long userId) {
        return dailyMatchingService.getMatchings(userId);
    }

    public List<MatchingSimpleResponse> getPaidDailyMatching(Long userId) {
        return dailyMatchingService.getPaidMatching(userId);
    }

    public DailyMatchingDetailResponse getDailyMatchingDetails(Long matcherId, Long matchedId) {
        return dailyMatchingService.getMatchingDetails(matcherId, matchedId);
    }

    public List<MatchingSimpleResponse> getHiffMatching(Long userId) {
        return hiffMatchingService.getMatchings(userId);
    }

    public MatchingSimpleResponse performHiffMatching(Long userId) {
        return hiffMatchingService.performMatching(userId);
    }

    public HiffMatchingDetailResponse getHiffMatchingDetails(Long matcherId, Long matchedId) {
        return hiffMatchingService.getMatchingDetails(matcherId, matchedId);
    }
}
