/**
 * 
 */
package com.opsera.code.deployer.resources;

import java.io.Serializable;

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

    private String s3Key;

    private String accessKey;

    private String awsSecretKey;

    private String regions;

    private String s3Url;

    private String description;

    private Integer port;

    private String ec2KeyName;

    private String platform;

}
