package com.opsera.code.deployer.resources;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@AllArgsConstructor
public class ElasticBeanstalkDeployResponse {

    private String status;
    private String message;
    private String url;

}
