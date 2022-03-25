package com.tenpo.error.handler;

import com.tenpo.error.catalog.CommonErrorCode;
import com.tenpo.error.catalog.ErrorCode;
import com.tenpo.error.catalog.TenpoErrorCode;
import com.tenpo.error.conventions.ApiErrorResponse;
import com.tenpo.error.conventions.HttpCode;
import com.tenpo.error.exception.AbstractApplicationException;
import com.tenpo.error.exception.BusinessException;
import com.tenpo.error.util.MessageHelper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

@Order(-2147483648)
@RestControllerAdvice
public class DefaultExceptionHandler {

    @Autowired
    private MessageHelper messageHelper;

    public DefaultExceptionHandler() {
    }

    @ExceptionHandler({BusinessException.class})
    public ResponseEntity<ApiErrorResponse> handleBusinessException(BusinessException exception) {
        return this.buildResponse(exception);
    }

    /*@ExceptionHandler({Exception.class})
    public ResponseEntity<ApiErrorResponse> handleGenericException(Exception exception) {
        return this.buildResponse(CommonErrorCode.unknownError, exception.getMessage());
    }*/

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<ApiErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        List<String> details = new ArrayList(ex.getBindingResult().getFieldErrors().size());
        Iterator var3 = ex.getBindingResult().getFieldErrors().iterator();

        while(var3.hasNext()) {
            FieldError fieldError = (FieldError)var3.next();
            details.add(fieldError.getField().concat(": ").concat(fieldError.getDefaultMessage()));
        }

        return this.buildResponse(TenpoErrorCode.PARAM_INVALID, details);
    }

    protected ResponseEntity<ApiErrorResponse> buildResponse(AbstractApplicationException exception) {
        return ApiErrorResponse.Entity.of(exception.getHttpStatus(), exception.getIntErrorCode(), exception.getTitle(), this.messageHelper.resolve(exception.getMessage(), exception.getMessageArgs()), exception.getDetails());
    }

    protected ResponseEntity<ApiErrorResponse> buildResponse(HttpCode httpCode, ErrorCode errorCode, Object details) {
        return ApiErrorResponse.Entity.of(httpCode.unwrapType(), errorCode.getProperties().getErrorCode(), errorCode.getProperties().getTitle(), this.messageHelper.resolve(errorCode.getProperties().getMessageId(), new String[0]), details);
    }

    protected ResponseEntity<ApiErrorResponse> buildResponse(ErrorCode errorCode, Object details) {
        Objects.requireNonNull(errorCode.getProperties().getHttpCode(), "Missing HTTP response status code.");
        return this.buildResponse(errorCode.getProperties().getHttpCode(), errorCode, details);
    }

}
