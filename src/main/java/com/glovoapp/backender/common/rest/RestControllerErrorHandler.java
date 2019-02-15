package com.glovoapp.backender.common.rest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.NoHandlerFoundException;

import com.glovoapp.backender.common.error.BackenderApiError;
import com.glovoapp.backender.common.error.BackenderApiException;


/**
 * Handles all exceptions for all REST controllers and translates them to a proper error response. Because we don't limit it to RestController.class,
 * it also deals with both @{@link org.springframework.web.HttpMediaTypeException}.
 */
@ControllerAdvice
public class RestControllerErrorHandler
{
    @ExceptionHandler(BackenderApiException.class)
    @ResponseBody
    BackenderErrorRest handleBackenderApiException(final HttpServletRequest request,
                                                   final HttpServletResponse response,
                                                   final BackenderApiException exception)
    {
        response.setStatus(exception.getError().getHttpStatus().value());
        return new BackenderErrorRest(request, exception.getError(), exception.getErrorParameters());
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    @ResponseBody
    BackenderErrorRest handleException(final HttpServletRequest request, final Exception e)
    {
        return new BackenderErrorRest(request, BackenderApiError.INTERNAL_SERVER_ERROR);
    }

    /**
     * Thrown when the requested resource is unknown.
     */
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseBody
    BackenderErrorRest handleNoHandlerFoundException(final HttpServletRequest request,
                                                     final NoHandlerFoundException exception)
    {
        return new BackenderErrorRest(request, BackenderApiError.UNKNOWN_RESOURCE, exception.getRequestURL());
    }
}
