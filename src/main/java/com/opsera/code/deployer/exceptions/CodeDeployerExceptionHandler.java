package com.opsera.code.deployer.exceptions;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * 
 * @author Purusothaman
 *
 */
@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class CodeDeployerExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(InvalidDataException.class)
	protected ResponseEntity<Object> handleInvalidDataException(InvalidDataException ex) {
		CodeDeployerErrorResponse codeDeployerErrorResponse = new CodeDeployerErrorResponse();
		codeDeployerErrorResponse.setMessage(ex.getMessage());
		codeDeployerErrorResponse.setStatus(HttpStatus.BAD_REQUEST.name());
		return new ResponseEntity<>(codeDeployerErrorResponse, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(GeneralElasticBeanstalkException.class)
	protected ResponseEntity<Object> handleGeneralElasticBeanstalkException(GeneralElasticBeanstalkException ex) {
		CodeDeployerErrorResponse codeDeployerErrorResponse = new CodeDeployerErrorResponse();
		codeDeployerErrorResponse.setMessage(ex.getMessage());
		codeDeployerErrorResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.name());
		return new ResponseEntity<>(codeDeployerErrorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
	}
}