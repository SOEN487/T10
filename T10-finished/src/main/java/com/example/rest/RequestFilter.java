package com.example.rest;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.Provider;
import java.io.IOException;
import java.util.Date;
import java.util.concurrent.TimeUnit;
@Provider
public class RequestFilter implements ContainerRequestFilter {
    @Override
    public void filter(ContainerRequestContext requestContext)
            throws IOException {
        UriInfo uriInfo=requestContext.getUriInfo();
        if (uriInfo.getPath().contains("user/register") || uriInfo.getPath().contains("user/login") || uriInfo.getPath().contains("user/logout") ) {
            return ;
        }
        UserRest.tokenHeader = requestContext.getHeaderString("Authorization-Token");
       // UserRest r=new UserRest();
        System.out.println("token Header in filter: "+UserRest.tokenHeader);
        Date generatedTime=UserRest.tokenExpiration.get(UserRest.tokenHeader);
        //if (generatedTime != null) //Means the token is the same
        if (generatedTime != null) {
            Date timeNow = new Date();
            long diff = timeNow.getTime() - generatedTime.getTime() ;
            long tokenDuration = TimeUnit.MILLISECONDS.toMinutes(diff);
            System.out.println("Duration: " + tokenDuration);
            if (tokenDuration > 30){
                UserRest.tokenUsername.remove(UserRest.tokenHeader);
                UserRest.tokenExpiration.remove(UserRest.tokenHeader);
                System.out.println("Session timed out");
                requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).entity("User not authenticated by Filter!").build());

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
            requestContext.abortWith(Response.status(Response.Status.UNAUTHORIZED).entity("User not authenticated by Filter!").build());
            return ;
        }

    }
}
