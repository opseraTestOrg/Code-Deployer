/**
 * 
 */
package com.opsera.code.deployer.resources;

import static com.opsera.code.deployer.resources.CodeDeployerConstants.IN_PROGRESS;

/**
 * @author Purusothaman
 *
 */
public enum DeploymentStatus {

    ABORTING("aborting"), LAUNCHING(IN_PROGRESS), UPDATING(IN_PROGRESS), LINKINGFROM(IN_PROGRESS), LINKINGTO(IN_PROGRESS), READY("successful"), TERMINATING("terminating"), TERMINATED("terminated");

    private String name;

    /**
     * 
     * @param name
     * @param port
     */
    private DeploymentStatus(String name) {
        this.name = name;
    }

    /**
     * 
     * @param name
     * @return
     */
    public String getName() {
        return this.name;
    }
}
