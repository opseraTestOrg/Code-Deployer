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
    public static final String BEANSTALK_DEPLOY_STATUS = "/deploy/status";
    public static final String VAULT_SECRET_KEY = "%s-%s-secretkey";
    public static final String VAULT_SSH_KEY = "%s-%s-sshkey";
    public static final String SSH_DEPLOY = "/ssh/deploy";
    public static final String GET_ARTIFACT_DETAILS = "/v1.0/job/getArtifactDetails";
    public static final String SSH_FILE_UPLOAD = "SSH file upload";
    public static final String BUCKET_NAME = "%s/%s/artifacts";
    public static final String REGISTRY_DETAILS = "%s.dkr.ecr.%s.amazonaws.com/%s:%s";
    public static final String QUERY_PARM_TOOLID = "toolId";
    public static final String QUERY_CUSTOMER_TOOLID = "customerId";
    public static final String TOOL_REGISTRY_ENDPOINT = "/v2/registry/tool";
    public static final String IMAGE = "image";
    public static final String PORTS = "ports";
    public static final String ENVIRONMENT = "environment";
    public static final String VOLUMES = "volumes";
    public static final String ELASTIC_BEANSTALK_SUCCESSFULLY = "Elastic Beanstalk is created successfully.";
    public static final String ELASTIC_BEANSTALK_CREATING = "Elastic Beanstalk is creating environment.";
    public static final String GREEN = "Green";
    public static final String IN_PROGRESS = "in progress";
    public static final String DEPLOYMENT_STATUS = "Deployment is %s";
}
