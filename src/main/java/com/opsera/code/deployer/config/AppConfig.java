package com.opsera.code.deployer.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ServiceLocatorFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;
import org.springframework.web.client.RestTemplate;

@Component
@Configuration
public class AppConfig {

	@Value("${pipeline.config.baseurl}")
	private String pipelineConfigBaseUrl;

	@Value("${aws.service.baseurl}")
	private String awsServiceBaseUrl;

	@Value("${vault.service.baseurl}")
	private String vaultBaseUrl;

	/**
	 * 
	 * @return
	 */
	@Bean
	public ServiceLocatorFactoryBean serviceLocatorFactoryBean() {
		ServiceLocatorFactoryBean factoryBean = new ServiceLocatorFactoryBean();
		factoryBean.setServiceLocatorInterface(IServiceFactory.class);
		return factoryBean;
	}

	/**
	 * 
	 * @return
	 */
	@Bean()
	public StopWatch stopWatch() {
		return new StopWatch();
	}

	/**
	 * 
	 * @return
	 */
	@Bean
	public RestTemplate getRestTemplate() {
		return new RestTemplate();
	}

	/**
	 * @return the pipelineConfigBaseUrl
	 */
	public String getPipelineConfigBaseUrl() {
		return pipelineConfigBaseUrl;
	}

	/**
	 * @return the awsServiceBaseUrl
	 */
	public String getAwsServiceBaseUrl() {
		return awsServiceBaseUrl;
	}

	/**
	 * @return the vaultBaseUrl
	 */
	public String getVaultBaseUrl() {
		return vaultBaseUrl;
	}
}