package com.example.webchat.chat.message;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@Getter
@ToString
public class ChatMessage {

    private String roomId;
    private MessageType messageType;
    private String sender;
    private String message;

    public ChatMessage(String roomId, MessageType messageType, String sender, String message) {
        this.roomId = roomId;
        this.messageType = messageType;
        this.sender = sender;
        this.message = message;
    }

    public String topic() {
        return ChatMessageConst.MESSAGE_TOPIC + this.roomId;
    }

    public String errorTopic() {
        return ChatMessageConst.ERROR_TOPIC;
    }

    public enum MessageType {
        ENTER, QUIT, MESSAGE;
    }
}