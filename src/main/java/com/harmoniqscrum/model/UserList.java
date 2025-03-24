package com.harmoniqscrum.model;

import java.util.ArrayList;

public class UserList {
    private static UserList Instance;
    private ArrayList<User> users;

    private UserList() {
        this.users = DataLoader.getUsers();
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
    
    public boolean haveUser(String username) {
        for(User user : users) {
            if(user.getUsername().equals(username)) {
                return true;
            }
        }
        
        return false;
    }
    
    public User getUser(String username) {
        for(User user : users) {
            if(user.getUsername().equals(username)) {
                return user;
            }
        }
        
        return null;
    }
    
    public boolean addUser(String firstName, String lastName, String username, 
                          String email, String password, String role, 
                          String theme, String highlightColor) {
        if(haveUser(username)) return false;
        
        users.add(new User(firstName, lastName, username, email, 
                             password, role, theme, highlightColor));
        return true;
    }
    
    public void saveUsers() {
        DataWriter.saveUsers();
    }
}