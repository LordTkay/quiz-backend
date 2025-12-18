package de.tkay.quiz.screen

import de.tkay.quiz.game.buzzer.BuzzQueueChangedEvent
import de.tkay.quiz.websocket.SessionManager
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Service
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession
import java.util.concurrent.CopyOnWriteArrayList

@Service
class ScreenService : SessionManager {
    private val screenList = CopyOnWriteArrayList<WebSocketSession>()

    fun add(session: WebSocketSession) {
        screenList.add(session)
    }

    fun remove(session: WebSocketSession) {
        screenList.remove(session)
    }

    override fun broadcast(message: TextMessage) {
        screenList.forEach { it.sendMessage(message) }
    }

    @EventListener
    private fun onBuzzQueueChanged(event: BuzzQueueChangedEvent) {
        broadcast(TextMessage("Buzz queue changed: ${event.queue.joinToString { it.player.username }}"))
    }

}
