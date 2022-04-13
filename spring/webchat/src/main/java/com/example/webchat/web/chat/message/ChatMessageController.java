package com.example.webchat.web.chat.message;

import com.example.webchat.chat.message.ChatMessage;
import com.example.webchat.chat.message.ChatMessage.MessageType;
import com.example.webchat.chat.message.service.ChatMessageService;
import com.example.webchat.stomp.StompPrincipal;
import com.example.webchat.util.GlobalConst;
import java.security.Principal;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;

@Controller
public class ChatMessageController {

    private final ChatMessageService chatMessageService;
    private final SimpMessageSendingOperations messagingTemplate;

    public ChatMessageController(
        ChatMessageService chatMessageService,
        SimpMessageSendingOperations messagingTemplate) {

        this.chatMessageService = chatMessageService;
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/chat/message")
    public void message(
        ChatMessageRequest chatMessageRequest,
        @Header("roomId") String roomId,
        StompPrincipal principal) {

        chatMessageService.send(
            new ChatMessage(
                roomId,
                chatMessageRequest.getMessageType(),
                principal.getUserName(),
                chatMessageRequest.getMessage()));
    }

    @MessageExceptionHandler
    public void exceptionHandler(
        Throwable exception,
        @Header("roomId") String roomId,
        Principal principal) {

        ChatMessage chatMessage = new ChatMessage(
            roomId,
            MessageType.MESSAGE,
            GlobalConst.EMPTY,
            exception.getMessage());
        messagingTemplate.convertAndSendToUser(
            principal.getName(),
            chatMessage.errorTopic(),
            chatMessage);
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
