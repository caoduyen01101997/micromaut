package com.example.config;

import io.micronaut.context.annotation.Factory;
import io.micronaut.context.annotation.Value;
import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.http.SdkHttpClient;
import software.amazon.awssdk.http.apache.ApacheHttpClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.CreateBucketRequest;
import software.amazon.awssdk.services.s3.model.HeadBucketRequest;
import software.amazon.awssdk.services.s3.model.NoSuchBucketException;

import java.net.URI;
import java.time.Duration;

@Factory
public class S3Config {

    private static final Logger LOG = LoggerFactory.getLogger(S3Config.class);

    @Value("${aws.services.s3.endpoint-override}")
    private String endpoint;

    @Value("${aws.services.s3.access-key}")
    private String accessKey;

    @Value("${aws.services.s3.secret-key}")
    private String secretKey;

    @Value("${micronaut.object-storage.aws.default.bucket:my-bucket}")
    private String bucketName;

    @Singleton
    public S3Client s3Client() {
        // Configure connection pool to prevent exhaustion
        SdkHttpClient httpClient = ApacheHttpClient.builder()
                .maxConnections(200)
                .connectionTimeout(Duration.ofSeconds(10))
                .socketTimeout(Duration.ofSeconds(30))
                .connectionAcquisitionTimeout(Duration.ofSeconds(10))
                .build();

        S3Client client = S3Client.builder()
                .httpClient(httpClient)
                .endpointOverride(URI.create(endpoint))
                .region(Region.US_EAST_1)
                .credentialsProvider(StaticCredentialsProvider.create(
                        AwsBasicCredentials.create(accessKey, secretKey)
                ))
                .forcePathStyle(true)
                .build();

        // Auto-create bucket if not exists
        try {
            client.headBucket(HeadBucketRequest.builder().bucket(bucketName).build());
            LOG.info("Bucket '{}' already exists", bucketName);
        } catch (NoSuchBucketException e) {
            client.createBucket(CreateBucketRequest.builder().bucket(bucketName).build());
            LOG.info("Bucket '{}' created successfully", bucketName);
        }

        return client;
    }
}
