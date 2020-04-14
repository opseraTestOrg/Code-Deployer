/**
 * 
 */
package com.opsera.code.deployer.resources;

import java.io.File;
import java.io.Serializable;

/**
 * @author Purusothaman
 *
 */
public class SSHDetailsRequest extends SSHDetails implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -987742566166286199L;

	private String sshKey;
	private String action;
	private File file;
	private String fileName;

	/**
	 * @return the sshKey
	 */
	public String getSshKey() {
		return sshKey;
	}

	/**
	 * @param sshKey the sshKey to set
	 */
	public void setSshKey(String sshKey) {
		this.sshKey = sshKey;
	}

	/**
	 * @return the action
	 */
	public String getAction() {
		return action;
	}

	/**
	 * @param action the action to set
	 */
	public void setAction(String action) {
		this.action = action;
	}

	/**
	 * @return the file
	 */
	public File getFile() {
		return file;
	}

	/**
	 * @param file the file to set
	 */
	public void setFile(File file) {
		this.file = file;
	}

	/**
	 * @return the fileName
	 */
	public String getFileName() {
		return fileName;
	}

	/**
	 * @param fileName the fileName to set
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

}
