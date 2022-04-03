package com.example.webchat.stomp.handler;

import com.example.webchat.stomp.StompPrincipal;
import java.security.Principal;
import java.util.Map;
import java.util.UUID;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

public class StompHandshakeHandler extends DefaultHandshakeHandler {

    @Override
    protected Principal determineUser(ServerHttpRequest request,
                                      WebSocketHandler wsHandler,
                                      Map<String, Object> attributes) {

        return new StompPrincipal(
            UUID.randomUUID().toString(),
            SecurityContextHolder.getContext().getAuthentication().getName());
    }
}