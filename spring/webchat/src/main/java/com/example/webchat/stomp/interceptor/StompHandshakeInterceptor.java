package com.example.webchat.stomp.interceptor;

import java.util.Arrays;
import java.util.Map;
import javax.servlet.http.Cookie;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

@Component
@Slf4j
public class StompHandshakeInterceptor implements HandshakeInterceptor {

    @Override
    public boolean beforeHandshake(
        ServerHttpRequest request, ServerHttpResponse response,
        WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        Cookie[] cookies = ((ServletServerHttpRequest) request).getServletRequest().getCookies();
        Arrays.stream(cookies)
            .forEach(cookie -> log.info("cookie {} : {}", cookie.getName(), cookie.getValue()));

        ((ServletServerHttpRequest) request)
            .getServletRequest()
            .setAttribute("cookies", cookies);

        attributes.put("cookies", cookies);

        return true;
    }

    @Override
    public void afterHandshake(
        ServerHttpRequest request, ServerHttpResponse response,
        WebSocketHandler wsHandler, Exception exception) {

    }
}
