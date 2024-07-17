package hiff.hiff.behiff.global.common.s3;

//@Configuration
//public class S3Config {
//
////    @Value("${aws.bucket.end-point}")
//    private String endPoint;
//
////    @Value("${aws.bucket.region-name}")
//    private String regionName;
//
////    @Value("${aws.bucket.access-key")
//    private String accessKey;
//
////    @Value("${aws.bucket.secret-key")
//    private String secretKey;
//
////    @Value("${aws.bucket.name}")
//    private String bucketName;
//
//    @Bean
//    public AmazonS3 amazonS3() {
//        return AmazonS3ClientBuilder.standard()
//                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(endPoint, regionName))
//                .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(accessKey, secretKey)))
//                .build();
//    }
//}
