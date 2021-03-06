package com.amt.dflipflop;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.amt.dflipflop.Constants.IS_PROD;


@Configuration
public class AmazonConfig {

    @Value("${aws.s3.key}")
    private String awsKey;

    @Value("${aws.s3.secret}")
    private String awsSecret;

    @Value("${aws.s3.region}")
    private String region;

    @Bean
    public AmazonS3 s3client() {
        if(IS_PROD){
            BasicAWSCredentials awsCredentials = new BasicAWSCredentials(awsKey, awsSecret);
            AmazonS3 amazonS3Client = AmazonS3ClientBuilder.standard()
                    .withRegion(Regions.fromName(region))
                    .withCredentials(new AWSStaticCredentialsProvider(awsCredentials))
                    .build();

            return amazonS3Client;
        }
        else{
            AmazonS3 amazonS3Client = AmazonS3ClientBuilder.standard()
                    .withRegion(Regions.fromName("eu-central-1"))
                    .build();

            return amazonS3Client;
        }

    }
}