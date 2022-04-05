package com.example.webchat.stomp.filter;

import com.example.webchat.chat.message.ChatMessage;
import com.example.webchat.chat.message.ChatMessageConst;
import com.example.webchat.chat.message.service.ChatMessageService;
import com.example.webchat.chat.room.service.ChatRoomService;
import com.example.webchat.stomp.StompConst;
import com.example.webchat.stomp.StompPrincipal;
import com.example.webchat.util.GlobalConst;
import com.example.webchat.web.security.JwtTokenProvider;
import java.util.Optional;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

@Component
public class FilterChannelInterceptor implements ChannelInterceptor {

    private final ChatRoomService chatRoomService;
    private final ChatMessageService chatMessageService;
    private final JwtTokenProvider jwtTokenProvider;

    public FilterChannelInterceptor(
        JwtTokenProvider jwtTokenProvider,
        ChatRoomService chatRoomService,
        ChatMessageService chatMessageService) {

        this.jwtTokenProvider = jwtTokenProvider;
        this.chatRoomService = chatRoomService;
        this.chatMessageService = chatMessageService;
    }

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(message);
        if (StompCommand.CONNECT == headerAccessor.getCommand()) {
            connect(getName(message), getUserName(message), headerAccessor);
        } else if (StompCommand.SUBSCRIBE == headerAccessor.getCommand()
            && Optional.ofNullable(headerAccessor.getDestination()).orElse(GlobalConst.EMPTY)
                       .contains(ChatMessageConst.MESSAGE_TOPIC)) {
            chatSubscribe(getUserName(message), headerAccessor);
        }
        return message;
    }

    private void connect(String name, String userName, StompHeaderAccessor headerAccessor) {
        jwtTokenProvider.validateToken(name);
        chatRoomService.addUser(
            headerAccessor.getFirstNativeHeader(StompConst.ROOM_ID),
            userName);
    }

    private void chatSubscribe(String userName, StompHeaderAccessor headerAccessor) {
        chatMessageService.send(
            new ChatMessage(
                headerAccessor.getFirstNativeHeader(StompConst.ROOM_ID),
                ChatMessage.MessageType.ENTER,
                GlobalConst.EMPTY,
                userName + "님 입장"));
    }

    private String getUserName(Message<?> message) {
        return Optional.ofNullable(getStompPrincipal(message))
                       .map(StompPrincipal::getUserName)
                       .orElse(StompConst.UNKNOWN_USER);
    }

    private String getName(Message<?> message) {
        return Optional.ofNullable(getStompPrincipal(message))
                       .map(StompPrincipal::getName)
                       .orElse(StompConst.UNKNOWN_USER);
    }

    private StompPrincipal getStompPrincipal(Message<?> message) {
        return (StompPrincipal) message.getHeaders().get(StompConst.SIMP_USER);
    }

}
