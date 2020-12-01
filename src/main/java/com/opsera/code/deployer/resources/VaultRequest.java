/**
 * 
 */
package com.opsera.code.deployer.resources;

import java.io.Serializable;
import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Purusothaman
 *
 */
@Getter
@Setter
@Builder
public class VaultRequest implements Serializable {
    /**
     * 
     */
    private static final long serialVersionUID = 6024748784122853021L;

    private String customerId;

    private List<String> componentKeys;

}
