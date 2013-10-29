package com.echo8.bprmf.type;

public class UserItemPair {
    private final String user;
    private final String item;

    public UserItemPair(String user, String item) {
        this.user = user;
        this.item = item;
    }

    public String getUser() {
        return user;
    }

    public String getItem() {
        return item;
    }
}
