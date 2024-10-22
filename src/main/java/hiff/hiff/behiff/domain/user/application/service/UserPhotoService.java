package hiff.hiff.behiff.domain.user.application.service;

import hiff.hiff.behiff.domain.user.domain.entity.User;
import hiff.hiff.behiff.domain.user.domain.entity.UserPhoto;
import hiff.hiff.behiff.domain.user.infrastructure.UserPhotoRepository;
import hiff.hiff.behiff.domain.user.presentation.dto.res.SignedUrlResponse;
import hiff.hiff.behiff.global.common.gcs.GcsService;
import jakarta.transaction.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class UserPhotoService {

    private final UserPhotoRepository userPhotoRepository;
    private final GcsService gcsService;
    private final UserCRUDService userCRUDService;

    private static final String MAIN_PHOTO_FOLDER_NAME = "main_photo";
    public static final String PHOTOS_FOLDER_NAME = "photos";
    private static final int PHOTO_COUNT_LIMIT = 2;

    public void registerMainPhoto(Long userId, String mainPhoto) {
        if (mainPhoto == null) {
            return;
        }
        saveMainPhotoUrl(userId, mainPhoto);
    }

    public void registerPhotos(Long userId, List<String> photos) {
        if (photos != null) {
            for (String photo : photos) {
                savePhotoUrl(userId, photo);
            }
        }
    }

    public List<String> getPhotosOfUser(Long userId) {
        List<UserPhoto> userPhotos = userPhotoRepository.findByUserId(userId);
        return userPhotos
            .stream()
            .map(UserPhoto::getPhotoUrl)
            .toList();
    }

    public void deletePhotos(List<String> trashPhotos) {
        if (!trashPhotos.isEmpty()) {
            trashPhotos
                .forEach(photoUrl -> {
                    userPhotoRepository.deleteByPhotoUrl(photoUrl);
                    gcsService.deleteImage(photoUrl, PHOTOS_FOLDER_NAME);
                });
        }
    }

    private void savePhotoUrl(Long userId, String photoUrl) {
        UserPhoto userPhoto = UserPhoto.builder()
            .userId(userId)
            .photoUrl(photoUrl)
            .build();
        userPhotoRepository.save(userPhoto);
    }

    public SignedUrlResponse generateSingedUrl(String mainPhotoName, List<String> photoNames) {
        String mainSignedUrl = gcsService.generateSignedUrl(MAIN_PHOTO_FOLDER_NAME, mainPhotoName);
        List<String> signedUrls = photoNames.stream().map(photoName -> {
            return gcsService.generateSignedUrl(PHOTOS_FOLDER_NAME, photoName);
        }).toList();

        return SignedUrlResponse.builder()
            .mainSignedUrl(mainSignedUrl)
            .signedUrls(signedUrls)
            .build();
    }

    private void saveMainPhotoUrl(Long userId, String mainPhotoUrl) {
        User user = userCRUDService.findById(userId);
        gcsService.deleteImage(user.getMainPhoto(), MAIN_PHOTO_FOLDER_NAME);
        user.updateMainPhoto(mainPhotoUrl);
    }
}
