package hiff.hiff.behiff.domain.user.application;

import hiff.hiff.behiff.domain.user.domain.entity.User;
import hiff.hiff.behiff.domain.user.domain.enums.Role;
import hiff.hiff.behiff.domain.user.domain.enums.SocialType;
import hiff.hiff.behiff.domain.user.exception.UserException;
import hiff.hiff.behiff.domain.user.infrastructure.UserPhotoRepository;
import hiff.hiff.behiff.domain.user.infrastructure.UserRepository;
import hiff.hiff.behiff.domain.user.presentation.dto.UserRegisterResponse;
import hiff.hiff.behiff.domain.user.presentation.dto.req.BirthRequest;
import hiff.hiff.behiff.domain.user.presentation.dto.req.GenderRequest;
import hiff.hiff.behiff.domain.user.presentation.dto.req.IncomeRequest;
import hiff.hiff.behiff.domain.user.presentation.dto.req.MbtiRequest;
import hiff.hiff.behiff.domain.user.presentation.dto.req.NicknameRequest;
import hiff.hiff.behiff.global.auth.jwt.service.JwtService;
import hiff.hiff.behiff.global.response.properties.ErrorCode;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final UserPhotoRepository userPhotoRepository;
    private final JwtService jwtService;
//    private final S3Service s3Service;

    public User registerUser(String email, String socialId, SocialType socialType,
        Role role) {
        User user = User.builder()
            .role(role)
            .socialType(socialType)
            .socialId(socialId)
            .email(email)
            .build();
        return userRepository.save(user);
    }

    public void withdraw(Long userId, Optional<String> accessToken, Optional<String> refreshToken) {
        User user = findUserById(userId);
        user.delete();

        String access = accessToken
            .orElseThrow(() -> new UserException(ErrorCode.SECURITY_INVALID_ACCESS_TOKEN));
        String refresh = refreshToken
            .orElseThrow(() -> new UserException(ErrorCode.REFRESH_TOKEN_REQUIRED));

        jwtService.isTokenValid(refresh);
        jwtService.isTokenValid(access);
        jwtService.deleteRefreshToken(refresh);
        jwtService.invalidAccessToken(access);
    }

    public User findUserById(Long id) {
        return userRepository.findById(id)
            .orElseThrow(() -> new UserException(ErrorCode.USER_NOT_FOUND));
    }

//    public UserRegisterResponse registerPhoto(Long userId, List<MultipartFile> photos) {
//        checkPhotoQuantity(photos);
//        findUserById(userId);
//
//        List<String> photoUrls = s3Service.savePhotos(photos);
//        for(String photoUrl : photoUrls) {
//            UserPhoto userPhoto = UserPhoto.builder()
//                    .userId(userId)
//                    .photoUrl(photoUrl)
//                    .build();
//            userPhotoRepository.save(userPhoto);
//        }
//
//        return UserRegisterResponse.builder()
//                .userId(userId)
//                .build();
//    }

    public UserRegisterResponse updateNickname(Long userId, NicknameRequest request) {
        User user = findUserById(userId);
        user.changeNickname(request.getNickname());
        return UserRegisterResponse.builder()
            .userId(userId)
            .build();
    }

    public UserRegisterResponse updateBirth(Long userId, BirthRequest request) {
        User user = findUserById(userId);
        user.changeBirthYear(request.getBirthYear());
        user.changeBirthMonth(request.getBirthMonth());
        user.changeBirthDay(request.getBirthDay());

        return UserRegisterResponse.builder()
            .userId(userId)
            .build();
    }

    public UserRegisterResponse updateGender(Long userId, GenderRequest request) {
        User user = findUserById(userId);
        user.changeGender(request.getGender());

        return UserRegisterResponse.builder()
            .userId(userId)
            .build();
    }

    public UserRegisterResponse updateMbti(Long userId, MbtiRequest request) {
        User user = findUserById(userId);
        user.changeMbti(request.getMbti());

        return UserRegisterResponse.builder()
            .userId(userId)
            .build();
    }

    public UserRegisterResponse updateIncome(Long userId, IncomeRequest request) {
        User user = findUserById(userId);
        user.changeIncome(request.getIncome());

        return UserRegisterResponse.builder()
            .userId(userId)
            .build();
    }

    private void checkPhotoQuantity(List<MultipartFile> photos) {
        if (photos.size() < 2) {
            throw new UserException(ErrorCode.PHOTO_QUANTITY_ERROR);
        }
    }
}
