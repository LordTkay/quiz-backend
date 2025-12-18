package de.tkay.quiz.game.buzzer

import org.springframework.context.ApplicationEvent

class BuzzQueueChangedEvent(
    source: Any,
    queue: List<BuzzEntry>
) : ApplicationEvent(source)
