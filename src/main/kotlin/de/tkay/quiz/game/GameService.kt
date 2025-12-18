package de.tkay.quiz.game

import de.tkay.quiz.gamemaster.event.ResetBuzzersEvent
import de.tkay.quiz.player.event.BuzzEvent
import de.tkay.quiz.player.model.Player
import org.slf4j.LoggerFactory
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Service
import org.springframework.web.socket.TextMessage

@Service
class GameService {
    private final val logger = LoggerFactory.getLogger(this.javaClass)

    private var activeBuzzer: Player? = null

    @EventListener
    private fun onBuzz(event: BuzzEvent) {
        if (activeBuzzer != null) {
            event.player.session.sendMessage(TextMessage("Another player already buzzed!"))
            return
        }
        val player = event.player
        activeBuzzer = player

        event.player.session.sendMessage(TextMessage("Buzzer activated!"))
        logger.info("Buzzer activated: {}", player.username)
    }

    @EventListener(ResetBuzzersEvent::class)
    private fun onResetBuzzers() {
        activeBuzzer = null
    }

}
