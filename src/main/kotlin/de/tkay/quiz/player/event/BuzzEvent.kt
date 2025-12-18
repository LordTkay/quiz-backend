package de.tkay.quiz.player.event

import de.tkay.quiz.player.model.Player
import org.springframework.context.ApplicationEvent

class BuzzEvent(
    source: Any,
    val player: Player
) : ApplicationEvent(source)
