package com.opsera.code.deployer.config;

import com.opsera.code.deployer.services.ElasticBeanstalkService;
import com.opsera.code.deployer.util.CodeDeployerUtil;

import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;
import org.springframework.web.client.RestTemplate;

@Component
public interface IServiceFactory {
    public ElasticBeanstalkService getElasticBeanstalkDeployService();
    public StopWatch stopWatch();
    public RestTemplate getRestTemplate();
    public CodeDeployerUtil getCodeDeployerUtil();
}
