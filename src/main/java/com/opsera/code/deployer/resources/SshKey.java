/**
 * 
 */
package com.opsera.code.deployer.resources;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Purusothaman
 *
 */
@Getter
@Setter
public class SshKey {
    private String fileName;
    private String vaultKey;

}
