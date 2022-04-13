package com.example.webchat.stomp;

import java.security.Principal;
import lombok.Getter;
import lombok.Setter;

@Getter
public class StompPrincipal implements Principal {

    private final String name;
    @Setter
    private String userName;
    @Setter
    private String roomId;

    public StompPrincipal(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return this.name;
    }
}