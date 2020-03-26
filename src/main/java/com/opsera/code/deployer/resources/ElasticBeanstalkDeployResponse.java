package com.opsera.code.deployer.resources;

public class ElasticBeanstalkDeployResponse {

    private String status;
    private String message;

    public ElasticBeanstalkDeployResponse(String status, String message) {
        this.status = status;
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
