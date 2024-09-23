package hiff.hiff.behiff.domain.user.application;

import hiff.hiff.behiff.domain.user.domain.entity.User;
import hiff.hiff.behiff.domain.user.domain.entity.UserPhoto;
import hiff.hiff.behiff.domain.user.exception.UserException;
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
    private final GcsService gcsService;
    private final UserCRUDService userCRUDService;

    private static final String MAIN_PHOTO_FOLDER_NAME = "main_photo";
    public static final String PHOTOS_FOLDER_NAME = "photos";
    private static final int PHOTO_COUNT_LIMIT = 2;

    public void registerPhoto(Long userId, MultipartFile mainPhoto, List<MultipartFile> photos) {
        String mainPhotoUrl = gcsService.saveImage(mainPhoto, MAIN_PHOTO_FOLDER_NAME);
        saveMainPhotoUrl(userId, mainPhotoUrl);

        for (MultipartFile photo : photos) {
            String photoUrl = gcsService.saveImage(photo, PHOTOS_FOLDER_NAME);
            savePhotoUrl(userId, photoUrl);
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
        if (trashPhotos != null) {
            trashPhotos
                .forEach(photoUrl -> {
                    userPhotoRepository.deleteByPhotoUrl(photoUrl);
                    gcsService.deleteImage(photoUrl, PHOTOS_FOLDER_NAME);
                });
        }
    }

    public void checkPhotoQuantity(List<MultipartFile> photos) {
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
        User user = userCRUDService.findById(userId);
            gcsService.deleteImage(user.getMainPhoto(), MAIN_PHOTO_FOLDER_NAME);
        user.updateMainPhoto(mainPhotoUrl);
    }
}
