package de.tkay.quiz.gamemaster


import de.tkay.quiz.gamemaster.event.ResetBuzzersEvent
import de.tkay.quiz.gamemaster.message.GamemasterIncomingMessage
import kotlinx.serialization.json.Json
import org.slf4j.LoggerFactory
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Component
import org.springframework.web.socket.CloseStatus
import org.springframework.web.socket.WebSocketMessage
import org.springframework.web.socket.WebSocketSession
import org.springframework.web.socket.handler.TextWebSocketHandler

@Component
class GamemasterWebSocketHandler(
    private val gamemasterService: GamemasterService,
    private val applicationEventPublisher: ApplicationEventPublisher
) : TextWebSocketHandler() {
    private final val logger = LoggerFactory.getLogger(javaClass)

    override fun afterConnectionEstablished(session: WebSocketSession) {
        gamemasterService.add(session)
        logger.info("Gamemaster connected: {}", session.id)
    }

    override fun afterConnectionClosed(
        session: WebSocketSession,
        status: CloseStatus
    ) {
        gamemasterService.remove(session)
        logger.info("Gamemaster disconnected: {}", session.id)
    }

    override fun handleMessage(session: WebSocketSession, message: WebSocketMessage<*>) {
        val incomingMessage = Json.decodeFromString<GamemasterIncomingMessage>(message.payload.toString())
        when (incomingMessage) {
            is GamemasterIncomingMessage.ResetBuzzers -> applicationEventPublisher.publishEvent(ResetBuzzersEvent(this))
        }

        logger.debug("Gamemaster Event {}: {}", incomingMessage.javaClass.name, session.id)
    }
}
