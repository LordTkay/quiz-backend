package de.tkay.quiz.websocket

import de.tkay.quiz.player.PlayerQueryHandshakeInterceptor
import de.tkay.quiz.player.PlayerWebSocketHandler
import de.tkay.quiz.player.model.Player
import org.springframework.context.annotation.Configuration
import org.springframework.web.socket.config.annotation.EnableWebSocket
import org.springframework.web.socket.config.annotation.WebSocketConfigurer
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry

@Configuration
@EnableWebSocket
class WebSocketConfiguration(
    private val playerWebSocketHandler: PlayerWebSocketHandler
) : WebSocketConfigurer {

    override fun registerWebSocketHandlers(registry: WebSocketHandlerRegistry) {
        registry
            .addHandler(playerWebSocketHandler, "/ws/player")
            .addInterceptors(PlayerQueryHandshakeInterceptor())
            .setAllowedOrigins("*")
    }
}
