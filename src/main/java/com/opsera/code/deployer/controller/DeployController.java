package com.opsera.code.deployer.controller;

import com.opsera.code.deployer.config.IServiceFactory;
import com.opsera.code.deployer.resources.ElasticBeanstalkDeployRequest;
import com.opsera.code.deployer.resources.ElasticBeanstalkDeployResponse;
import com.opsera.code.deployer.services.ElasticBeanstalkService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api("Opsera API for integration with AWS CodeDeploy")
public class DeployController {

    public static final Logger LOGGER = LoggerFactory.getLogger(DeployController.class);

    @Autowired
    private IServiceFactory serviceFactory;

    @GetMapping(path = "v1.0/deploy/ebs", consumes = "application/json", produces = "application/json")
    @ApiOperation("Deploys build artifacts from AWS S3 to Elastic Beanstalk.")
    public ResponseEntity<ElasticBeanstalkDeployResponse> elasticBeanstalkDeploy(@RequestBody ElasticBeanstalkDeployRequest request) {

        long startTime = System.currentTimeMillis();
        LOGGER.info("Starting AWS Elastic Beanstalk deployment.");

        try {

            if(request == null || StringUtils.isEmpty(request.getAwsAccessKeyId()) ||
                    StringUtils.isEmpty(request.getAwsSecretAccessKey()) ||
                    StringUtils.isEmpty(request.getApplicationName()) ||
                    StringUtils.isEmpty(request.getApplicationVersionLabel()) ||
                    StringUtils.isEmpty(request.getS3Bucket()) ||
                    StringUtils.isEmpty(request.getS3Key())) {
                throw new IllegalArgumentException("Deploy requires an aws access key, aws secret, application name, application version, s3 bucket, and s3 key values.");
            }

            ElasticBeanstalkService ebsService = serviceFactory.getElasticBeanstalkDeployService();
            ebsService.deploy(request.getAwsAccessKeyId(), request.getAwsSecretAccessKey(),
                    request.getApplicationName(), request.getApplicationVersionLabel(),
                    request.getS3Bucket(), request.getS3Key());

            ElasticBeanstalkDeployResponse response = new ElasticBeanstalkDeployResponse("DEPLOYED", "Elastic Beanstalk application deployed.");
            LOGGER.info("Finished deploying application {} to Elastic Beanstalk in {}.", request.getApplicationName(),
                    System.currentTimeMillis()-startTime);
            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (Exception e) {

            ElasticBeanstalkDeployResponse response = new ElasticBeanstalkDeployResponse("FAILED", e.getMessage());
            LOGGER.error("Failed triggering Job {} time taken to execute {} secs", request.getApplicationName(),
                    System.currentTimeMillis()-startTime, e);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);

        }

    }

    @GetMapping("/status")
    @ApiOperation("To check the service status.")
    public String status() {
        return "Code deployer service running";
    }
}
