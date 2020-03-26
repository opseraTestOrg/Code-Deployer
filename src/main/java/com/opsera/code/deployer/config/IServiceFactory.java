package com.opsera.code.deployer.config;

import com.opsera.code.deployer.services.ElasticBeanstalkService;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

@Component
public interface IServiceFactory {
    public ElasticBeanstalkService getElasticBeanstalkDeployService();
    public StopWatch stopWatch();
}
