package com.opsera.code.deployer.services;

import static com.opsera.code.deployer.resources.CodeDeployerConstants.BEANSTALK_DEPLOY;
import static com.opsera.code.deployer.resources.CodeDeployerConstants.BEANSTALK_DEPLOY_STATUS;
import static com.opsera.code.deployer.resources.CodeDeployerConstants.BUCKET_NAME;
import static com.opsera.code.deployer.resources.CodeDeployerConstants.DEPLOYMENT_STATUS;
import static com.opsera.code.deployer.resources.CodeDeployerConstants.ENVIRONMENT;
import static com.opsera.code.deployer.resources.CodeDeployerConstants.IMAGE;
import static com.opsera.code.deployer.resources.CodeDeployerConstants.PORTS;
import static com.opsera.code.deployer.resources.CodeDeployerConstants.REGISTRY_DETAILS;
import static com.opsera.code.deployer.resources.CodeDeployerConstants.UPLOAD_FILE;
import static com.opsera.code.deployer.resources.CodeDeployerConstants.VOLUMES;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.opsera.code.deployer.config.AppConfig;
import com.opsera.code.deployer.config.IServiceFactory;
import com.opsera.code.deployer.exceptions.GeneralElasticBeanstalkException;
import com.opsera.code.deployer.exceptions.InvalidDataException;
import com.opsera.code.deployer.resources.Configuration;
import com.opsera.code.deployer.resources.DeploymentStatus;
import com.opsera.code.deployer.resources.DockerComposer;
import com.opsera.code.deployer.resources.ElasticBeanstalkDeployRequest;
import com.opsera.code.deployer.resources.ElasticBeanstalkDeployResponse;
import com.opsera.code.deployer.resources.ToolDetails;
import com.opsera.code.deployer.resources.VaultData;
import com.opsera.code.deployer.resources.VaultRequest;
import com.opsera.code.deployer.util.CodeDeployerUtil;

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
     * For deploying to elastic beanstalk
     * 
     * 
     * @param configuration
     * @throws GeneralElasticBeanstalkException
     */
    public String deploy(ElasticBeanstalkDeployRequest request) throws GeneralElasticBeanstalkException {
        LOGGER.debug("Deploying customer {} to Elastic Beanstalk.", request.getCustomerId());
        try {
            CodeDeployerUtil codeDeployerUtil = serviceFactory.getCodeDeployerUtil();
            Configuration configuration = codeDeployerUtil.getToolConfigurationDetails(request);
            configuration.setCustomerId(request.getCustomerId());
            ElasticBeanstalkDeployRequest s3ECRRequest = new ElasticBeanstalkDeployRequest();
            s3ECRRequest.setPipelineId(request.getPipelineId());
            s3ECRRequest.setStepId(configuration.getS3ECRStepId());
            s3ECRRequest.setCustomerId(request.getCustomerId());
            Configuration s3ECRConfiguration = codeDeployerUtil.getToolConfigurationDetails(s3ECRRequest);
            configuration.setPipelineId(request.getPipelineId());
            configuration.setStepId(request.getStepId());
            if (s3ECRConfiguration.getJobType().equalsIgnoreCase("SEND S3")) {
                configuration.setS3Url(s3ECRConfiguration.getS3Url());
            } else {
                String bucketName = configuration.getBucketName();
                String s3Url = createAndUploadDockerComposer(request, s3ECRConfiguration, configuration);
                configuration.setS3Url(s3Url);
                configuration.setBucketName(bucketName);
            }

            String url = deployToBeantalk(configuration);
            LOGGER.debug("Completed deploying to application {} with source bundle {}.", configuration.getApplicationName(), configuration.getBucketName());
            return url;
        } catch (Exception e) {
            LOGGER.error("Error occurred while deploying to customer {} with pipelineid {} .", request.getCustomerId(), request.getPipelineId(), e);
            throw new GeneralElasticBeanstalkException("Getting error while deploying the application");
        }
    }

    /**
     * For deploying to elastic beanstalk
     * 
     * 
     * @param configuration
     * @throws GeneralElasticBeanstalkException
     */
    public ElasticBeanstalkDeployResponse ebsStatus(ElasticBeanstalkDeployRequest request) throws GeneralElasticBeanstalkException {
        LOGGER.debug("Ebs Application status customer {} to Elastic Beanstalk.", request.getCustomerId());
        try {
            CodeDeployerUtil codeDeployerUtil = serviceFactory.getCodeDeployerUtil();
            Configuration configuration = codeDeployerUtil.getToolConfigurationDetails(request);
            configuration.setCustomerId(request.getCustomerId());
            String status = statusofBeantalkApplication(configuration);
            String message = String.format(DEPLOYMENT_STATUS, DeploymentStatus.valueOf(status.toUpperCase()).getName());
            String url = DeploymentStatus.READY.name().equalsIgnoreCase(status.toUpperCase()) ? String.format("http://%s", configuration.getDomainName()) : "";
            status = DeploymentStatus.READY.name().equalsIgnoreCase(status.toUpperCase()) ? "Deployed" : status;
            ElasticBeanstalkDeployResponse response = new ElasticBeanstalkDeployResponse(status, message, url);
            LOGGER.debug("Completed deploying status to application {} with source bundle {}.", configuration.getApplicationName(), configuration.getBucketName());
            return response;
        } catch (Exception e) {
            LOGGER.error("Error occurred while deploying status to customer {} with pipelineid {} .", request.getCustomerId(), request.getPipelineId(), e);
            throw new GeneralElasticBeanstalkException("Getting error while deployment status the application", e);
        }
    }

    /**
     * 
     * Deploys an application to elastic bean stalk
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

    private String createAndUploadDockerComposer(ElasticBeanstalkDeployRequest request, Configuration ecrPushConfiguration, Configuration config) throws JsonProcessingException, InvalidDataException {
        ElasticBeanstalkDeployRequest buildStepRequest = new ElasticBeanstalkDeployRequest();
        buildStepRequest.setCustomerId(request.getCustomerId());
        buildStepRequest.setPipelineId(request.getPipelineId());
        buildStepRequest.setStepId(ecrPushConfiguration.getBuildStepId());
        Configuration buildConfiguration = serviceFactory.getCodeDeployerUtil().getToolConfigurationDetails(buildStepRequest);
        ToolDetails toolDetails = serviceFactory.getCodeDeployerUtil().getToolDetails(config.getAwsToolConfigId(), request.getCustomerId());
        Configuration awsConfig = toolDetails.getConfiguration();
        String awsAccountId = awsConfig.getAwsAccountId().getVaultKey();
        VaultRequest vaultRequest = VaultRequest.builder().customerId(toolDetails.getOwner()).componentKeys(Arrays.asList(awsAccountId)).build();
        VaultData vaultData = serviceFactory.getCodeDeployerUtil().readDataFromVault(vaultRequest);
        awsAccountId = serviceFactory.getCodeDeployerUtil().decodeString(vaultData.getData().get(awsAccountId));
        String ecrImage = String.format(REGISTRY_DETAILS, awsAccountId, awsConfig.getRegions(),
                StringUtils.isEmpty(ecrPushConfiguration.getEcrRepoName()) ? buildConfiguration.getDockerName() : ecrPushConfiguration.getEcrRepoName(), buildConfiguration.getDockerTagName());
        DockerComposer dockerComposer = new DockerComposer();
        dockerComposer.setVersion("2.4");
        Map<String, Map<String, Object>> services = new LinkedHashMap<>();
        Map<String, Object> subServices = new LinkedHashMap<>();
        subServices.put(IMAGE, ecrImage);
        subServices.put(PORTS, Arrays.asList(String.format("%s:%s", config.getPort(), config.getPort())));
        if (!CollectionUtils.isEmpty(config.getEnvironments())) {
            subServices.put(ENVIRONMENT, config.getEnvironments());
        }
        if (!CollectionUtils.isEmpty(config.getDockerVolumePath())) {
            List<String> volumes = new ArrayList<>();
            config.getDockerVolumePath().forEach((id, name) -> volumes.add(String.format("%s:%s", id, name)));
            subServices.put(VOLUMES, volumes);
        }
        services.put(buildConfiguration.getDockerName(), subServices);
        dockerComposer.setServices(services);
        ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory());
        String dockerComposerFile = objectMapper.writeValueAsString(dockerComposer);
        config.setBucketName(String.format(BUCKET_NAME, config.getBucketName(), config.getApplicationName()));
        config.setBucketName(config.getBucketName());
        config.setFileName(String.format("%s.yml", request.getPipelineId()));
        LOGGER.debug("Artifact details is BucketName:{} and fileName :{}", config.getBucketName(), config.getFileName());
        config.setFileData(dockerComposerFile.getBytes());
        return uploadFileToS3(config);

    }

    /**
     * 
     * Uploads file to S3
     * 
     * @param config
     * @return
     * @throws Exception
     * @throws S3Exception
     */
    private String uploadFileToS3(Configuration config) throws InvalidDataException {
        LOGGER.debug("Started to connect with aws service to upload the file");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        RestTemplate restTemplate = serviceFactory.getRestTemplate();
        HttpEntity<Configuration> requestEntity = new HttpEntity<>(config, headers);
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(appConfig.getAwsServiceBaseUrl()).path(UPLOAD_FILE);
        ResponseEntity<String> response = restTemplate.exchange(uriBuilder.toUriString(), HttpMethod.POST, requestEntity, String.class);
        if (response.getStatusCode() == HttpStatus.OK) {
            LOGGER.debug("Completed to connect with aws service to upload the file");
            return response.getBody();
        }
        LOGGER.debug("Getting Error in while File uploading ");
        throw new InvalidDataException("Getting Error in while File uploading ");
    }

    /**
     * TO getting the status of beanstalk
     * 
     * @param configuration
     * @return
     * @throws GeneralElasticBeanstalkException
     */
    private String statusofBeantalkApplication(Configuration configuration) throws GeneralElasticBeanstalkException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        RestTemplate restTemplate = serviceFactory.getRestTemplate();
        HttpEntity<Configuration> requestEntity = new HttpEntity<>(configuration, headers);
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(appConfig.getAwsServiceBaseUrl()).path(BEANSTALK_DEPLOY_STATUS);
        ResponseEntity<String> response = restTemplate.exchange(uriBuilder.toUriString(), HttpMethod.POST, requestEntity, String.class);
        if (response.getStatusCode() == HttpStatus.OK) {
            return response.getBody();
        }
        throw new GeneralElasticBeanstalkException("Failed to getting the deployment status in Beanstalk");
    }
}
