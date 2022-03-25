package com.tenpo.error.exception;

import com.tenpo.error.catalog.ErrorCode;
import com.tenpo.error.conventions.HttpCode;

public class BusinessException extends AbstractApplicationException {
    private static final HttpCode DEFAULT_HTTP_CODE;

    public BusinessException(ErrorCode errorCode, Object details, Throwable cause, String... messageArgs) {
        super(errorCode, details, cause, messageArgs);
    }

    public BusinessException(ErrorCode errorCode, Object details, String... messageArgs) {
        super(errorCode, details, messageArgs);
    }

    public BusinessException(ErrorCode errorCode, Throwable cause, String... messageArgs) {
        this(errorCode, (Object)null, cause, messageArgs);
    }

    public BusinessException(ErrorCode errorCode, String... messageArgs) {
        this(errorCode, (Throwable)null, messageArgs);
    }

    protected HttpCode getDefaultHttpCode() {
        return DEFAULT_HTTP_CODE;
    }

    static {
        DEFAULT_HTTP_CODE = HttpCode.UNPROCESSABLE_ENTITY;
    }
}
