package hiff.hiff.behiff.domain.profile.application.service;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import hiff.hiff.behiff.domain.catalog.domain.entity.GenderCount;
import hiff.hiff.behiff.domain.catalog.infrastructure.GenderCountRepository;
import hiff.hiff.behiff.domain.catalog.infrastructure.MbtiScoreRepository;
import hiff.hiff.behiff.domain.profile.domain.entity.UserProfile;
import hiff.hiff.behiff.domain.profile.domain.enums.*;
import hiff.hiff.behiff.domain.profile.exception.ProfileException;
import hiff.hiff.behiff.domain.profile.infrastructure.UserProfileRepository;
import hiff.hiff.behiff.domain.user.exception.UserException;
import hiff.hiff.behiff.global.common.redis.RedisService;
import hiff.hiff.behiff.global.response.properties.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserProfileService {

    private final GenderCountRepository genderCountRepository;
    private final MbtiScoreRepository mbtiScoreRepository;
    private final RedisService redisService;
    private final UserProfileRepository userProfileRepository;
    private final WebClient webClient = WebClient.builder()
            .baseUrl("https://maps.googleapis.com")
            .build();
    @Value("${google.api.key}")
    private String apiKey;

    public static final String MBTI_PREFIX = "mbti_";

    public void updateNickname(UserProfile userProfile, String nickname) {
        checkNicknameDuplication(nickname);
        userProfile.changeNickname(nickname);
    }

    public void updateBirth(UserProfile userProfile, Integer birthYear, Integer birthMonth, Integer birthDay) {
        userProfile.changeBirth(birthYear, birthMonth, birthDay);
    }

    public void updateGender(UserProfile userProfile, Gender gender) {
        userProfile.updateGender(gender);
        updateGenderCount(gender);
    }

    private void updateGenderCount(Gender gender) {
        GenderCount genderCount = genderCountRepository.findById(gender)
            .orElseThrow(() -> new UserException(ErrorCode.GENDER_COUNT_NOT_FOUND));
        genderCount.addCount();
    }

    public void updateMbti(UserProfile userProfile, Mbti mbti) {
        userProfile.updateMbti(mbti);
    }

    public void cacheMbtiSimilarity() {
        mbtiScoreRepository.findAll()
            .forEach(mbtiScore -> {
                String mbti1 = mbtiScore.getId().getMbti1();
                String mbti2 = mbtiScore.getId().getMbti2();
                int similarity = mbtiScore.getScore();
                redisService.setValue(MBTI_PREFIX + mbti1 + "_" + mbti2,
                    String.valueOf(similarity));
            });
    }

    private void checkNicknameDuplication(String nickname) {
        userProfileRepository.findByNickname(nickname)
            .ifPresent(user -> {
                throw new ProfileException(ErrorCode.NICKNAME_ALREADY_EXISTS);
            });
    }

    public UserProfile findByUserId(Long userId) {
        return userProfileRepository.findByUserId(userId)
            .orElseThrow(() -> new ProfileException(ErrorCode.USER_PROFILE_NOT_FOUND));
    }

    public void deleteByUserId(Long userId) {
        userProfileRepository.deleteByUserId(userId);
    }

    public void createUserProfile(Long userId) {
        UserProfile userProfile = UserProfile.builder()
            .userId(userId)
            .build();
        userProfileRepository.save(userProfile);
    }

    public void updateLocationByPos(Long userId, double latitude, double longitude) {
        webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/maps/api/geocode/json")
                        .queryParam("latlng", latitude + "," + longitude)
                        .queryParam("key", apiKey)
                        .queryParam("language", "ko")
                        .build())
                .header("User-Agent", "SpringBoot WebClient")
                .retrieve()
                .bodyToMono(String.class)
                .map(this::extractLocation)
                .onErrorReturn(ErrorCode.ADDRESS_EXTRACT_ERROR.getMessage())
                .subscribe(
                        location -> updateLocation(userId, location),
                        error -> {
                            throw new ProfileException(ErrorCode.ADDRESS_EXTRACT_ERROR);
                        }
                );
    }

    private void updateLocation(Long userId, String location) {
        if(location == null) {
            return;
        }
        UserProfile userProfile = findByUserId(userId);
        userProfile.updateLocation(location);
        userProfileRepository.save(userProfile);
    }

    private String extractLocation(String response) {
        try {
            JsonObject jsonResponse = JsonParser.parseString(response).getAsJsonObject();
            if (jsonResponse.has("results") && jsonResponse.getAsJsonArray("results").size() > 0) {
                String address = jsonResponse.getAsJsonArray("results")
                        .get(0)
                        .getAsJsonObject()
                        .get("formatted_address")
                        .getAsString();
                String[] split = address.split(" ");
                return split[1] + " " + split[2];
            }
        } catch (Exception e) {
            throw new ProfileException(ErrorCode.ADDRESS_EXTRACT_ERROR);
        }

        return null;
    }
}
