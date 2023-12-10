package com.chatus.config;

import com.chatus.beans.MyInterceptor;
import com.chatus.beans.WebSocketCustomHandshakeHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.*;
@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    private final WebSocketCustomHandshakeHandler webSocketCustomHandshakeHandler;
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")
                .setAllowedOriginPatterns("*")
                .setHandshakeHandler(this.webSocketCustomHandshakeHandler)
                .withSockJS();
        registry.addEndpoint("/ws")
                .setAllowedOriginPatterns("*")
                .setHandshakeHandler(this.webSocketCustomHandshakeHandler);
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.setApplicationDestinationPrefixes("/app");
        registry.enableSimpleBroker("/queue");
        registry.setUserDestinationPrefix("/user");
    }

    //    @Override
//    public void configureClientInboundChannel(ChannelRegistration registration) {
//        registration.interceptors(new MyInterceptor());
//    }
}
