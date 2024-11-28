package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

import org.springframework.beans.factory.annotation.Value;

@Configuration
public class s3config {

    @Value("${cloud.aws.credentials.access-key}")
    private String accessKeyId;
    @Value("${cloud.aws.credentials.secret-key}")
    private String secretkey;

    @Bean
    public S3Client s3client() {
        Region region = Region.US_EAST_1;
        AwsCredentials credentials = AwsBasicCredentials.create(accessKeyId, secretkey);
        S3Client s3client = S3Client.builder().region(region)
                .credentialsProvider(StaticCredentialsProvider.create(credentials)).build();
        return s3client;
    }

}
