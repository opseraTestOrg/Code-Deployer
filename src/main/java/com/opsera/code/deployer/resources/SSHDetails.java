/**
 * 
 */
package com.opsera.code.deployer.resources;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Purusothaman
 *
 */
@Getter
@Setter
public class SSHDetails {

    private String serverIp;
    private String userName;
    private String serverPath;
    private List<String> commands;
    private String sshAuthKeyName;
    private SecretKey secretKey;
    private SshKey sshKey;
    private String sshAction;

}
