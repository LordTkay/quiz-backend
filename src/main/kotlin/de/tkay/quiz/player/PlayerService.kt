package de.tkay.quiz.player

import de.tkay.quiz.player.model.Player
import org.springframework.stereotype.Service
import org.springframework.web.socket.WebSocketSession
import java.util.concurrent.ConcurrentHashMap

@Service
class PlayerService {
    private val players = ConcurrentHashMap<String, Player>()

    fun isTaken(username: String): Boolean {
        return players.keys.any { it.equals(username, ignoreCase = true) }
    }

    fun add(username: String, session: WebSocketSession): Player {
        val player = Player(username, session)
        players[username] = player
        return player
    }

    fun remove(username: String) {
        players.remove(username)
    }

}
