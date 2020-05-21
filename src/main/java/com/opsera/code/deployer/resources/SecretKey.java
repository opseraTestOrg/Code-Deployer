/**
 * 
 */
package com.opsera.code.deployer.resources;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Purusothaman
 *
 */
@Getter
@Setter
public class SecretKey implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = -4427554139526784456L;

    private String name;

    private String vaultKey;

}
