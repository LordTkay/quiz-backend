package de.tkay.quiz.game.buzzer

import de.tkay.quiz.gamemaster.event.ResetBuzzersEvent
import de.tkay.quiz.player.event.BuzzEvent
import org.slf4j.LoggerFactory
import org.springframework.context.ApplicationEventPublisher
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Service
import org.springframework.web.socket.TextMessage
import java.util.concurrent.atomic.AtomicReference

@Service
class BuzzerService(
    private val applicationEventPublisher: ApplicationEventPublisher
) {
    private final val logger = LoggerFactory.getLogger(this.javaClass)

    private var queue = AtomicReference<List<BuzzEntry>>(listOf())

    /**
     * Handles the event triggered when a player buzzes. This method processes the player's
     * buzz action by updating the queue, determining their position in the buzz order,
     * and sending feedback to the player via a WebSocket session.
     *
     * @param event The buzz event containing the player who triggered the buzz action.
     */
    @EventListener
    private fun onBuzz(event: BuzzEvent) {

        var queueSize = -1
        var firstEntry: BuzzEntry? = null

        queue.getAndUpdate { currentQueue ->
            if (currentQueue.any { it.player == event.player }) return@getAndUpdate currentQueue
            val updatedQueue = currentQueue + BuzzEntry(event.player, System.currentTimeMillis())
            queueSize = updatedQueue.size
            firstEntry = updatedQueue.firstOrNull()
            updatedQueue
        }

        when (queueSize) {
            -1 -> logger.debug("Player {} has already buzzed!", event.player.username)
            0 -> logger.error("Queue is after a player has buzzed!")
            else -> {
                val message = if (queueSize == 1) {
                    "You buzzed first!"
                } else {
                    val delta = System.currentTimeMillis() - firstEntry!!.timestamp
                    "You are in {$queueSize}th place and were $delta milliseconds late."
                }
                logger.info("Player {} buzzed: {}th place", event.player.username, queueSize)
                event.player.session.sendMessage(TextMessage(message))
                applicationEventPublisher.publishEvent(BuzzQueueChangedEvent(this, queue.get()))
            }
        }
    }

    @EventListener(ResetBuzzersEvent::class)
    private fun onResetBuzzers() {
        queue.set(listOf())
    }
}
