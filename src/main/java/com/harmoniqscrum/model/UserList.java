package com.harmoniqscrum.model;

import java.util.ArrayList;

public class UserList {
    private static UserList Instance;
    private ArrayList<User> users;

    private UserList() {
        this.users = new ArrayList<>();
    }

    public static UserList getInstance() {
        if(Instance == null) {
            Instance = new UserList();
        }
        return Instance;
    }

    public void addUser(User user) {
        users.add(user);
    }

    public void removeUser(User user) {
        users.remove(user);
    }

    public ArrayList<User> getUsers() {
        return users;
    }
}