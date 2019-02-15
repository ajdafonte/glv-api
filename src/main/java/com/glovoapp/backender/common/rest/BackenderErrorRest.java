package com.glovoapp.backender.common.rest;

import javax.servlet.http.HttpServletRequest;

import com.glovoapp.backender.common.error.BackenderApiError;


/**
 * General error rest response object for the Backender API.
 */
public class BackenderErrorRest
{
    private final String description;
    private final String url;

    public BackenderErrorRest(final HttpServletRequest request, final BackenderApiError error, final String... errorParameters)
    {
        this.description = error.getErrorDescription(errorParameters);
        this.url = request.getRequestURL().toString();
    }

    public String getDescription()
    {
        return description;
    }

    public String getUrl()
    {
        return url;
    }
}
