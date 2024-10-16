package hiff.hiff.behiff.domain.user.presentation.controller;

import hiff.hiff.behiff.domain.user.application.UserServiceFacade;
import hiff.hiff.behiff.domain.user.domain.entity.User;
import hiff.hiff.behiff.domain.user.presentation.dto.req.BirthRequest;
import hiff.hiff.behiff.domain.user.presentation.dto.req.CareerRequest;
import hiff.hiff.behiff.domain.user.presentation.dto.req.DistanceRequest;
import hiff.hiff.behiff.domain.user.presentation.dto.req.EducationRequest;
import hiff.hiff.behiff.domain.user.presentation.dto.req.GenderRequest;
import hiff.hiff.behiff.domain.user.presentation.dto.req.HobbyRequest;
import hiff.hiff.behiff.domain.user.presentation.dto.req.HopeAgeRequest;
import hiff.hiff.behiff.domain.user.presentation.dto.req.LifeStyleRequest;
import hiff.hiff.behiff.domain.user.presentation.dto.req.MbtiRequest;
import hiff.hiff.behiff.domain.user.presentation.dto.req.NicknameRequest;
import hiff.hiff.behiff.domain.user.presentation.dto.req.PosRequest;
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
    public ResponseEntity<UserUpdateResponse> updateSmokingStatus(@AuthenticationPrincipal User user, SmokingRequest request) {
        UserUpdateResponse response = userServiceFacade.updateSmokingStatus(user, request);
        return ResponseEntity.ok(response);
    }
}
