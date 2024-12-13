package hiff.hiff.behiff.domain.profile.application.service;

import hiff.hiff.behiff.domain.profile.domain.entity.UserPhoto;
import hiff.hiff.behiff.domain.profile.domain.entity.UserProfile;
import hiff.hiff.behiff.domain.profile.domain.entity.VerificationPhoto;
import hiff.hiff.behiff.domain.profile.domain.enums.VerificationStatus;
import hiff.hiff.behiff.domain.profile.exception.ProfileException;
import hiff.hiff.behiff.domain.profile.infrastructure.UserPhotoRepository;
import hiff.hiff.behiff.domain.profile.infrastructure.VerificationPhotoRepository;
import hiff.hiff.behiff.domain.profile.presentation.dto.res.SignedUrlResponse;
import hiff.hiff.behiff.domain.profile.presentation.dto.res.VerificationPhotoResponse;
import hiff.hiff.behiff.global.common.gcs.GcsService;
import hiff.hiff.behiff.global.response.properties.ErrorCode;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class VerificationPhotoService {

    private final VerificationPhotoRepository verificationPhotoRepository;
    private final GcsService gcsService;

    public static final String VERFICATION_PHOTO_FOLDER = "verification_photo";

    public void registerVerificationPhoto(Long userId, String photoUrl) {
        VerificationPhoto verificationPhoto = VerificationPhoto.builder()
                .photoUrl(photoUrl)
                .userId(userId)
                .status(VerificationStatus.IN_PROGRESS)
                .build();
        verificationPhotoRepository.save(verificationPhoto);
    }

    public String getPhoto(Long userId) {
        VerificationPhoto verificationPhoto = verificationPhotoRepository.findByUserId(userId)
                .orElseThrow(() -> new ProfileException(ErrorCode.VERIFICATION_PHOTO_NOT_FOUND));
        return verificationPhoto.getPhotoUrl();
    }

    public void deletePhoto(Long userId) {
        VerificationPhoto verificationPhoto = verificationPhotoRepository.findByUserId(userId)
                .orElseThrow(() -> new ProfileException(ErrorCode.VERIFICATION_PHOTO_NOT_FOUND));
        gcsService.deleteImage(verificationPhoto.getPhotoUrl(), VERFICATION_PHOTO_FOLDER);
        verificationPhotoRepository.delete(verificationPhoto);
    }
}
