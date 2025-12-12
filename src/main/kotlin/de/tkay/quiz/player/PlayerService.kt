package de.tkay.quiz.player

import de.tkay.quiz.player.model.Player
import org.springframework.stereotype.Service
import org.springframework.web.socket.WebSocketSession
import java.util.concurrent.ConcurrentHashMap

@Service
class PlayerService {
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

}
