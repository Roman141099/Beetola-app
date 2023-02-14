package org.beetola.app.exception;

import org.springframework.http.HttpStatus;

import static java.lang.String.format;

public class WebException extends RuntimeException {

    private final HttpStatus status;
    private final String reason;

    public WebException(String message, HttpStatus status, String reason) {
        super(message);
        this.status = status;
        this.reason = reason;
    }

    @Override
    public String getMessage() {
        return format("Something wen wrong with web integration, returned response with code : %d" +
                " and reason : %s with message : %s", status.value(), reason, super.getMessage());
    }
}
