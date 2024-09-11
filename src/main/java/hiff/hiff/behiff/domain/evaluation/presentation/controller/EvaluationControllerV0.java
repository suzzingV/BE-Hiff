package hiff.hiff.behiff.domain.evaluation.presentation.controller;

import hiff.hiff.behiff.domain.user.application.UserPhotoService;
import hiff.hiff.behiff.domain.user.application.UserService;
import hiff.hiff.behiff.domain.user.domain.entity.User;
import hiff.hiff.behiff.domain.user.infrastructure.UserRepository;
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
    private final UserService userService;
    private final UserPhotoService userPhotoService;

    @GetMapping("/users")
    public String getUsersWithoutAppearanceScore(Model model) {
        List<User> usersWithoutScore = userRepository.findUsersWithoutAppearanceScore(0.0);
        model.addAttribute("users", usersWithoutScore);
        return "not-evaluated-users";
    }

    // 유저 상세 페이지
    @GetMapping("/detail/{id}")
    public String getUserDetail(@PathVariable Long id, Model model) {
        User user = userService.findById(id);
        List<String> photos = userPhotoService.getPhotosOfUser(id);
        model.addAttribute("user", user);
        model.addAttribute("main", user.getMainPhoto());
        model.addAttribute("photos", photos);
        return "user-detail";
    }

    // 외모 점수 업데이트
    @PostMapping("/score")
    public String updateAppearanceScore(@RequestParam("userId") Long userId, @RequestParam("score") Double score) {
        User user = userService.findById(userId);
        user.updateEvaluatedScoreTmp(score);
        userRepository.save(user);
        return "redirect:/evaluation/users";
    }
}
