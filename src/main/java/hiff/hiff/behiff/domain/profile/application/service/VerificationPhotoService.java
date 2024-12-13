package hiff.hiff.behiff.domain.profile.application.service;

import hiff.hiff.behiff.domain.profile.domain.entity.VerificationPhoto;
import hiff.hiff.behiff.domain.profile.domain.enums.VerificationStatus;
import hiff.hiff.behiff.domain.profile.exception.ProfileException;
import hiff.hiff.behiff.domain.profile.infrastructure.VerificationPhotoRepository;
import hiff.hiff.behiff.global.common.gcs.GcsService;
import hiff.hiff.behiff.global.response.properties.ErrorCode;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class VerificationPhotoService {

    private final VerificationPhotoRepository verificationPhotoRepository;
    private final GcsService gcsService;

    public static final String VERIFICATION_PHOTO_FOLDER = "verification_photo";

    public void registerVerificationPhoto(Long userId, String photoUrl) {
        deletePhoto(userId);
        VerificationPhoto verificationPhoto = VerificationPhoto.builder()
                .photoUrl(photoUrl)
                .userId(userId)
                .status(VerificationStatus.IN_PROGRESS)
                .build();
        verificationPhotoRepository.save(verificationPhoto);
    }

    public String getPhoto(Long userId) {
        VerificationPhoto verificationPhoto = findByUserId(userId);
        return verificationPhoto.getPhotoUrl();
    }

    public void deletePhoto(Long userId) {
        if(verificationPhotoRepository.findByUserId(userId).isEmpty()) {
            return;
        }
        VerificationPhoto verificationPhoto = findByUserId(userId);
        gcsService.deleteImage(verificationPhoto.getPhotoUrl(), VERIFICATION_PHOTO_FOLDER);
        verificationPhotoRepository.delete(verificationPhoto);
    }

    private VerificationPhoto findByUserId(Long userId) {
        return verificationPhotoRepository.findByUserId(userId)
                .orElseThrow(() -> new ProfileException(ErrorCode.VERIFICATION_PHOTO_NOT_FOUND));
    }

    public VerificationStatus getStatus(Long userId) {
        VerificationPhoto verificationPhoto = findByUserId(userId);
        return verificationPhoto.getStatus();
    }
}
