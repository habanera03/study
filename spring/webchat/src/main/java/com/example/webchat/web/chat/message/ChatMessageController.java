package com.example.webchat.web.chat.message;

import com.example.webchat.chat.message.ChatMessage;
import com.example.webchat.chat.message.ChatMessage.MessageType;
import com.example.webchat.chat.message.service.ChatMessageService;
import com.example.webchat.web.security.JwtTokenProvider;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

@Controller
public class ChatMessageController {

    private final JwtTokenProvider jwtTokenProvider;
    private final ChatMessageService chatMessageService;

    public ChatMessageController(JwtTokenProvider jwtTokenProvider, ChatMessageService chatMessageService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.chatMessageService = chatMessageService;
    }

    @MessageMapping("/chat/message")
    public void message(ChatMessageRequest chatMessageRequest,
                        @Header("token") String token,
                        @Header("roomId") String roomId) {
        chatMessageService.send(
            new ChatMessage(
                roomId,
                chatMessageRequest.getMessageType(),
                jwtTokenProvider.getUserNameFromJwt(token),
                chatMessageRequest.getMessage()));
    }

    @NoArgsConstructor
    @Getter
    private static class ChatMessageRequest {

        private MessageType messageType;
        private String message;

        public ChatMessageRequest(MessageType messageType, String message) {
            this.messageType = messageType;
            this.message = message;
        }
    }
}
