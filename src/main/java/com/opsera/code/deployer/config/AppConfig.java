package com.opsera.code.deployer.config;

import org.springframework.beans.factory.config.ServiceLocatorFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

@Component
@Configuration
public class AppConfig {

    @Bean
    public ServiceLocatorFactoryBean serviceLocatorFactoryBean() {
        ServiceLocatorFactoryBean factoryBean = new ServiceLocatorFactoryBean();
        factoryBean.setServiceLocatorInterface(IServiceFactory.class);
        return factoryBean;
    }

    @Bean
    public StopWatch stopWatch() {
        return new StopWatch();
    }

}
