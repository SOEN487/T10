package com.example.rest;
import javax.ws.rs.container.*;
import javax.ws.rs.ext.Provider;
import java.io.IOException;

@PreMatching
@Provider
public class PreMatchingFilter implements ContainerResponseFilter  {
    @Override
    public void filter(ContainerRequestContext requestContext,ContainerResponseContext responseContext)
            throws IOException {
        if (requestContext.getMethod().equals("POST")) {
            responseContext.getHeaders().add("Method","POST");
        }
        if (requestContext.getMethod().equals("PUT")) {
            responseContext.getHeaders().add("Method","PUT");
        }
        if (requestContext.getMethod().equals("GET")) {
            responseContext.getHeaders().add("Method","GET");
        }
        if (requestContext.getMethod().equals("DELETE")) {
            responseContext.getHeaders().add("Method","DELETE");
        }
    }

}



























