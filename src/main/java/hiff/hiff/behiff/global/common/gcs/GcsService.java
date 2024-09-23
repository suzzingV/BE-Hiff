package hiff.hiff.behiff.global.common.gcs;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import hiff.hiff.behiff.global.common.gcs.exception.GcsException;
import hiff.hiff.behiff.global.response.properties.ErrorCode;
import java.io.InputStream;
import java.util.UUID;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

@Component
@Transactional
public class GcsService {

    @Value("${spring.cloud.gcp.storage.credentials.location}")
    private String keyFileName;

    @Value("${spring.cloud.gcp.storage.bucket}")
    private String bucketName;

    private static final String STORAGE_URL = "https://storage.googleapis.com/";

    public String saveImage(MultipartFile image, String folderName) {
        String objectName = UUID.randomUUID().toString();
        String imgUrl = STORAGE_URL + bucketName + "/" + folderName + "/" + objectName;

        try {
            InputStream keyFile = ResourceUtils.getURL(keyFileName).openStream();
            String ext = image.getContentType();
            Storage storage = StorageOptions.newBuilder()
                .setCredentials(GoogleCredentials.fromStream(keyFile))
                .build()
                .getService();

            if (image.isEmpty()) {
                imgUrl = null;
            } else {
                BlobInfo blobInfo = BlobInfo.newBuilder(bucketName, folderName + "/" + objectName)
                    .setContentType(ext).build();

                storage.create(blobInfo, image.getInputStream());
            }
        } catch (Exception e) {
            throw new GcsException(ErrorCode.IMAGE_STORAGE_SAVE_ERROR);
        }

        return imgUrl;
    }

    public void deleteImage(String imgUrl, String folderName) {
        if(imgUrl.isEmpty()) {
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
            throw new GcsException(ErrorCode.IMAGE_STORAGE_SAVE_ERROR);
        }
    }

    private String getObjectNameFromUrl(String imgUrl, String folder) {
        int folderIndex = imgUrl.indexOf(folder);
        return imgUrl.substring(folderIndex);
    }

}
