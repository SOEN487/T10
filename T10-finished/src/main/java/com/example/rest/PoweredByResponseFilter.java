package com.example.rest;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import org.jboss.resteasy.core.ServerResponse;
import org.jboss.resteasy.core.Headers;

@Provider
public class PoweredByResponseFilter implements ContainerResponseFilter {
    private static final ServerResponse ACCESS_DENIED = new ServerResponse("Access denied for this resource", 401, new Headers<Object>());;
    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext)
            throws IOException {
        responseContext.getHeaders().add("X-Powered-By", "SOEN487 Filter Demo");
        UriInfo uriInfo=requestContext.getUriInfo();
        if (uriInfo.getPath().contains("user/register") || uriInfo.getPath().contains("user/login") || uriInfo.getPath().contains("user/logout") ) {
            return;
        }
        String authorizationHeaderToken = requestContext.getHeaderString("Authorization-Token");
        //Adding same token to the response (for Demoing purpose only)
        // NOT TO BE INCLUDED IN THE PRODUCTION.
        responseContext.getHeaders().add("Authorization-Token", authorizationHeaderToken);
        UserRest r=new UserRest();
        Date generatedTime=r.tokenExpiration.get(authorizationHeaderToken);
        //if (generatedTime != null) //Means the token is the same
        if (generatedTime != null) {
            Date timeNow = new Date();
            long diff = timeNow.getTime() - generatedTime.getTime() ;
            long tokenDuration = TimeUnit.MILLISECONDS.toMinutes(diff);
            System.out.println("Duration: " + tokenDuration);
            if (tokenDuration > 30){
                r.tokenUsername.remove(authorizationHeaderToken);
                r.tokenExpiration.remove(authorizationHeaderToken);
                System.out.println("Session timed out");
                requestContext.abortWith(ACCESS_DENIED);
                return;
            }
            else{
                System.out.println("Token in Header is VALID");
                return;
            }
        }
        else // If the token is changed
        {
            System.out.println("Token in Header is NOT VALID");
            requestContext.abortWith(ACCESS_DENIED);
            return;
        }
    }
}




