/**
 * 
 */
package com.opsera.code.deployer.resources;

/**
 * @author sundar
 *
 */
public class CodeDeployerConstants {

	private CodeDeployerConstants() {
		// do nothing
	}

	public static final String TOOLS_CONFIG_URL = "/tools/configuration";
	public static final String UPLOAD_FILE = "/uploadFile";
	public static final String BASIC = "Basic ";
	public static final String VAULT_READ = "/read";
	public static final String BEANSTALK_DEPLOY = "/deploy";
	public static final String VAULT_SECRET_KEY = "%s-%s-secretkey";
	public static final String VAULT_SSH_KEY = "%s-%s-sshkey";
	public static final String SSH_DEPLOY = "/ssh/deploy";
	public static final String GET_ARTIFACT_DETAILS = "/v1.0/job/getArtifactDetails";
	public static final String SSH_FILE_UPLOAD = "SSH file upload";

}