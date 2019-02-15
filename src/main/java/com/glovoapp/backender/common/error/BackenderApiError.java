package com.glovoapp.backender.common.error;

import org.springframework.http.HttpStatus;

import com.google.common.base.MoreObjects;


/**
 * Holds all specific errors returned by Backender API.
 */
public enum BackenderApiError
{
    // Default error code
    INTERNAL_SERVER_ERROR("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR),

    //  Resource specific error codes
    UNKNOWN_ORDER("Resource not found. %s", HttpStatus.NOT_FOUND),
    // Common error codes for request handling
    UNKNOWN_RESOURCE("Resource not found. %s", HttpStatus.NOT_FOUND),
    UNSUPPORTED_REQUEST_METHOD("'%s' is not supported for this resource", HttpStatus.METHOD_NOT_ALLOWED),
    UNSUPPORTED_MEDIA_TYPE("Media type of the request body is unsupported - use '%s' instead", HttpStatus.UNSUPPORTED_MEDIA_TYPE),
    BAD_REQUEST("Bad request", HttpStatus.BAD_REQUEST),
    INVALID_REQUEST("Invalid request: %s", HttpStatus.BAD_REQUEST);

    private final String errorText;
    private final HttpStatus httpStatus;

    BackenderApiError(final String errorText, final HttpStatus httpStatus)
    {
        this.errorText = errorText;
        this.httpStatus = httpStatus;
    }

    public String getErrorCode()
    {
        return name();
    }

    public String getErrorDescription(final String... parameters)
    {
        return String.format(errorText, (Object[]) parameters);
    }

    public HttpStatus getHttpStatus()
    {
        return httpStatus;
    }

    @Override
    public String toString()
    {
        return MoreObjects.toStringHelper(this)
            .add("errorText", errorText)
            .add("httpStatus", httpStatus)
            .toString();
    }
}
