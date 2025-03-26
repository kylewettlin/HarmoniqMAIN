package com.harmoniqscrum.model;

public class TestLogin {
    public static void main(String[] args) {

        UserList.getInstance().clearUsers();
        // Setup
        UserList userList = UserList.getInstance();

        // Add sample users
        User user1 = new User("Alice", "Smith", "alice123", "alice@example.com", "pass123", "student", "light", "blue");
        User user2 = new User("Bob", "Johnson", "bob456", "bob@example.com", "qwerty", "teacher", "dark", "green");
        
        userList.addUser(user1);
        userList.addUser(user2);

        // Test login via facade
        HarmoniqFACADE facade = new HarmoniqFACADE();
        
        System.out.println("---- Login Tests ----");

        User loggedIn1 = facade.login("alice123", "pass123");
        System.out.println(loggedIn1 != null ? "✅ Login success: " + loggedIn1 : "❌ Login failed");

        User loggedIn2 = facade.login("bob456", "wrongpass");
        System.out.println(loggedIn2 != null ? "✅ Login success: " + loggedIn2 : "❌ Login failed (expected)");

        User loggedIn3 = facade.login("unknownUser", "12345");
        System.out.println(loggedIn3 != null ? "✅ Login success: " + loggedIn3 : "❌ Login failed (expected)");
    }
}
