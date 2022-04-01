package com.example.webchat.stomp.filter;

import com.example.webchat.web.security.JwtTokenProvider;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

@Component
public class FilterChannelInterceptor implements ChannelInterceptor {

    private final JwtTokenProvider jwtTokenProvider;

    public FilterChannelInterceptor(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(message);
        if (StompCommand.CONNECT.equals(headerAccessor.getCommand())) {
            jwtTokenProvider.validateToken(headerAccessor.getFirstNativeHeader("token"));
        }
        return message;
    }
}
