/**
 * 
 */
package com.opsera.code.deployer.resources;

import java.io.Serializable;
import java.util.List;

/**
 * @author Purusothaman
 *
 */
public class SSHDetails implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -987742566166286199L;

    private String serverIp;
    private String userName;
    private String serverPath;
    private List<String> commands;
    private String sshKeyName;

    /**
     * @return the serverIp
     */
    public String getServerIp() {
        return serverIp;
    }

    /**
     * @param serverIp the serverIp to set
     */
    public void setServerIp(String serverIp) {
        this.serverIp = serverIp;
    }

    /**
     * @return the userName
     */
    public String getUserName() {
        return userName;
    }

    /**
     * @param userName the userName to set
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * @return the commands
     */
    public List<String> getCommands() {
        return commands;
    }

    /**
     * @param commands the commands to set
     */
    public void setCommands(List<String> commands) {
        this.commands = commands;
    }

    /**
     * @return the serverPath
     */
    public String getServerPath() {
        return serverPath;
    }

    /**
     * @param serverPath the serverPath to set
     */
    public void setServerPath(String serverPath) {
        this.serverPath = serverPath;
    }

    /**
     * @return the sshKeyName
     */
    public String getSshKeyName() {
        return sshKeyName;
    }

    /**
     * @param sshKeyName the sshKeyName to set
     */
    public void setSshKeyName(String sshKeyName) {
        this.sshKeyName = sshKeyName;
    }

}
