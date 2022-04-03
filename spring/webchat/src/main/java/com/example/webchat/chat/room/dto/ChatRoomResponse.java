package com.example.webchat.chat.room.dto;

import com.example.webchat.chat.room.ChatRoom;
import lombok.Getter;

@Getter
public class ChatRoomResponse {

    private final ChatRoom chatRoom;
    private final long userCount;

    public ChatRoomResponse(ChatRoom chatRoom, long userCount) {
        this.chatRoom = chatRoom;
        this.userCount = userCount;
    }
}
