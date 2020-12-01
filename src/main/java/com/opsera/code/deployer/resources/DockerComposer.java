/**
 * 
 */
package com.opsera.code.deployer.resources;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;
import lombok.ToString;

/**
 * @author Purusothaman
 *
 */
@Data
@ToString
@JsonInclude(value = Include.NON_NULL)
public class DockerComposer {
    private String version;
    private Map<String, Map<String, Object>> services;



}
