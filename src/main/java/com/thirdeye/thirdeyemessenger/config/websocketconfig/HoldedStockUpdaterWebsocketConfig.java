package com.thirdeye.thirdeyemessenger.config.websocketconfig;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class HoldedStockUpdaterWebsocketConfig implements WebSocketMessageBrokerConfigurer {
	@Value("${frontend.urls}")
    private String[] frontendUrls;
    
	@Override
    public void configureMessageBroker(MessageBrokerRegistry config)
    {
    	config.enableSimpleBroker("/holdedstockviewer");
    }
	
	@Override
    public void registerStompEndpoints(StompEndpointRegistry registry)
    {
    	registry.addEndpoint("/holdedstockviewersubs")
    	.setAllowedOrigins(frontendUrls)
    	.withSockJS();
    }
}

