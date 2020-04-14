/**
 * 
 */
package com.opsera.code.deployer.resources;

import java.io.Serializable;

/**
 * @author Purusothaman
 *
 */
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
	private String secretKey;
	private String regions;
	private String s3Url;
	private String description;
	private Integer port;
	private String ec2KeyName;
	private String platform;


	/**
	 * @return the applicationName
	 */
	public String getApplicationName() {
		return applicationName;
	}

	/**
	 * @param applicationName the applicationName to set
	 */
	public void setApplicationName(String applicationName) {
		this.applicationName = applicationName;
	}

	/**
	 * @return the applicationVersionLabel
	 */
	public String getApplicationVersionLabel() {
		return applicationVersionLabel;
	}

	/**
	 * @param applicationVersionLabel the applicationVersionLabel to set
	 */
	public void setApplicationVersionLabel(String applicationVersionLabel) {
		this.applicationVersionLabel = applicationVersionLabel;
	}

	/**
	 * @return the bucketName
	 */
	public String getBucketName() {
		return bucketName;
	}

	/**
	 * @param bucketName the bucketName to set
	 */
	public void setBucketName(String bucketName) {
		this.bucketName = bucketName;
	}

	/**
	 * @return the s3Key
	 */
	public String getS3Key() {
		return s3Key;
	}

	/**
	 * @param s3Key the s3Key to set
	 */
	public void setS3Key(String s3Key) {
		this.s3Key = s3Key;
	}

	/**
	 * @return the accessKey
	 */
	public String getAccessKey() {
		return accessKey;
	}

	/**
	 * @param accessKey the accessKey to set
	 */
	public void setAccessKey(String accessKey) {
		this.accessKey = accessKey;
	}

	/**
	 * @return the secretKey
	 */
	public String getSecretKey() {
		return secretKey;
	}

	/**
	 * @param secretKey the secretKey to set
	 */
	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}

	/**
	 * @return the regions
	 */
	public String getRegions() {
		return regions;
	}

	/**
	 * @param regions the regions to set
	 */
	public void setRegions(String regions) {
		this.regions = regions;
	}

	/**
	 * @return the s3Url
	 */
	public String getS3Url() {
		return s3Url;
	}

	/**
	 * @param s3Url the s3Url to set
	 */
	public void setS3Url(String s3Url) {
		this.s3Url = s3Url;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the port
	 */
	public Integer getPort() {
		return port;
	}

	/**
	 * @param port the port to set
	 */
	public void setPort(Integer port) {
		this.port = port;
	}

	/**
	 * @return the ec2KeyName
	 */
	public String getEc2KeyName() {
		return ec2KeyName;
	}

	/**
	 * @param ec2KeyName the ec2KeyName to set
	 */
	public void setEc2KeyName(String ec2KeyName) {
		this.ec2KeyName = ec2KeyName;
	}

	/**
	 * @return the platform
	 */
	public String getPlatform() {
		return platform;
	}

	/**
	 * @param platform the platform to set
	 */
	public void setPlatform(String platform) {
		this.platform = platform;
	}
}
