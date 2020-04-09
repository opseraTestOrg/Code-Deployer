package com.opsera.code.deployer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class CodeDeployerApplication {

	public static void main(String[] args) {
		SpringApplication.run(CodeDeployerApplication.class, args);
	}
}