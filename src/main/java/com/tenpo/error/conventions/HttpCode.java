package com.tenpo.error.conventions;

import org.springframework.http.HttpStatus;

public enum HttpCode {
    OK(HttpStatus.OK),
    CREATED(HttpStatus.CREATED),
    BAD_REQUEST(HttpStatus.BAD_REQUEST),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED),
    FORBIDDEN(HttpStatus.FORBIDDEN),
    NOT_FOUND(HttpStatus.NOT_FOUND),
    UNPROCESSABLE_ENTITY(HttpStatus.UNPROCESSABLE_ENTITY),
    TOO_MANY_REQUESTS(HttpStatus.TOO_MANY_REQUESTS),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR);

    private final HttpStatus httpStatus;

    private HttpCode(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }

    public HttpStatus unwrapType() {
        return this.httpStatus;
    }
}
