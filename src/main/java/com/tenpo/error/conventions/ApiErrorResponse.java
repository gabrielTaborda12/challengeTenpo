package com.tenpo.error.conventions;

import java.time.Instant;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ApiErrorResponse {
    private final String timestamp = Instant.now().toString();
    private final HttpStatus httpStatus;
    private final int errorCode;
    private final String title;
    private final String message;
    private final Object details;
    private final String traceId;

    public ApiErrorResponse(HttpStatus httpStatus, int errorCode, String title, String message, Object details) {
        this.httpStatus = httpStatus;
        this.errorCode = errorCode;
        this.title = title;
        this.message = message;
        this.details = details;
        Optional var10001 = Optional.ofNullable(MDC.get("traceId"));
        UUID var10002 = UUID.randomUUID();
        Objects.requireNonNull(var10002);
        this.traceId = (String)var10001.orElseGet(var10002::toString);
        MDC.put("traceId", this.traceId);
    }

    public String getTimestamp() {
        return this.timestamp;
    }

    public HttpStatus getHttpStatus() {
        return this.httpStatus;
    }

    public int getErrorCode() {
        return this.errorCode;
    }

    public String getTitle() {
        return this.title;
    }

    public String getMessage() {
        return this.message;
    }

    public Object getDetails() {
        return this.details;
    }

    public String getTraceId() {
        return this.traceId;
    }

    public static final class Entity {
        private Entity() {
            throw new IllegalStateException("Utility class");
        }

        public static ResponseEntity<ApiErrorResponse> of(HttpStatus httpStatus, int errorCode, String title, String message, Object details) {
            return new ResponseEntity(new ApiErrorResponse(httpStatus, errorCode, title, message, details), httpStatus);
        }

        public static ResponseEntity<ApiErrorResponse> of(HttpStatus httpStatus, int errorCode, String title, String message) {
            return of(httpStatus, errorCode, title, message, (Object)null);
        }
    }
}
