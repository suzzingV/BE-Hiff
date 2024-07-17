package hiff.hiff.behiff.global.common.s3;

//@Component
//@RequiredArgsConstructor
//@Transactional
//public class S3Service {
//
//    private final AmazonS3 s3;
//
//    @Value("${aws.bucket.end-point}")
//    private String endPoint;
//
//    @Value("${aws.bucket.name}")
//    private String bucketName;
//
//    public List<String> savePhotos(List<MultipartFile> photos) {
//        List<String> photoUrls = new ArrayList<>();
//        for (MultipartFile photo : photos) {
//            ObjectMetadata objectMetadata = new ObjectMetadata();
//            objectMetadata.setContentLength(photo.getSize());
//            objectMetadata.setContentType(photo.getContentType());
//            String photoName = UUID.randomUUID().toString(); // 고유한 파일 이름 부여
//            try {
//                PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, photoName, photo.getInputStream(), objectMetadata)
//                        .withCannedAcl(CannedAccessControlList.PublicRead); // url로 접근할 수 있도록 파일 공개 설정
//                s3.putObject(putObjectRequest);
//            } catch (SdkClientException | IOException e) {
//                throw new S3Exception(ErrorCode.S3_ACCESS_DENIED, e.getMessage());
//            }
//
//            photoUrls.add(endPoint + "/" + photoName);
//        }
//
//        return photoUrls;
//    }
//
//
//}
