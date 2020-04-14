package com.opsera.code.deployer.exceptions;

public class GeneralElasticBeanstalkException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2687597792255594720L;

	/**
	 * @param message
	 */
	public GeneralElasticBeanstalkException(String message) {
		super(message);
	}

	/**
	 * 
	 * @param message
	 * @param throwable
	 */
	public GeneralElasticBeanstalkException(String message, Throwable throwable) {
		super(message, throwable);
	}

}
