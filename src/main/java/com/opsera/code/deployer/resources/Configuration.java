/**
 * 
 */
package com.opsera.code.deployer.resources;

import java.io.Serializable;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Purusothaman
 *
 */
@Getter
@Setter
public class Configuration extends SSHDetails implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 6071885561543648608L;

    private String applicationName;
    private String applicationVersionLabel;
    private String bucketName;
    private String bucketAccess;
    private String s3Key;
    private String regions;
    private String s3Url;
    private String description;
    private Integer port;
    private String ec2KeyName;
    private String platform;
    private String s3StepId;
    private String awsToolConfigId;
    private String customerId;
    private String ecrPushStepId;
    private String s3ECRStepId;
    private String buildStepId;
    private Map<String, String> environments;
    private Map<String, String> dockerVolumePath;
    private String fileName;
    private byte[] fileData;
    private String hostedZoneId;
    private String domainName;
    private String dockerName;
    private String dockerTagName;
    private SecretKey awsAccountId;
    private String pipelineId;
    private String ecrRepoName;


}
