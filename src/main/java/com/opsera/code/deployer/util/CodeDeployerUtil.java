/**
 * 
 */
package com.opsera.code.deployer.util;

import static com.opsera.code.deployer.resources.CodeDeployerConstants.QUERY_CUSTOMER_TOOLID;
import static com.opsera.code.deployer.resources.CodeDeployerConstants.QUERY_PARM_TOOLID;
import static com.opsera.code.deployer.resources.CodeDeployerConstants.TOOLS_CONFIG_URL;
import static com.opsera.code.deployer.resources.CodeDeployerConstants.TOOL_REGISTRY_ENDPOINT;
import static com.opsera.code.deployer.resources.CodeDeployerConstants.VAULT_READ;

import java.util.Base64;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.opsera.code.deployer.config.AppConfig;
import com.opsera.code.deployer.config.IServiceFactory;
import com.opsera.code.deployer.resources.Configuration;
import com.opsera.code.deployer.resources.ElasticBeanstalkDeployRequest;
import com.opsera.code.deployer.resources.ToolDetails;
import com.opsera.code.deployer.resources.VaultData;
import com.opsera.code.deployer.resources.VaultRequest;

/**
 * @author sundar
 *
 */
@Component
public class CodeDeployerUtil {

    public static final Logger LOGGER = LoggerFactory.getLogger(CodeDeployerUtil.class);

    private IServiceFactory factory;

    private AppConfig appConfig;

    /**
     * @param factory
     * @param appConfig
     */
    @Autowired
    public CodeDeployerUtil(IServiceFactory factory, AppConfig appConfig) {
        super();
        this.factory = factory;
        this.appConfig = appConfig;
    }

    /**
     * 
     * @param vaultRequest
     * @return
     * 
     */
    public VaultData readDataFromVault(VaultRequest vaultRequest) {
        RestTemplate restTemplate = factory.getRestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<VaultRequest> requestEntity = new HttpEntity<>(vaultRequest, headers);
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(appConfig.getVaultBaseUrl()).path(VAULT_READ);
        ResponseEntity<VaultData> response = restTemplate.exchange(uriBuilder.toUriString(), HttpMethod.POST, requestEntity, VaultData.class);
        return response.getBody();
    }

    /**
     * 
     * @param request
     * @return
     */
    public Configuration getToolConfigurationDetails(ElasticBeanstalkDeployRequest request) {
        RestTemplate restTemplate = factory.getRestTemplate();
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(appConfig.getPipelineConfigBaseUrl()).path(TOOLS_CONFIG_URL);
        return restTemplate.postForObject(uriBuilder.toUriString(), request, Configuration.class);
    }

    /**
     * Get the tools config
     * 
     * @param toolConfig
     * @return
     */
    public ToolDetails getToolDetails(String toolConfigId, String customerId) {
        RestTemplate restTemplate = factory.getRestTemplate();
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(appConfig.getPipelineConfigBaseUrl() + TOOL_REGISTRY_ENDPOINT).queryParam(QUERY_PARM_TOOLID, toolConfigId)
                .queryParam(QUERY_CUSTOMER_TOOLID, customerId);
        return restTemplate.getForObject(uriBuilder.toUriString(), ToolDetails.class);
    }

    /**
     * 
     * @param plainText
     * @return
     */
    public String encodeString(String plainText) {
        return Base64.getEncoder().encodeToString(plainText.getBytes());
    }

    /**
     * 
     * @param encodeString
     * @return
     */
    public String decodeString(String encodeString) {
        byte[] actualByte = Base64.getDecoder().decode(encodeString);
        return new String(actualByte);
    }
}
