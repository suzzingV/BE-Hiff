package hiff.hiff.behiff.domain.matching.application.service;

import hiff.hiff.behiff.domain.matching.presentation.dto.res.MatchingDetailResponse;
import hiff.hiff.behiff.domain.matching.presentation.dto.res.MatchingSimpleResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class MatchingServiceFacade {

    private final MatchingService matchingService;
//
//    private final UserCRUDService userCRUDService;
//    private final SmsUtil smsUtil;

    public List<MatchingSimpleResponse> getRandomMatching(Long userId) {
        return matchingService.getMatchings(userId);
    }

    public MatchingDetailResponse getMatchingDetails(Long userId, Long matchedId) {
        return matchingService.getMatchingDetails(userId, matchedId);
    }
//
//    public List<MatchingSimpleResponse> getHiffMatching(Long userId) {
//        return hiffMatchingService.getMatchings(userId);
//    }
//
//    public Long performHiffMatching(Long userId) {
//        return hiffMatchingService.performMatching(userId);
//    }
//
//    public HiffMatchingDetailResponse getHiffMatchingDetails(Long matcherId, Long matchedId) {
//        return hiffMatchingService.getMatchingDetails(matcherId, matchedId);
//    }

//    public void matchUnmatched() {
//        userCRUDService.findAll()
//            .forEach(user -> {
//                if (!hiffMatchingService.checkMatchingHistory(user.getId())) {
//                    Long matchedId = hiffMatchingService.performMatching(user.getId());
//                    if (matchedId != null) {
//                        User matched = userCRUDService.findById(matchedId);
//                        smsUtil.sendMatchingMessage(user.getPhoneNum());
//                        smsUtil.sendMatchingMessage(matched.getPhoneNum());
//                    }
//                }
//            });
//    }
}
