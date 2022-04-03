package com.example.webchat.chat.room;

import java.io.Serializable;
import java.util.UUID;
import lombok.Getter;

@Getter
public class ChatRoom implements Serializable {

    private final String id;
    private final String name;

    public ChatRoom(String name) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
    }

}
