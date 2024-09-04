package hiff.hiff.behiff.domain.user.application;

import hiff.hiff.behiff.domain.user.domain.entity.UserMainPhoto;
import hiff.hiff.behiff.domain.user.domain.entity.UserPhoto;
import hiff.hiff.behiff.domain.user.exception.UserException;
import hiff.hiff.behiff.domain.user.infrastructure.UserMainPhotoRepository;
import hiff.hiff.behiff.domain.user.infrastructure.UserPhotoRepository;
import hiff.hiff.behiff.global.common.gcs.GcsService;
import hiff.hiff.behiff.global.response.properties.ErrorCode;
import jakarta.transaction.Transactional;

import java.util.List;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Transactional
public class UserPhotoService {

    private final UserPhotoRepository userPhotoRepository;
    private final UserMainPhotoRepository userMainPhotoRepository;
    private final GcsService gcsService;

    private static final String MAIN_PHOTO_FOLDER_NAME = "main_photo";
    private static final String PHOTOS_FOLDER_NAME = "photos";
    private static final int PHOTO_COUNT_LIMIT = 2;

    public void registerPhoto(Long userId, MultipartFile mainPhoto, List<MultipartFile> photos) {
        checkPhotoQuantity(photos);

        deleteOldMainPhoto(userId);
        String mainPhotoUrl = gcsService.saveImage(mainPhoto, MAIN_PHOTO_FOLDER_NAME);
        saveMainPhotoUrl(userId, mainPhotoUrl);

        deleteOldPhotos(userId);
        for(MultipartFile photo : photos) {
            String photoUrl = gcsService.saveImage(photo, PHOTOS_FOLDER_NAME);
            savePhotoUrl(userId, photoUrl);
        }
    }

    public String getMainPhotoOfUser(Long userId) {
        UserMainPhoto userMainPhoto = userMainPhotoRepository.findByUserId(userId)
                .orElseThrow(() -> new UserException(ErrorCode.MAIN_PHOTO_NOT_FOUND));
        return userMainPhoto.getPhotoUrl();
    }

    public List<String> getPhotosOfUser(Long userId) {
        List<UserPhoto> userPhotos = userPhotoRepository.findByUserId(userId);
        if(userPhotos.isEmpty()) {
            throw new UserException(ErrorCode.PHOTOS_NOT_FOUND);
        }
        return userPhotos
            .stream()
            .map(UserPhoto::getPhotoUrl)
            .toList();
    }

    private void deleteOldPhotos(Long userId) {
        userPhotoRepository.findByUserId(userId)
                .forEach(userPhoto -> {
                    String photoUrl = userPhoto.getPhotoUrl();
                    gcsService.deleteImage(photoUrl, PHOTOS_FOLDER_NAME);
                    userPhotoRepository.delete(userPhoto);
                });
    }

    private void deleteOldMainPhoto(Long userId) {
        userMainPhotoRepository.findByUserId(userId)
                .ifPresent(userMainPhoto -> {
                    String photoUrl = userMainPhoto.getPhotoUrl();
                    gcsService.deleteImage(photoUrl, MAIN_PHOTO_FOLDER_NAME);
                    userMainPhotoRepository.delete(userMainPhoto);
                });
    }

    private void checkPhotoQuantity(List<MultipartFile> photos) {
        if (photos.size() < PHOTO_COUNT_LIMIT) {
            throw new UserException(ErrorCode.PHOTO_QUANTITY_ERROR);
        }
    }

    private void savePhotoUrl(Long userId, String mainPhotoUrl) {
        UserPhoto userPhoto = UserPhoto.builder()
                .userId(userId)
                .photoUrl(mainPhotoUrl)
                .build();
        userPhotoRepository.save(userPhoto);
    }

    private void saveMainPhotoUrl(Long userId, String mainPhotoUrl) {
        UserMainPhoto userMainPhoto = UserMainPhoto.builder()
                .userId(userId)
                .photoUrl(mainPhotoUrl)
                .build();
        userMainPhotoRepository.save(userMainPhoto);
    }
}
