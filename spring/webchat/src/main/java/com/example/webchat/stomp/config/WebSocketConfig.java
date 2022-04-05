package com.example.webchat.stomp.config;

import com.example.webchat.stomp.StompConst;
import com.example.webchat.stomp.filter.FilterChannelInterceptor;
import com.example.webchat.stomp.handler.StompHandshakeHandler;
import com.example.webchat.stomp.interceptor.StompHandshakeInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    private final FilterChannelInterceptor channelInterceptor;
    private final StompHandshakeHandler handshakeHandler;
    private final StompHandshakeInterceptor handshakeInterceptor;

    public WebSocketConfig(
        FilterChannelInterceptor channelInterceptor,
        StompHandshakeHandler handshakeHandler,
        StompHandshakeInterceptor handshakeInterceptor) {

        this.channelInterceptor = channelInterceptor;
        this.handshakeHandler = handshakeHandler;
        this.handshakeInterceptor = handshakeInterceptor;
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint(StompConst.ENDPOINT)
            .setAllowedOriginPatterns(StompConst.ALLOW_ORIGIN_PATTERNS)
            .setHandshakeHandler(handshakeHandler)
            .addInterceptors(handshakeInterceptor)
            .withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.setApplicationDestinationPrefixes(StompConst.APPLICATION_DESTINATION_PREFIX);
        config.enableSimpleBroker(StompConst.SIMPLE_BROKER);
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(channelInterceptor);
    }
}