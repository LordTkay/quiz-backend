package de.tkay.quiz.player.event

import de.tkay.quiz.player.model.Player
import org.springframework.context.ApplicationEvent

class BuzzingEvent(
    source: Any,
    val player: Player
) : ApplicationEvent(source)
