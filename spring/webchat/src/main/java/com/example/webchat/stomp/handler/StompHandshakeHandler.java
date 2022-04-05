package com.example.webchat.stomp.handler;

import com.example.webchat.stomp.StompPrincipal;
import com.example.webchat.web.security.JwtTokenProvider;
import java.security.Principal;
import java.util.Arrays;
import java.util.Map;
import javax.servlet.http.Cookie;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

@Component
@Slf4j
public class StompHandshakeHandler extends DefaultHandshakeHandler {

    private final JwtTokenProvider jwtTokenProvider;

    public StompHandshakeHandler(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    protected Principal determineUser(
        ServerHttpRequest request,
        WebSocketHandler wsHandler,
        Map<String, Object> attributes) {

        Arrays.stream((Cookie[]) ((ServletServerHttpRequest) request).getServletRequest().getAttribute("cookies"))
            .forEach(cookie -> log.info("request attribute cookie {} : {}", cookie.getName(), cookie.getValue()));

        Arrays.stream((Cookie[]) attributes.get("cookies"))
            .forEach(cookie -> log.info("attributes cookie {} : {}", cookie.getName(), cookie.getValue()));

        String name = SecurityContextHolder.getContext().getAuthentication().getName();

        return new StompPrincipal(jwtTokenProvider.generateToken(name), name);
    }
}