package com.example.webchat.stomp.filter;

import com.example.webchat.chat.message.ChatMessage;
import com.example.webchat.chat.message.ChatMessage.MessageType;
import com.example.webchat.chat.message.ChatMessageConst;
import com.example.webchat.chat.message.service.ChatMessageService;
import com.example.webchat.chat.room.service.ChatRoomService;
import com.example.webchat.stomp.StompConst;
import com.example.webchat.stomp.StompPrincipal;
import java.util.Objects;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class FilterChannelInterceptor implements ChannelInterceptor {

    private final ChatRoomService chatRoomService;
    private final ChatMessageService chatMessageService;

    public FilterChannelInterceptor(
        ChatRoomService chatRoomService,
        ChatMessageService chatMessageService) {

        this.chatRoomService = chatRoomService;
        this.chatMessageService = chatMessageService;
    }

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(message);

        switch (Objects.requireNonNull(headerAccessor.getCommand())) {
            case CONNECT -> {
                connection(headerAccessor);
            }
            case SUBSCRIBE -> {
                subscribe(Objects.requireNonNull(headerAccessor.getDestination()),
                    createChatMessage(
                        message,
                        headerAccessor.getFirstNativeHeader(StompConst.ROOM_ID),
                        MessageType.ENTER));
            }
            case DISCONNECT -> {
                disconnect(createChatMessage(
                    message,
                    getStompPrincipal(headerAccessor).getRoomId(),
                    MessageType.QUIT));
            }
        }

        return message;
    }

    private void connection(StompHeaderAccessor headerAccessor) {
        StompPrincipal stompPrincipal = getStompPrincipal(headerAccessor);
        stompPrincipal.setRoomId(headerAccessor.getFirstNativeHeader(StompConst.ROOM_ID));
        stompPrincipal.setUserName(Optional.ofNullable(
                headerAccessor.getFirstNativeHeader(StompConst.USER_NAME))
            .orElse(StompConst.UNKNOWN_USER));

        chatRoomService.addUser(headerAccessor.getFirstNativeHeader(StompConst.ROOM_ID),
            getStompPrincipal(headerAccessor).getUserName());
    }

    private void subscribe(String destination, ChatMessage message) {
        if (isErrorSubscribe(destination)) {
            return;
        }
        chatMessageService.send(message);
    }

    private boolean isErrorSubscribe(String destination) {
        return destination.contains(ChatMessageConst.ERROR_TOPIC);
    }

    private void disconnect(ChatMessage message) {
        chatMessageService.send(message);
    }

    private ChatMessage createChatMessage(
        Message<?> message,
        String roomId,
        ChatMessage.MessageType messageType) {

        return new ChatMessage(
            roomId,
            messageType,
            "",
            getStompPrincipal(message).getUserName() + messageType.getDefaultMessage());
    }

    private StompPrincipal getStompPrincipal(StompHeaderAccessor headerAccessor) {
        return (StompPrincipal) Objects.requireNonNull(headerAccessor.getUser());
    }

    private StompPrincipal getStompPrincipal(Message<?> message) {
        return (StompPrincipal) message.getHeaders().get(StompConst.SIMP_USER);
    }
}
