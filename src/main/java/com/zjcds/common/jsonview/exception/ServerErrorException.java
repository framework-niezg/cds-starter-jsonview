package com.zjcds.common.jsonview.exception;

import org.springframework.http.HttpStatus;

/**
 * created date：2018-06-25
 * @author niezhegang
 */
public class ServerErrorException extends JsonViewException {
    public ServerErrorException() {
        super();
        setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public ServerErrorException(String message) {
        super(message);
        setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public ServerErrorException(String message, Throwable cause) {
        super(message, cause);
        setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public ServerErrorException(Throwable cause) {
        super(cause);
        setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    protected ServerErrorException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
        setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
