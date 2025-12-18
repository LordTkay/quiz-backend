package de.tkay.quiz.websocket

import de.tkay.quiz.gamemaster.GamemasterWebSocketHandler
import de.tkay.quiz.player.PlayerQueryHandshakeInterceptor
import de.tkay.quiz.player.PlayerWebSocketHandler
import de.tkay.quiz.screen.ScreenWebSocketHandler
import de.tkay.quiz.player.model.Player
import org.springframework.context.annotation.Configuration
import org.springframework.web.socket.config.annotation.EnableWebSocket
import org.springframework.web.socket.config.annotation.WebSocketConfigurer
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry

@Configuration
@EnableWebSocket
class WebSocketConfiguration(
    private val playerWebSocketHandler: PlayerWebSocketHandler,
    private val gamemasterWebSocketHandler: GamemasterWebSocketHandler,
    private val screenWebSocketHandler: ScreenWebSocketHandler
) : WebSocketConfigurer {

    override fun registerWebSocketHandlers(registry: WebSocketHandlerRegistry) {
        registry
            .addHandler(playerWebSocketHandler, "/ws/player")
            .addInterceptors(PlayerQueryHandshakeInterceptor())
            .setAllowedOrigins("*")

        registry
            .addHandler(gamemasterWebSocketHandler, "/ws/gamemaster")
            .setAllowedOrigins("*")

        registry
            .addHandler(screenWebSocketHandler, "/ws/screen")
            .setAllowedOrigins("*")
    }
}
