package de.tkay.quiz.player.model

import org.springframework.web.socket.WebSocketSession

data class Player(
    val username: String,
    val session: WebSocketSession
) {
}
