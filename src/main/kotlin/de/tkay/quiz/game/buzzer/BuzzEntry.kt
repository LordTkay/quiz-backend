package de.tkay.quiz.game.buzzer

import de.tkay.quiz.player.model.Player

data class BuzzEntry(
    val player: Player,
    val timestamp: Long
)
