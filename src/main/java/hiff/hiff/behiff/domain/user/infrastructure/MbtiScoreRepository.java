package hiff.hiff.behiff.domain.user.infrastructure;

import hiff.hiff.behiff.domain.user.domain.compositeKey.HobbySimId;
import hiff.hiff.behiff.domain.user.domain.compositeKey.MbtiScoreKey;
import hiff.hiff.behiff.domain.user.domain.entity.HobbySimilarity;
import hiff.hiff.behiff.domain.user.domain.entity.MbtiScore;
import hiff.hiff.behiff.domain.user.domain.enums.Mbti;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MbtiScoreRepository extends JpaRepository<MbtiScore, MbtiScoreKey> {

}
