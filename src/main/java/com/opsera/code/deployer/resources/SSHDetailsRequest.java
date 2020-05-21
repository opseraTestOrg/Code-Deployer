/**
 * 
 */
package com.opsera.code.deployer.resources;

import java.io.File;
import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Purusothaman
 *
 */
@Getter
@Setter
public class SSHDetailsRequest extends SSHDetails implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -987742566166286199L;

    private String sshAuthKey;
    private String action;
    private File file;
    private String fileName;

}
