package com.tenpo.error.catalog;

import com.tenpo.error.conventions.HttpCode;

public enum TenpoErrorCode implements ErrorCode {
    PARAM_INVALID(1001, HttpCode.BAD_REQUEST, "tenpo.validation.parameters.invalid"),
    BAD_REQUEST_USER(1001, HttpCode.BAD_REQUEST, "tenpo.validation.parameters.invalid.user"),
    NOT_FOUND_LOGIN(1001, HttpCode.NOT_FOUND, "tenpo.validation.parameters.invalid.login"),
    BAD_REQUEST_NAME(1001, HttpCode.BAD_REQUEST, "tenpo.validation.parameters.invalid.user.name"),
    BAD_REQUEST_LAST_NAME(1001, HttpCode.BAD_REQUEST, "tenpo.validation.parameters.invalid.last.user"),
    BAD_REQUEST_EMAIL(1001, HttpCode.BAD_REQUEST, "tenpo.validation.parameters.invalid.email"),
    FORBIDDEN(1002, HttpCode.FORBIDDEN, "tenpo.forbidden"),
    INTERNAL_SERVER_ERROR(1002, HttpCode.INTERNAL_SERVER_ERROR, "tenpo.server.error");

    private final Properties properties;

    TenpoErrorCode(int errorCode, HttpCode httpCode, String messageId) {
        this.properties = Properties.of(errorCode, httpCode, this.name(), messageId);
    }

    @Override
    public Properties getProperties() {
        return properties;
    }
}
