package com.example.rest;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Path("/customerform")
public class CustomerRestForm {
    /**
     * Class for holding the list of customers and handling the requests
     */

    private static ArrayList<Customer> customers = new ArrayList<>();

    /**
     * Meant for returning the list of customers
     * @return A concatenation of the toString method for all customers
     */
    @GET
    @Produces("application/xml")
    public ArrayList<Customer> getCustomer() {
        return customers;
    }

    /**
     * Meant for getting a customer with a specific ID
     * @param id of the customer
     * @return toString method of customer
     */
    @GET
    @Path("{id}")
    @Produces("application/xml")
    public Customer getCustomerList(@PathParam("id") int id) {
        Customer customer = customers.stream().filter(customer1 -> customer1.getId() == id)
                .findFirst()
                .orElse(null);
        return customer;
    }

    /**
     * Meant for creating customers using the post method
     * @param name of the customer
     * @param age of the customer
     */
    @POST
    public Response createCustomer(@HeaderParam("x-api-key") String token,
                                   @FormParam("name") String name, @FormParam("age") int age) throws GeneralSecurityException, IOException {
        if(validateToken(token)){
            Customer newCustomer = new Customer(name, age);
            customers.add(newCustomer);
            return Response.status(Response.Status.OK).entity("Successfully added customer!").build();
        }
        return Response.status(Response.Status.UNAUTHORIZED).entity("User not authenticated!").build();
    }

    /**
     * Meant for replacing customer with specific ID
     * @param id of the customer
     * @param name of the customer
     * @param age of the customer
     */
    @PUT
    @Path("{id}")
    public Response modifyCustomer(@HeaderParam("x-api-key") String token,
                                   @PathParam("id") int id, @FormParam("name") String name, @FormParam("age") int age) {
        if(validateToken(token)){
           Customer customer = customers.stream().filter(customer1 -> customer1.getId() == id)
                    .findFirst()
                    .orElse(null);
            if(customer != null){
                customer.setAge(age);
                customer.setName(name);
                return Response.status(Response.Status.OK).entity("Succesfully modified customer!").build();
            }
            else{
                return Response.status(Response.Status.OK).entity("Customer does not exist!").build();
            }
        }
        return Response.status(Response.Status.UNAUTHORIZED).entity("User not authenticated!").build();
    }

    /**
     * Meant for deleting customer with specific ID
     * @param id of the customer
     */
    @DELETE
    @Path("{id}")
    public Response deleteCustomer(@HeaderParam("x-api-key") String token, @PathParam("id") int id) {
        if(validateToken(token)){
           customers = customers.stream().filter(customer -> customer.getId() != id)
                    .collect(Collectors.toCollection(ArrayList::new));
            return Response.status(Response.Status.OK).entity("Succesfully deleted customer!").build();
        }
        return Response.status(Response.Status.UNAUTHORIZED).entity("User not authenticated!").build();
    }
    @Bean
    static RestTemplate restTemplate() {
        return new RestTemplate();
    }

    /**
     * This method sends a POST request to the User API, to verify that a call is done
     * by an authenticated user
     * @param token generated from login
     * @return boolean if the user is authenticated or not
     */

    private boolean validateToken(String token) {
        UserRest userRest=new UserRest();
        if(userRest.tokenUsername.containsKey(token)){
            Date timeNow = new Date();
            long diff = timeNow.getTime() - userRest.tokenExpiration.get(token).getTime();
            long tokenDuration = TimeUnit.MILLISECONDS.toMinutes(diff);
            System.out.println("Duration: " + tokenDuration);
            if(tokenDuration > 30){
                userRest.tokenUsername.remove(token);
                userRest.tokenExpiration.remove(token);
            }
            else{
                return true;
            }
        }
        return false;
    }
}
