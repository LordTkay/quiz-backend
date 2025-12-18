package de.tkay.quiz.player

import de.tkay.quiz.game.buzzer.BuzzPositionEvent
import de.tkay.quiz.game.buzzer.BuzzQueueChangedEvent
import de.tkay.quiz.player.message.PlayerIncomingMessage
import de.tkay.quiz.player.model.Player
import de.tkay.quiz.websocket.SessionManager
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Service
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession
import java.util.concurrent.ConcurrentHashMap

@Service
class PlayerService : SessionManager {
    private val players = ConcurrentHashMap<String, Player>()

    fun isTaken(username: String): Boolean {
        val normalizedUsername = username.lowercase()

        return players.containsKey(normalizedUsername)
    }

    fun add(username: String, session: WebSocketSession): Player {
        val normalizedUsername = username.lowercase()

        val player = Player(username, session)
        players[normalizedUsername] = player
        return player
    }

    fun remove(username: String) {
        val normalizedUsername = username.lowercase()

        players.remove(normalizedUsername)
    }

    override fun broadcast(message: TextMessage) {
        players.values.forEach { it.session.sendMessage(message) }
    }

    @EventListener
    private fun onBuzzQueueChanged(event: BuzzQueueChangedEvent) {
        if (event.queue.isNotEmpty()) return
        broadcast(TextMessage("Buzz queue is empty!"))
    }

    @EventListener
    private fun onBuzzPosition(event: BuzzPositionEvent) {
        event.player.session.sendMessage(TextMessage("You placed in ${event.position}th with delay of ${event.delay}ms"))
    }

}
