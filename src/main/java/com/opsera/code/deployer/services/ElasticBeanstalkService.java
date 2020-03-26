package com.opsera.code.deployer.services;

import com.opsera.code.deployer.exceptions.GeneralElasticBeanstalkException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.services.elasticbeanstalk.ElasticBeanstalkClient;
import software.amazon.awssdk.services.elasticbeanstalk.model.*;

@Component
public class ElasticBeanstalkService {

    public static final Logger LOGGER = LoggerFactory.getLogger(ElasticBeanstalkService.class);

    public void deploy (String awsAccessKeyId, String awsSecretAccessKey, String applicationName, String applicationVersionLabel, String s3Bucket, String s3Key) throws GeneralElasticBeanstalkException {

        LOGGER.debug("Deploying application {} to Elastic Beanstalk.", applicationName);

        try {
            AwsCredentials awsCredentials = AwsBasicCredentials
                    .create(awsAccessKeyId, awsSecretAccessKey);

            ElasticBeanstalkClient ebsClient = ElasticBeanstalkClient.builder()
                    .credentialsProvider(StaticCredentialsProvider.create(awsCredentials))
                    .build();

            S3Location sourceBundle = S3Location.builder()
                    .s3Bucket(s3Bucket)
                    .s3Key(s3Key)
                    .build();

            CreateApplicationVersionResponse response = ebsClient.createApplicationVersion(CreateApplicationVersionRequest.builder()
                    .applicationName(applicationName)
                    .sourceBundle(sourceBundle)
                    .versionLabel(applicationVersionLabel)
                    .build());

            LOGGER.debug("Completed deploying to application {} with source bundle {}.", applicationName, s3Bucket);

        } catch (Exception e) {
            LOGGER.error(String.format("Error occurred while deploying to application %s with source bundle %s.", applicationName, s3Bucket), e);
            throw new GeneralElasticBeanstalkException();
        }

    }

    public static void main(String[] args) {
        ElasticBeanstalkService ebsService = new ElasticBeanstalkService();
        try {
            ebsService.deploy("awsAccessKeyId",
                    "awsSecretAccessKey",
                    "test-app",
                    "v2",
                    "opsera-pipeline-artifacts",
                    "jobs/Send to S3/5/build/libs/jenkins-integrator-1.1.jar");
        } catch (GeneralElasticBeanstalkException e) {
            e.printStackTrace();
        }
    }

}
