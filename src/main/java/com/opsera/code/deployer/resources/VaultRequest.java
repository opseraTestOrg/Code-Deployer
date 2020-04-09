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
public class VaultRequest implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6024748784122853021L;

	private String customerId;
	private List<String> componentKeys;

	/**
	 * @return the customerId
	 */
	public String getCustomerId() {
		return customerId;
	}

	/**
	 * @param customerId the customerId to set
	 */
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	/**
	 * @return the componentKeys
	 */
	public List<String> getComponentKeys() {
		return componentKeys;
	}

	/**
	 * @param componentKeys the componentKeys to set
	 */
	public void setComponentKeys(List<String> componentKeys) {
		this.componentKeys = componentKeys;
	}

}