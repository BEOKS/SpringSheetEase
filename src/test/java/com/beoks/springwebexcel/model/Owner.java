package com.beoks.springwebexcel.model;

import java.util.List;

public class Owner {
    private String name;
    private String address;
    private List<Owner> friends;

    public Owner(String name, String address) {
        this.name = name;
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public List<Owner> getFriends() {
        return friends;
    }

    public void setFriends(List<Owner> friends) {
        this.friends = friends;
    }
}
