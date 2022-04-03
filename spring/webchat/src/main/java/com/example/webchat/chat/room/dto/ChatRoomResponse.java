package com.example.webchat.chat.room.dto;

import com.example.webchat.chat.room.ChatRoom;
import lombok.Getter;

@Getter
public class ChatRoomResponse {

    private final ChatRoom chatRoom;
    private final int userCount;

    public ChatRoomResponse(ChatRoom chatRoom, int userCount) {
        this.chatRoom = chatRoom;
        this.userCount = userCount;
    }
}
