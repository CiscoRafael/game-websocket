package game.socket.mini_rpg.config;

import game.socket.mini_rpg.controller.BatalhaHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    @Autowired
    private BatalhaHandler batalhaHandler;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        // Define o endpoint puro e libera o acesso do Postman
        registry.addHandler(batalhaHandler, "/ws").setAllowedOrigins("*");
    }
}