package com.example.snapmap;

import com.google.firebase.database.DatabaseReference;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class users {

    public String userId;
    public String username;
    public String email;
    public String password;
    public String joinDate;
    public users(){};
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getJoinDate() {
        return joinDate;
    }


    public users(String userId, String username, String email, String password, String joinDate) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.password = password;
        this.joinDate = joinDate;
    }


}
