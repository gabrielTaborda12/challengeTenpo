package com.tenpo.error.catalog;

import com.tenpo.error.conventions.HttpCode;

public enum CommonErrorCode implements ErrorCode {
    accessDenied(403, HttpCode.FORBIDDEN, "arsenal.security.authorization.fail"),
    missingRequestParameter(9003, HttpCode.BAD_REQUEST, "arsenal.conventions.request.missing_param"),
    missingRequestBody(9004, HttpCode.BAD_REQUEST, "arsenal.conventions.request.missing_body"),
    validationErrorPropertyConstraintViolation(9005, HttpCode.BAD_REQUEST, "arsenal.validation.property.violation"),
    validationErrorMethodArgumentNotValid(9006, HttpCode.BAD_REQUEST, "arsenal.validation.method.param.violation"),
    validationErrorTypeMismatch(9007, HttpCode.BAD_REQUEST, "arsenal.validation.type.mismatch.violation"),
    noHandlerFound(9008, HttpCode.NOT_FOUND, "arsenal.conventions.request.no_handler_found"),
    unknownError(9999, HttpCode.INTERNAL_SERVER_ERROR, "arsenal.unknown_error");

    private final Properties properties;

    private CommonErrorCode(int errorCode, HttpCode httpCode, String message) {
        this.properties = Properties.of(errorCode, httpCode, this.name(), message);
    }

    private CommonErrorCode(int errorCode, String message) {
        this.properties = Properties.of(errorCode, (HttpCode)null, this.name(), message);
    }

    public Properties getProperties() {
        return this.properties;
    }
}
