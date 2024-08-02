package hiff.hiff.behiff.domain.user.application;

import hiff.hiff.behiff.domain.user.domain.entity.Belief;
import hiff.hiff.behiff.domain.user.domain.entity.UserBelief;
import hiff.hiff.behiff.domain.user.exception.UserException;
import hiff.hiff.behiff.domain.user.infrastructure.BeliefRepository;
import hiff.hiff.behiff.domain.user.infrastructure.UserBeliefRepository;
import hiff.hiff.behiff.domain.user.presentation.dto.req.BeliefRequest;
import hiff.hiff.behiff.domain.user.presentation.dto.res.UserUpdateResponse;
import hiff.hiff.behiff.global.response.properties.ErrorCode;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class UserBeliefService {

    private final BeliefRepository beliefRepository;
    private final UserBeliefRepository userBeliefRepository;

    public UserUpdateResponse updateBelief(Long userId, BeliefRequest request) {
        List<Long> originBeliefs = request.getOriginBeliefs();
        List<String> newBeliefs = request.getNewBeliefs();

        updateUserBelief(userId, originBeliefs);
        registerNewBelief(userId, newBeliefs);

        return UserUpdateResponse.from(userId);
    }

    public List<String> getBeliefsOfUser(Long userId) {
        return userBeliefRepository.findByUserId(userId)
                .stream()
                .map(userBelief -> {
                    Belief belief = findBeliefById(userBelief.getBeliefId());
                    return belief.getName();
                })
                .toList();
    }

    private void registerNewBelief(Long userId, List<String> newBeliefs) {
        for(String beliefName : newBeliefs) {
            Belief belief = createBelief(beliefName);
            UserBelief userBelief = UserBelief.builder()
                    .userId(userId)
                    .beliefId(belief.getId())
                    .build();
            userBeliefRepository.save(userBelief);
        }
    }

    private void updateUserBelief(Long userId, List<Long> originBeliefs) {
        List<UserBelief> oldBeliefs = userBeliefRepository.findByUserId(userId);
        userBeliefRepository.deleteAll(oldBeliefs);

        for(Long beliefId : originBeliefs) {
            Belief belief = findBeliefById(beliefId);
            belief.addCount();
            UserBelief userBelief = UserBelief.builder()
                    .userId(userId)
                    .beliefId(belief.getId())
                    .build();
            userBeliefRepository.save(userBelief);
        }
    }

    private Belief createBelief(String beliefName) {
        beliefRepository.findByName(beliefName)
                .ifPresent(belief -> { throw new UserException(ErrorCode.BELIEF_ALREADY_EXISTS); });
        Belief belief = Belief.builder()
                .name(beliefName)
                .build();
        beliefRepository.save(belief);
        return belief;
    }

    private Belief findBeliefById(Long beliefId) {
        return beliefRepository.findById(beliefId)
                .orElseThrow(() -> new UserException(ErrorCode.BELIEF_NOT_FOUND));
    }
}
