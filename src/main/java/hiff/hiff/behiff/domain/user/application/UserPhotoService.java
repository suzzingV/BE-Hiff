package hiff.hiff.behiff.domain.user.application;

import hiff.hiff.behiff.domain.user.domain.entity.UserPhoto;
import hiff.hiff.behiff.domain.user.exception.UserException;
import hiff.hiff.behiff.domain.user.infrastructure.UserPhotoRepository;
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
//    private final S3Service s3Service;

    // TODO: 대표 사진 등록
//    public void registerPhoto(MultipartFile mainPhoto, List<MultipartFile> photos) {
//        checkPhotoQuantity(mainPhoto, photos);
//
//        List<String> photoUrls = s3Service.savePhotos(photo, photos);
//        for(String photoUrl : photoUrls) {
//            UserPhoto userPhoto = UserPhoto.builder()
//                    .userId(userId)
//                    .photoUrl(photoUrl)
//                    .build();
//            userPhotoRepository.save(userPhoto);
//        }
//    }

    public List<String> getPhotosOfUser(Long userId) {
        return userPhotoRepository.findByUserId(userId)
            .stream()
            .map(UserPhoto::getPhotoUrl)
            .toList();
    }

    private void checkPhotoQuantity(List<MultipartFile> photos) {
        if (photos.size() < 2) {
            throw new UserException(ErrorCode.PHOTO_QUANTITY_ERROR);
        }
    }
}
