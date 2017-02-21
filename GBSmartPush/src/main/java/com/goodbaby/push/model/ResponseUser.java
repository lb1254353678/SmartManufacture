package com.goodbaby.push.model;

/**
 * Created by goodbaby on 17/2/10.
 */

public class ResponseUser {
    private String state;
    private User user;

    public String getState() {
        return state;
    }

    public User getUser() {
        return user;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
