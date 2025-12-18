package de.tkay.quiz.player

import de.tkay.quiz.player.event.BuzzEvent
import de.tkay.quiz.player.message.PlayerIncomingMessage
import de.tkay.quiz.player.model.Player
import kotlinx.serialization.json.Json
import org.slf4j.LoggerFactory
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Component
import org.springframework.web.socket.CloseStatus
import org.springframework.web.socket.WebSocketMessage
import org.springframework.web.socket.WebSocketSession
import org.springframework.web.socket.handler.TextWebSocketHandler

@Component
class PlayerWebSocketHandler(
    private val playerService: PlayerService,
    private val applicationEventPublisher: ApplicationEventPublisher
) : TextWebSocketHandler() {
    private final val logger = LoggerFactory.getLogger(javaClass)

    override fun afterConnectionEstablished(session: WebSocketSession) {
        val username = session.attributes["username"] as String

        if (playerService.isTaken(username)) {
            logger.warn("Player {} tried to connect, but this username is already connected!", username)
            session.close(CloseStatus.NOT_ACCEPTABLE)
            return
        }

        session.attributes["player"] = playerService.add(username, session)
        logger.info("Player connected: {} ({})", username, session.id)
    }

    override fun afterConnectionClosed(
        session: WebSocketSession,
        status: CloseStatus
    ) {
        if (status == CloseStatus.NOT_ACCEPTABLE) return

        val username = session.attributes["username"] as String
        playerService.remove(username)
        logger.info("Player disconnected: {} ({})", username, session.id)
    }

    override fun handleMessage(session: WebSocketSession, message: WebSocketMessage<*>) {
        val player = session.attributes["player"] as Player

        val incomingMessage = Json.decodeFromString<PlayerIncomingMessage>(message.payload.toString())
        when (incomingMessage) {
            is PlayerIncomingMessage.Buzz -> applicationEventPublisher.publishEvent(BuzzEvent(this, player))
        }

        logger.debug("Player Event {}: {} ({})", incomingMessage.javaClass.name, player.username, session.id)
    }
}
