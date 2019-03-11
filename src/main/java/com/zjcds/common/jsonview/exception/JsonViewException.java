package com.zjcds.common.jsonview.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

/**
 * created date：2016-11-29
 *
 * @author niezhegang
 */
@Getter
@Setter
public class JsonViewException extends RuntimeException {

    private HttpStatus httpStatus = HttpStatus.OK;

    public JsonViewException() {
        super();
    }

    public JsonViewException(String message) {
        super(message);
    }

    public JsonViewException(String message, Throwable cause) {
        super(message, cause);
    }

    public JsonViewException(Throwable cause) {
        super(cause);
    }

    protected JsonViewException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }


}
