package de.tkay.quiz.game.buzzer

import org.springframework.context.ApplicationEvent

class BuzzQueueChangedEvent(
    source: Any,
    val queue: List<BuzzEntry>
) : ApplicationEvent(source)
