package com.example.webchat.stomp.filter;

import com.example.webchat.chat.message.ChatMessage;
import com.example.webchat.chat.message.service.ChatMessageService;
import com.example.webchat.chat.room.service.ChatRoomService;
import com.example.webchat.web.security.JwtTokenProvider;
import java.security.Principal;
import java.util.Optional;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

@Component
public class FilterChannelInterceptor implements ChannelInterceptor {

    private final JwtTokenProvider jwtTokenProvider;
    private final ChatRoomService chatRoomService;
    private final ChatMessageService chatMessageService;
    private static final String ROOM_ID = "roomId";

    public FilterChannelInterceptor(JwtTokenProvider jwtTokenProvider,
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
            connect(headerAccessor);
        } else if (StompCommand.SUBSCRIBE == headerAccessor.getCommand()
            && Optional.ofNullable(headerAccessor.getDestination()).orElse("").contains("/topic/chat/room/")) {
            String name = Optional.ofNullable((Principal) message.getHeaders().get("simpUser")).map(Principal::getName)
                                  .orElse("UnknownUser");
            chatMessageService.send(
                new ChatMessage(
                    headerAccessor.getFirstNativeHeader("roomId"),
                    ChatMessage.MessageType.ENTER,
                    "",
                    name + "님 입장"));
        }
        return message;
    }

    private void connect(StompHeaderAccessor headerAccessor) {
        String token = headerAccessor.getFirstNativeHeader("token");
        jwtTokenProvider.validateToken(token);
        chatRoomService.addUser(
            headerAccessor.getFirstNativeHeader(ROOM_ID),
            jwtTokenProvider.getUserNameFromJwt(token));
    }

}
