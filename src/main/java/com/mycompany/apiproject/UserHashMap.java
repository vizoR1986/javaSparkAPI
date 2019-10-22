/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.apiproject;

import java.util.Collection;
import java.util.HashMap;
import org.omg.CORBA.UserException;

/**
 *
 * @author robertnagy
 */
public class UserHashMap implements UserService {

    private HashMap<String, User> users = new HashMap<>();

    @Override
    public void addUser(User user) {
        users.put(user.getId(), user);
    }

    @Override
    public Collection<User> getUsers() {
        return users.values();
    }

    @Override
    public User getUser(String id) {
        User returnUser = null;
        if (users.containsKey(id)) {
            returnUser = users.get(id);
        }
        return returnUser;
    }

    @Override
    public User editUser(User user) throws UserException {
        throw new UnsupportedOperationException("Not supported yet."); //ToDo for editing users
    }

    @Override
    public void deleteUser(String id) {
        getUsers().remove(id);
    }

    @Override
    public boolean userExist(String id) {
        return getUsers().contains(id);
    }

    public void setUsers(HashMap<String, User> users) {
        this.users = users;
    }

    public HashMap<String, User> getUsers(HashMap<String, User> users) {
        return users;
    }

}
