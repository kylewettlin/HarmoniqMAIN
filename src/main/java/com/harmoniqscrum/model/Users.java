package com.harmoniqscrum.model;

import java.util.ArrayList;

public class Users {
    private static Users users;
    private ArrayList<User> userList;
    
    private Users() {
        userList = DataLoader.getUsers();
    }
    
    public static Users getInstance() {
        if(users == null) {
            users = new Users();
        }
        
        return users;
    }

    public boolean haveUser(String username) {
        for(User user : userList) {
            if(user.getUsername().equals(username)) {
                return true;
            }
        }
        
        return false;
    }
    
    public User getUser(String username) {
        for(User user : userList) {
            if(user.getUsername().equals(username)) {
                return user;
            }
        }
        
        return null;
    }
    
    public ArrayList<User> getUsers() {
        return userList;
    }
    
    public boolean addUser(String firstName, String lastName, String username, 
                          String email, String password, String role, 
                          String theme, String highlightColor) {
        if(haveUser(username)) return false;
        
        userList.add(new User(firstName, lastName, username, email, 
                             password, role, theme, highlightColor));
        return true;
    }
    
    public void saveUsers() {
        DataWriter.saveUsers();
    }
} 