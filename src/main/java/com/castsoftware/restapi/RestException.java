package com.castsoftware.restapi;

public class RestException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
    
	public RestException(String message) {
        super(message);
    }
}
