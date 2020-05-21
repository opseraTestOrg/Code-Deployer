/**
 * 
 */
package com.opsera.code.deployer.services;

import static com.opsera.code.deployer.resources.CodeDeployerConstants.GET_ARTIFACT_DETAILS;
import static com.opsera.code.deployer.resources.CodeDeployerConstants.SSH_DEPLOY;
import static com.opsera.code.deployer.resources.CodeDeployerConstants.SSH_FILE_UPLOAD;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.opsera.code.deployer.config.AppConfig;
import com.opsera.code.deployer.config.IServiceFactory;
import com.opsera.code.deployer.exceptions.GeneralElasticBeanstalkException;
import com.opsera.code.deployer.exceptions.InvalidDataException;
import com.opsera.code.deployer.resources.Configuration;
import com.opsera.code.deployer.resources.ElasticBeanstalkDeployRequest;
import com.opsera.code.deployer.resources.SSHDetails;
import com.opsera.code.deployer.resources.SSHDetailsRequest;
import com.opsera.code.deployer.resources.VaultData;
import com.opsera.code.deployer.resources.VaultRequest;
import com.opsera.code.deployer.util.CodeDeployerUtil;

/**
 * @author Purusothaman
 *
 */
@Service
public class SSHService {
    public static final Logger LOGGER = LoggerFactory.getLogger(SSHService.class);

    private IServiceFactory serviceFactory;

    private AppConfig appConfig;

    /**
     * @param serviceFactory
     * @param appConfig
     */
    @Autowired
    public SSHService(IServiceFactory serviceFactory, AppConfig appConfig) {
        this.serviceFactory = serviceFactory;
        this.appConfig = appConfig;
    }

    /**
     * This method used to deploy the application through ssh
     * 
     * @param request
     * @return
     * @throws InvalidDataException
     * @throws GeneralElasticBeanstalkException
     */
    public String sshDeploy(ElasticBeanstalkDeployRequest request) throws GeneralElasticBeanstalkException {
        try {
            LOGGER.debug("Started to deploying the application through ssh");
            CodeDeployerUtil codeDeployerUtil = serviceFactory.getCodeDeployerUtil();
            Configuration configuration = codeDeployerUtil.getToolConfigurationDetails(request);

            VaultRequest vaultRequest = new VaultRequest();
            vaultRequest.setCustomerId(request.getCustomerId());

            vaultRequest.setComponentKeys(Arrays.asList(configuration.getSshKey().getVaultKey()));

            VaultData vaultData = codeDeployerUtil.readDataFromVault(vaultRequest);
            String sshKey = vaultData.getData().get(configuration.getSshKey().getVaultKey());

            SSHDetailsRequest sshDetailsRequest = getSshDetailsRequest(configuration, sshKey);

            if (SSH_FILE_UPLOAD.equalsIgnoreCase(configuration.getSshAction())) {
                getArtifactDetails(request, sshDetailsRequest);
            }
            String executionResult = deployToSshExecution(sshDetailsRequest);
            LOGGER.debug("Completed deploying the application through ssh{}.", configuration.getServerIp());

            return executionResult;
        } catch (IOException e) {
            throw new GeneralElasticBeanstalkException("Error occurred while deploying to application ", e);
        }
    }

    /**
     * returns SSH details request
     * 
     * @param configuration
     * @param sshkey
     * @return
     */
    private SSHDetailsRequest getSshDetailsRequest(Configuration configuration, String sshkey) {
        CodeDeployerUtil codeDeployerUtil = serviceFactory.getCodeDeployerUtil();
        SSHDetailsRequest sshDetails = new SSHDetailsRequest();
        sshDetails.setUserName(configuration.getUserName());
        sshDetails.setServerIp(configuration.getServerIp());
        sshDetails.setSshAuthKeyName(configuration.getSshKey().getFileName());
        sshDetails.setServerPath(configuration.getServerPath());
        sshDetails.setCommands(configuration.getCommands());
        sshDetails.setSshAuthKey(codeDeployerUtil.decodeString(sshkey));
        sshDetails.setSshAction(configuration.getSshAction());
        return sshDetails;
    }

    /**
     * 
     * @param config
     * @return
     * @throws GeneralElasticBeanstalkException
     *
     */
    private String deployToSshExecution(SSHDetails sshDetails) throws GeneralElasticBeanstalkException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        RestTemplate restTemplate = serviceFactory.getRestTemplate();
        HttpEntity<SSHDetails> requestEntity = new HttpEntity<>(sshDetails, headers);
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(appConfig.getAwsServiceBaseUrl()).path(SSH_DEPLOY);
        ResponseEntity<String> response = restTemplate.exchange(uriBuilder.toUriString(), HttpMethod.POST, requestEntity, String.class);
        if (response.getStatusCode() == HttpStatus.OK) {
            return response.getBody();
        }
        throw new GeneralElasticBeanstalkException("Failed to deploy in Beanstalk");
    }

    /**
     * 
     * @param config
     * @return
     * @throws FileNotFoundException
     * @throws GeneralElasticBeanstalkException
     * @throws IOException
     *
     */
    private SSHDetailsRequest getArtifactDetails(ElasticBeanstalkDeployRequest request, SSHDetailsRequest sshDetailsRequest) throws IOException {
        RestTemplate restTemplate = serviceFactory.getRestTemplate();
        restTemplate.getMessageConverters().add(new ByteArrayHttpMessageConverter());

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_OCTET_STREAM));

        HttpEntity<ElasticBeanstalkDeployRequest> entity = new HttpEntity<>(request, headers);
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(appConfig.getJenkinsIntegratorBaseUrl()).path(GET_ARTIFACT_DETAILS);

        ResponseEntity<byte[]> response = restTemplate.exchange(uriBuilder.toUriString(), HttpMethod.POST, entity, byte[].class);

        if (response.getStatusCode() == HttpStatus.OK) {
            ContentDisposition contentDisposition = response.getHeaders().getContentDisposition();
            String fileName = contentDisposition.getFilename();
            String filePrefix = fileName.substring(0, fileName.lastIndexOf('.'));
            String fileSuffix = fileName.substring(fileName.lastIndexOf('.'), fileName.length());
            File file = File.createTempFile(filePrefix, fileSuffix);
            try (OutputStream out = new FileOutputStream(file)) {
                out.write(response.getBody());
            }
            sshDetailsRequest.setFile(file);
            sshDetailsRequest.setFileName(fileName);
        }
        return sshDetailsRequest;
    }
}
