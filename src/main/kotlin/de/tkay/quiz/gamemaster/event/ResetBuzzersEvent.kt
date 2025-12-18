package de.tkay.quiz.gamemaster.event

import org.springframework.context.ApplicationEvent

class ResetBuzzersEvent(
    source: Any
) : ApplicationEvent(source)
