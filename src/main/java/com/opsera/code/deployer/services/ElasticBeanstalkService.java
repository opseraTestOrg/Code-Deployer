package com.opsera.code.deployer.services;

import static com.opsera.code.deployer.resources.CodeDeployerConstants.BEANSTALK_DEPLOY;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.opsera.code.deployer.config.AppConfig;
import com.opsera.code.deployer.config.IServiceFactory;
import com.opsera.code.deployer.exceptions.GeneralElasticBeanstalkException;
import com.opsera.code.deployer.resources.Configuration;

@Component
public class ElasticBeanstalkService {

	public static final Logger LOGGER = LoggerFactory.getLogger(ElasticBeanstalkService.class);

	private IServiceFactory serviceFactory;

	private AppConfig appConfig;

	/**
	 * @param serviceFactory
	 * @param appConfig
	 */
	@Autowired
	public ElasticBeanstalkService(IServiceFactory serviceFactory, AppConfig appConfig) {
		this.serviceFactory = serviceFactory;
		this.appConfig = appConfig;
	}

	/**
	 * 
	 * @param configuration
	 * @throws GeneralElasticBeanstalkException
	 */
	public void deploy(Configuration configuration) throws GeneralElasticBeanstalkException {
		LOGGER.debug("Deploying application {} to Elastic Beanstalk.", configuration.getApplicationName());
		try {
			deployToBeantalk(configuration);
			LOGGER.debug("Completed deploying to application {} with source bundle {}.", configuration.getApplicationName(), configuration.getBucketName());
		} catch (Exception e) {
			LOGGER.error("Error occurred while deploying to application {} with source bundle {} .", configuration.getApplicationName(), configuration.getBucketName(), e);
			throw new GeneralElasticBeanstalkException("Getting error while deploying the application");
		}
	}

	/**
	 * 
	 * @param config
	 * @return
	 * @throws GeneralElasticBeanstalkException
	 *
	 */
	private String deployToBeantalk(Configuration config) throws GeneralElasticBeanstalkException {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		RestTemplate restTemplate = serviceFactory.getRestTemplate();
		HttpEntity<Configuration> requestEntity = new HttpEntity<>(config, headers);
			UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(appConfig.getAwsServiceBaseUrl()).path(BEANSTALK_DEPLOY);
			ResponseEntity<String> response = restTemplate.exchange(uriBuilder.toUriString(), HttpMethod.POST, requestEntity, String.class);
			if (response.getStatusCode() == HttpStatus.OK) {
				return response.getBody();
			}
			throw new GeneralElasticBeanstalkException("Failed to deploy in Beanstalk");
	}
}