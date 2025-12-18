package de.tkay.quiz.gamemaster

import de.tkay.quiz.websocket.SessionManager
import org.springframework.stereotype.Service
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession
import java.util.concurrent.CopyOnWriteArrayList

@Service
class GamemasterService : SessionManager {
    private val gamemasterList = CopyOnWriteArrayList<WebSocketSession>()

    fun add(session: WebSocketSession) {
        gamemasterList.add(session)
    }

    fun remove(session: WebSocketSession) {
        gamemasterList.remove(session)
    }

    override fun broadcast(message: TextMessage) {
        gamemasterList.forEach { it.sendMessage(message) }
    }


}
