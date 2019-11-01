/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.apiproject;

import com.google.gson.Gson;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import static spark.Spark.*;

/**
 *
 * @author robertnagy
 */
public class Main {

    public static void main(String[] args) {
        Main main = new Main();
        main.setSparkAPI();
    }

    public void setSparkAPI() {
        final UserService userService = new UserHashMap();

        post("/hello/users", (request, response) -> {
            response.type("application/json");

            User user = new Gson().fromJson(request.body(), User.class);
            
            if (!checkForUsername(user.getFirstName(), user.getLastName())) {
                return new Gson().toJson(new StandardResponse(StatusResponse.ERROR, "The specified name for the user contains special characters , please use alphabetical letters only..."));
            }
            /*if (!checkForGivenDate(user.getBirthDay())) {
                return new Gson().toJson(new StandardResponse(StatusResponse.ERROR, "The specified date must be before today , please add the birth date accordingly..."));
            }*/
            
            userService.addUser(user);
            return new Gson().toJson(new StandardResponse(
                    StatusResponse.SUCCESS));

        });

        get("/hello/users", (request, response) -> {
            response.type("application/json");
            if (userService.getUsers().isEmpty()) {
                halt(401, "There is no user stored in the DB");
            }
            return new Gson().toJson(new StandardResponse(StatusResponse.SUCCESS, new Gson().toJsonTree(userService.getUsers())));
        });

        get("/hello/users/:id", (request, response) -> {
            response.type("application/json");
            if(!userService.getUsers().contains(request.params(":id"))) {
                halt(401,"There was no user found in the database that has the following ID :" + request.params(":id"));
            }
            return new Gson().toJson(new StandardResponse(StatusResponse.SUCCESS, new Gson().toJsonTree(userService.getUser(request.params(":id")))));
        });

        put("/hello/users/:id", (request, response) -> {
            response.type("application/json");

            User toEdit = new Gson().fromJson(request.body(), User.class);
            User editedUser = userService.editUser(toEdit);

            if (editedUser != null) {
                return new Gson().toJson(new StandardResponse(StatusResponse.SUCCESS, new Gson().toJsonTree(editedUser)));
            } else {
                return new Gson().toJson(new StandardResponse(StatusResponse.ERROR, new Gson().toJson("User not found or error in edit")));
            }
        });

        delete("/hello/users/:id", (request, response) -> {
            response.type("application/json");

            userService.deleteUser(request.params(":id"));
            return new Gson().toJson(new StandardResponse(StatusResponse.SUCCESS, "user deleted"));
        });

        options("/hello/users/:id", (request, response) -> {
            response.type("application/json");

            return new Gson().toJson(new StandardResponse(StatusResponse.SUCCESS, (userService.userExist(request.params(":id"))) ? "User exists" : "User does not exists"));
        });

        get("/borkai", (req, res) -> {
            return "A ló néz a csikó lát, felszívnék egy csík kólát";
        });
    }

    boolean checkForGivenDate(String userDate) {
        Date checkDate = null;
        Date currentDate = new Date();
        try {
            SimpleDateFormat simpledf = new SimpleDateFormat("yyyy-MM-dd");
            checkDate = simpledf.parse(userDate);
        } catch (ParseException ex) {
            System.out.println(ex.getStackTrace());
        }
        boolean check = (checkDate.after(currentDate)) ? true : false;
        return check;
    }

    boolean checkForUsername(String firstName, String lastName) {
        String regexpForUser = "^[a-z]+$";
        Pattern p = Pattern.compile(regexpForUser, Pattern.CASE_INSENSITIVE);
        Matcher mLast = p.matcher(lastName+firstName);
        boolean isNameProper = (mLast.matches()) ? true : false;
        return isNameProper;
    }
}
