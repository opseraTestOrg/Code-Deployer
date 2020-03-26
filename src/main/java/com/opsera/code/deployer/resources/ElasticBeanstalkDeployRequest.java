package com.opsera.code.deployer.resources;

import java.io.Serializable;

public class ElasticBeanstalkDeployRequest implements Serializable {

    private String awsAccessKeyId;
    private String awsSecretAccessKey;
    private String applicationName;
    private String applicationVersionLabel;
    private String s3Bucket;
    private String s3Key;

    public String getAwsAccessKeyId() {
        return awsAccessKeyId;
    }

    public void setAwsAccessKeyId(String awsAccessKeyId) {
        this.awsAccessKeyId = awsAccessKeyId;
    }

    public String getAwsSecretAccessKey() {
        return awsSecretAccessKey;
    }

    public void setAwsSecretAccessKey(String awsSecretAccessKey) {
        this.awsSecretAccessKey = awsSecretAccessKey;
    }

    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    public String getApplicationVersionLabel() {
        return applicationVersionLabel;
    }

    public void setApplicationVersionLabel(String applicationVersionLabel) {
        this.applicationVersionLabel = applicationVersionLabel;
    }

    public String getS3Bucket() {
        return s3Bucket;
    }

    public void setS3Bucket(String s3Bucket) {
        this.s3Bucket = s3Bucket;
    }

    public String getS3Key() {
        return s3Key;
    }

    public void setS3Key(String s3Key) {
        this.s3Key = s3Key;
    }

}
