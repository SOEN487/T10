package com.example.rest;

//import javax.annotation.security.PermitAll;
import javax.ws.rs.*;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Path("/user")
public class UserRest {

    public static ArrayList<User> users = new ArrayList<>();
    public static Map<String, String> tokenUsername = new HashMap<String, String>();
    public static Map<String, Date> tokenExpiration = new HashMap<String, Date>();
    public static String tokenHeader="";

    /**
     * Meant to add user that can utilize the API
     * @param username of user
     * @param password of user
     */
    @POST
    @Path("/register")
    public String createUser(@FormParam("username") String username, @FormParam("password") String password) {
        User user = new User(username, password);
        users.add(user);
        return "Created user: " + username;
    }

    /**
     * Used to login the API with an existing user
     * @param username of user
     * @param password of user
     * @return string containing the login token
     */
    @POST
    @Path("/login")
    @Produces("application/json")
    public Response login(@FormParam("username") String username, @FormParam("password") String password) {
        User user = users.stream().filter(user1 -> user1.getUsername().equals(username))
                .findFirst()
                .orElse(null);
        MyResponse authResponse;
        Response.Status status;
        if(user != null){
            if (user.getPassword().equals(password))
            {
                user.generateToken();
                tokenUsername.put(user.getToken(), username);
                tokenExpiration.put(user.getToken(), new Date());
                authResponse = new MyResponse(true, user.getToken());
                status = Response.Status.OK;
               }
            else{
                authResponse = new MyResponse(false, "");
                status = Response.Status.UNAUTHORIZED;
            }
        }
        else{
            authResponse = new MyResponse(false, "");
            status = Response.Status.FORBIDDEN;
        }
        return Response.status(status).entity(authResponse).build();
    }

    /**
     * Used to logout from the API and delete the token
     * @param username of user
     * @return string containing the login token
     */
    @POST
    @Path("/logout")
    @Produces("application/json")
    public String logout(@FormParam("username") String username) {
        User user = users.stream().filter(user1 -> user1.getUsername().equals(username))
                .findFirst()
                .orElse(null);
        if(user != null){
            if(user.getToken().equals("")){
                return "Not logged in.";
            }
            else{
                tokenUsername.remove(user.getToken());
                tokenExpiration.remove(user.getToken());
                user.destroyToken();
                return "Logged out. Token succesfully destroyed.";
            }
        }
        else{
            return "User does not exist!";
        }
    }
}
