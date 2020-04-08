package com.opsera.code.deployer.controller;

import static com.opsera.code.deployer.resources.Constants.VAULT_SECRET_KEY;

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.opsera.code.deployer.config.IServiceFactory;
import com.opsera.code.deployer.resources.Configuration;
import com.opsera.code.deployer.resources.ElasticBeanstalkDeployRequest;
import com.opsera.code.deployer.resources.ElasticBeanstalkDeployResponse;
import com.opsera.code.deployer.resources.VaultData;
import com.opsera.code.deployer.resources.VaultRequest;
import com.opsera.code.deployer.services.ElasticBeanstalkService;
import com.opsera.code.deployer.util.CodeDeployerUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@Api("Opsera API for integration with AWS CodeDeploy")
public class DeployController {

	public static final Logger LOGGER = LoggerFactory.getLogger(DeployController.class);

	@Autowired
	private IServiceFactory serviceFactory;

	/**
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@PostMapping(path = "v1.0/deploy/ebs", consumes = "application/json", produces = "application/json")
	@ApiOperation("Deploys build artifacts from AWS S3 to Elastic Beanstalk.")
	public ResponseEntity<ElasticBeanstalkDeployResponse> elasticBeanstalkDeploy(@RequestBody ElasticBeanstalkDeployRequest request) throws Exception {

		long startTime = System.currentTimeMillis();
		LOGGER.info("Starting AWS Elastic Beanstalk deployment.");
		try {
			ElasticBeanstalkService ebsService = serviceFactory.getElasticBeanstalkDeployService();
			CodeDeployerUtil codeDeployerUtil = serviceFactory.getCodeDeployerUtil();
			Configuration configuration = codeDeployerUtil.getToolConfigurationDetails(request);
			VaultRequest vaultRequest = new VaultRequest();
			vaultRequest.setCustomerId(request.getCustomerId());
			String vaultSecretKey = String.format(VAULT_SECRET_KEY, request.getPipelineId(), request.getStepId());
			vaultRequest.setComponentKeys(Arrays.asList(vaultSecretKey));
			VaultData vaultData = codeDeployerUtil.readDataFromVault(vaultRequest);
			String secretkey = vaultData.getData().get(vaultSecretKey);
			configuration.setSecretKey(codeDeployerUtil.decodeString(secretkey));
			ebsService.deploy(configuration);
			ElasticBeanstalkDeployResponse response = new ElasticBeanstalkDeployResponse("DEPLOYED", "Elastic Beanstalk application deployed.");
			LOGGER.info("Finished deploying application {} to Elastic Beanstalk in {}.", configuration.getApplicationName(), System.currentTimeMillis() - startTime);
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (Exception e) {
			LOGGER.error("Failed deploying application time taken to execute {} secs", System.currentTimeMillis() - startTime, e);
			throw e;
		}
	}

	/**
	 * 
	 * @return
	 */
	@GetMapping("/status")
	@ApiOperation("To check the service status.")
	public String status() {
		return "Code deployer service running";
	}
}