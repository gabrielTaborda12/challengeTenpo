package com.tenpo.error.exception;

import java.util.Objects;
import java.util.Optional;

import com.tenpo.error.catalog.ErrorCode;
import com.tenpo.error.conventions.HttpCode;
import org.springframework.http.HttpStatus;

public abstract class AbstractApplicationException extends RuntimeException {
    private final ErrorCode errorCode;
    private final Object details;
    private final String[] messageArgs;

    protected abstract HttpCode getDefaultHttpCode();

    public AbstractApplicationException() {
        this.details = "";
        this.errorCode = null;
        this.messageArgs = null;
    }

    AbstractApplicationException(ErrorCode errorCode, Object details, Throwable cause, String... messageArgs) {
        super(errorCode.getProperties().getMessageId(), cause);
        this.errorCode = errorCode;
        this.details = details;
        this.messageArgs = messageArgs;
    }

    AbstractApplicationException(ErrorCode errorCode, Object details, String... messageArgs) {
        super(errorCode.getProperties().getMessageId());
        this.errorCode = errorCode;
        this.details = details;
        this.messageArgs = messageArgs;
    }

    public int getIntErrorCode() {
        return this.errorCode.getProperties().getErrorCode();
    }

    public String getTitle() {
        return this.errorCode.getProperties().getTitle();
    }

    public HttpStatus getHttpStatus() {
        Objects.requireNonNull(this.getDefaultHttpCode(), "Not null attribute, a default HttpCode must be specified.");
        return ((HttpCode)Optional.ofNullable(this.errorCode.getProperties().getHttpCode()).orElse(this.getDefaultHttpCode())).unwrapType();
    }

    public Object getDetails() {
        return this.details;
    }

    public String[] getMessageArgs() {
        return this.messageArgs;
    }
}
