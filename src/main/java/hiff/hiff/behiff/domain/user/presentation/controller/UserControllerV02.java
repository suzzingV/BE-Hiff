package hiff.hiff.behiff.domain.user.presentation.controller;

import hiff.hiff.behiff.domain.user.application.UserServiceFacade;
import hiff.hiff.behiff.domain.user.domain.entity.User;
import hiff.hiff.behiff.domain.user.presentation.dto.req.BirthRequest;
import hiff.hiff.behiff.domain.user.presentation.dto.req.BuddyRequest;
import hiff.hiff.behiff.domain.user.presentation.dto.req.CareerRequest;
import hiff.hiff.behiff.domain.user.presentation.dto.req.ContactFrequencyRequest;
import hiff.hiff.behiff.domain.user.presentation.dto.req.DistanceRequest;
import hiff.hiff.behiff.domain.user.presentation.dto.req.DrinkingRequest;
import hiff.hiff.behiff.domain.user.presentation.dto.req.EducationRequest;
import hiff.hiff.behiff.domain.user.presentation.dto.req.GenderRequest;
import hiff.hiff.behiff.domain.user.presentation.dto.req.HobbyRequest;
import hiff.hiff.behiff.domain.user.presentation.dto.req.HopeAgeRequest;
import hiff.hiff.behiff.domain.user.presentation.dto.req.IdeologyRequest;
import hiff.hiff.behiff.domain.user.presentation.dto.req.LifeStyleRequest;
import hiff.hiff.behiff.domain.user.presentation.dto.req.MbtiRequest;
import hiff.hiff.behiff.domain.user.presentation.dto.req.NicknameRequest;
import hiff.hiff.behiff.domain.user.presentation.dto.req.PosRequest;
import hiff.hiff.behiff.domain.user.presentation.dto.req.ReligionRequest;
import hiff.hiff.behiff.domain.user.presentation.dto.req.SchoolRequest;
import hiff.hiff.behiff.domain.user.presentation.dto.req.SmokingRequest;
import hiff.hiff.behiff.domain.user.presentation.dto.req.UserPhotoRequest;
import hiff.hiff.behiff.domain.user.presentation.dto.req.WeightValueRequest;
import hiff.hiff.behiff.domain.user.presentation.dto.res.TagResponse;
import hiff.hiff.behiff.domain.user.presentation.dto.res.UserInfoResponse;
import hiff.hiff.behiff.domain.user.presentation.dto.res.UserUpdateResponse;
import hiff.hiff.behiff.domain.user.presentation.dto.res.UserWeightValueResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "User", description = "User 관련 API")
@RestController
@RequestMapping("/api/v0.2/user")
@RequiredArgsConstructor
public class UserControllerV02 {

    private final UserServiceFacade userServiceFacade;

    @Operation(
        summary = "흡연 여부 갱신",
        description = "흡연 여부를 갱신합니다. 토큰 o"
    )
    @ApiResponse(
        responseCode = "200",
        description = "흡연 여부 갱신에 성공하였습니다."
    )
    @PatchMapping("/smoking")
    public ResponseEntity<UserUpdateResponse> updateSmokingStatus(@AuthenticationPrincipal User user, @RequestBody @Valid SmokingRequest request) {
        UserUpdateResponse response = userServiceFacade.updateSmokingStatus(user.getId(), request);
        return ResponseEntity.ok(response);
    }

    @Operation(
        summary = "음주 여부 갱신",
        description = "음주 여부를 갱신합니다. 토큰 o"
    )
    @ApiResponse(
        responseCode = "200",
        description = "음주 여부 갱신에 성공하였습니다."
    )
    @PatchMapping("/drinking")
    public ResponseEntity<UserUpdateResponse> updateDrinkingStatus(@AuthenticationPrincipal User user, @RequestBody @Valid DrinkingRequest request) {
        UserUpdateResponse response = userServiceFacade.updateDrinkingStatus(user.getId(), request);
        return ResponseEntity.ok(response);
    }

    @Operation(
        summary = "친한 이성 수 갱신",
        description = "친한 이성 수를 갱신합니다. 토큰 o"
    )
    @ApiResponse(
        responseCode = "200",
        description = "친한 이성 수 갱신에 성공하였습니다."
    )
    @PatchMapping("/opposite-buddy")
    public ResponseEntity<UserUpdateResponse> updateBuddyCount(@AuthenticationPrincipal User user, @RequestBody @Valid BuddyRequest request) {
        UserUpdateResponse response = userServiceFacade.updateBuddy(user.getId(), request);
        return ResponseEntity.ok(response);
    }

    @Operation(
        summary = "종교 갱신",
        description = "종교를 갱신합니다. 토큰 o"
    )
    @ApiResponse(
        responseCode = "200",
        description = "종교 갱신에 성공하였습니다."
    )
    @PatchMapping("/religion")
    public ResponseEntity<UserUpdateResponse> updateReligion(@AuthenticationPrincipal User user, @RequestBody @Valid ReligionRequest request) {
        UserUpdateResponse response = userServiceFacade.updateReligion(user.getId(), request);
        return ResponseEntity.ok(response);
    }

    @Operation(
        summary = "종교 갱신",
        description = "종교를 갱신합니다. 토큰 o"
    )
    @ApiResponse(
        responseCode = "200",
        description = "종교 갱신에 성공하였습니다."
    )
    @PatchMapping("/ideology")
    public ResponseEntity<UserUpdateResponse> updateIdeology(@AuthenticationPrincipal User user, @RequestBody @Valid IdeologyRequest request) {
        UserUpdateResponse response = userServiceFacade.updateIdeology(user.getId(), request);
        return ResponseEntity.ok(response);
    }

    @Operation(
        summary = "연인과의 연락 빈도 갱신",
        description = "연인과의 연락 빈도를 갱신합니다. 토큰 o"
    )
    @ApiResponse(
        responseCode = "200",
        description = "연인과의 연락 빈도 갱신에 성공하였습니다."
    )
    @PatchMapping("/contact-frequency")
    public ResponseEntity<UserUpdateResponse> updateContactFrequency(@AuthenticationPrincipal User user, @RequestBody @Valid ContactFrequencyRequest request) {
        UserUpdateResponse response = userServiceFacade.updateContactFrequency(user.getId(), request);
        return ResponseEntity.ok(response);
    }
}
