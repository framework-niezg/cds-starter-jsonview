package com.zjcds.common.jsonview.exception;

import org.springframework.http.HttpStatus;

/**
 * created date：2018-06-25
 * @author niezhegang
 */
public class ClientErrorException extends JsonViewException {
    public ClientErrorException() {
        super();
        setHttpStatus(HttpStatus.BAD_REQUEST);
    }

    public ClientErrorException(String message) {
        super(message);
        setHttpStatus(HttpStatus.BAD_REQUEST);
    }

    public ClientErrorException(String message, Throwable cause) {
        super(message, cause);
        setHttpStatus(HttpStatus.BAD_REQUEST);
    }

    public ClientErrorException(Throwable cause) {
        super(cause);
        setHttpStatus(HttpStatus.BAD_REQUEST);
    }

    protected ClientErrorException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
        setHttpStatus(HttpStatus.BAD_REQUEST);
    }
}
