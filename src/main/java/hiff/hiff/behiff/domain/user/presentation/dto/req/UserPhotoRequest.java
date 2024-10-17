package hiff.hiff.behiff.domain.user.presentation.dto.req;

import java.util.List;
import lombok.Getter;

@Getter
public class UserPhotoRequest {

    private String newMainPhoto;

    private List<String> newPhotos;

    private List<String> trashPhotos;
}
