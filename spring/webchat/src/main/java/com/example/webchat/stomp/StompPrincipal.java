package com.example.webchat.stomp;

import java.security.Principal;

public class StompPrincipal implements Principal {

    private final String name;
    private final String userName;

    public StompPrincipal(String name, String userName) {
        this.name = name;
        this.userName = userName;
    }

    @Override
    public String getName() {
        return this.name;
    }

    public String getUserName() {
        return this.userName;
    }
}