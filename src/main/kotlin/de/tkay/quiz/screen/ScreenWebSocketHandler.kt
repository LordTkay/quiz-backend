package de.tkay.quiz.screen


import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.web.socket.CloseStatus
import org.springframework.web.socket.WebSocketMessage
import org.springframework.web.socket.WebSocketSession
import org.springframework.web.socket.handler.TextWebSocketHandler

@Component
class ScreenWebSocketHandler(
    private val screenService: ScreenService
) : TextWebSocketHandler() {
    private final val logger = LoggerFactory.getLogger(javaClass)

    override fun afterConnectionEstablished(session: WebSocketSession) {
        screenService.add(session)
        logger.info("Screen connected: {}", session.id)
    }

    override fun afterConnectionClosed(
        session: WebSocketSession,
        status: CloseStatus
    ) {
        screenService.remove(session)
        logger.info("Screen disconnected: {}", session.id)
    }

    override fun handleMessage(session: WebSocketSession, message: WebSocketMessage<*>) {
        logger.debug("Screen Event {}: {}", "None", session.id)
    }
}
