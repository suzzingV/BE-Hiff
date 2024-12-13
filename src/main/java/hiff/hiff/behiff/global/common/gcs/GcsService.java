package hiff.hiff.behiff.global.common.gcs;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.HttpMethod;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import hiff.hiff.behiff.global.common.gcs.exception.GcsException;
import hiff.hiff.behiff.global.response.properties.ErrorCode;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ResourceUtils;

@Component
@Transactional
public class GcsService {

    @Value("${spring.cloud.gcp.storage.credentials.location}")
    private String keyFileName;

    @Value("${spring.cloud.gcp.storage.bucket}")
    private String bucketName;

    @Value("${spring.cloud.gcp.storage.project-id")
    private String projectId;

    private static final String STORAGE_URL = "https://storage.googleapis.com/";

//    public String saveImage(MultipartFile image, String folderName) {
//        String objectName = UUID.randomUUID().toString();
//        String imgUrl = STORAGE_URL + bucketName + "/" + folderName + "/" + objectName;
//
//        try {
//            InputStream keyFile = ResourceUtils.getURL(keyFileName).openStream();
//            String ext = image.getContentType();
//            Storage storage = StorageOptions.newBuilder()
//                .setCredentials(GoogleCredentials.fromStream(keyFile))
//                .build()
//                .getService();
//
//            if (image.isEmpty()) {
//                imgUrl = null;
//            } else {
//                BlobInfo blobInfo = BlobInfo.newBuilder(bucketName, folderName + "/" + objectName)
//                    .setContentType(ext).build();
//
//                storage.create(blobInfo, image.getInputStream());
//            }
//        } catch (Exception e) {
//            throw new GcsException(ErrorCode.IMAGE_STORAGE_SAVE_ERROR);
//        }
//
//        return imgUrl;
//    }
//
    public void deleteImage(String imgUrl, String folderName) {
        if(imgUrl == null) {
            return;
        }
        String objectName = getObjectNameFromUrl(imgUrl, folderName);

        try {
            InputStream keyFile = ResourceUtils.getURL(keyFileName).openStream();
            Storage storage = StorageOptions.newBuilder()
                .setCredentials(GoogleCredentials.fromStream(keyFile))
                .build()
                .getService();
            Blob blob = storage.get(bucketName, objectName);
            BlobId idWithGeneration = blob.getBlobId();
            storage.delete(idWithGeneration);
        } catch (Exception e) {
            throw new GcsException(ErrorCode.IMAGE_STORAGE_DELETE_ERROR);
        }
    }

    private String getObjectNameFromUrl(String imgUrl, String folder) {
        int folderIndex = imgUrl.indexOf(folder);
        return imgUrl.substring(folderIndex);
    }

    public String generateSignedUrl(String folderName, String objectName) {
        try {
        InputStream keyFile = ResourceUtils.getURL(keyFileName).openStream();
        Storage storage = StorageOptions.newBuilder()
                .setCredentials(GoogleCredentials.fromStream(keyFile))
                .build()
                .getService();

            // Define Resource
            BlobInfo blobInfo = BlobInfo.newBuilder(BlobId.of(bucketName + "/" + folderName, objectName)).build();

            // Generate Signed URL
            Map<String, String> extensionHeaders = new HashMap<>();
            extensionHeaders.put("Content-Type", getContentType(objectName));

            URL url =
                storage.signUrl(
                    blobInfo, 15,
                    TimeUnit.MINUTES,
                    Storage.SignUrlOption.httpMethod(HttpMethod.PUT),
                    Storage.SignUrlOption.withExtHeaders(extensionHeaders),
                    Storage.SignUrlOption.withV4Signature());
            return url.toString();
        } catch (Exception e) {
            throw new GcsException(ErrorCode.SIGNED_URL_GENERATION_ERROR);
        }
    }

    private String getContentType(String objectName) {
        String contentType;
        // 파일 이름에서 확장자 추출
        int lastDotIndex = objectName.lastIndexOf('.');
        if (lastDotIndex != -1) {
            String extension = objectName.substring(lastDotIndex + 1).toLowerCase();
            contentType = switch (extension) {
                case "pdf" -> "application/pdf";
                case "jpg", "jpeg" -> "image/jpeg";
                case "png" -> "image/png";
                default -> "application/octet-stream"; // 기본값
            };
        } else {
            contentType = "application/octet-stream"; // 확장자가 없는 경우 기본값
        }
        return contentType;
    }
}
