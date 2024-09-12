package hiff.hiff.behiff.domain.evaluation.presentation.controller;

import hiff.hiff.behiff.domain.matching.application.service.MatchingServiceFacade;
import hiff.hiff.behiff.domain.matching.presentation.dto.res.MatchingSimpleResponse;
import hiff.hiff.behiff.domain.user.application.UserPhotoService;
import hiff.hiff.behiff.domain.user.application.UserServiceFacade;
import hiff.hiff.behiff.domain.user.domain.entity.User;
import hiff.hiff.behiff.domain.user.infrastructure.UserRepository;
import hiff.hiff.behiff.global.auth.domain.FcmToken;
import hiff.hiff.behiff.global.auth.infrastructure.FcmTokenRepository;
import hiff.hiff.behiff.global.common.fcm.FcmUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/evaluation")
public class EvaluationControllerV0 {

    private final UserRepository userRepository;
    private final UserServiceFacade userServiceFacade;
    private final UserPhotoService userPhotoService;
    private final MatchingServiceFacade matchingServiceFacade;
    private final FcmTokenRepository fcmTokenRepository;

    @GetMapping("/users")
    public String getUsersWithoutAppearanceScore(Model model) {
        List<User> usersWithoutScore = userRepository.findUsersWithoutAppearanceScore(0.0);
        model.addAttribute("users", usersWithoutScore);
        return "not-evaluated-users";
    }

    // 유저 상세 페이지
    @GetMapping("/detail/{id}")
    public String getUserDetail(@PathVariable Long id, Model model) {
        User user = userServiceFacade.findById(id);
        List<String> photos = userPhotoService.getPhotosOfUser(id);
        model.addAttribute("user", user);
        model.addAttribute("main", user.getMainPhoto());
        model.addAttribute("photos", photos);
        return "user-detail";
    }

    // 외모 점수 업데이트
    @PostMapping("/score")
    public String updateAppearanceScore(@RequestParam("userId") Long userId, @RequestParam("score") Double score) {
        User user = userServiceFacade.findById(userId);
        user.updateEvaluatedScoreTmp(score);
        userRepository.save(user);
        MatchingSimpleResponse response = matchingServiceFacade.performHiffMatching(userId);

        FcmToken userToken = fcmTokenRepository.findByUserId(userId);
        FcmToken matchedToken = fcmTokenRepository.findByUserId(response.getUserId());
        FcmUtils.sendMatchingAlarm(userToken.getToken());
        FcmUtils.sendMatchingAlarm(matchedToken.getToken());
        return "redirect:/evaluation/users";
    }
}
