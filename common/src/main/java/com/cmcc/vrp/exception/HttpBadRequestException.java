package com.cmcc.vrp.exception;

/**
 * @author lgk8023
 *
 */
public class HttpBadRequestException extends Exception {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    
    public HttpBadRequestException() {
        super();
    }

    public HttpBadRequestException(String message) {
        super(message);
    }

}
