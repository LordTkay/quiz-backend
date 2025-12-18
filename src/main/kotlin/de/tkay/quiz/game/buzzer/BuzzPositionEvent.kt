package de.tkay.quiz.game.buzzer

import de.tkay.quiz.player.model.Player
import org.springframework.context.ApplicationEvent

class BuzzPositionEvent(
    source: Any,
    val player: Player,
    val position: Int,
    val delay: Long
) : ApplicationEvent(source)
