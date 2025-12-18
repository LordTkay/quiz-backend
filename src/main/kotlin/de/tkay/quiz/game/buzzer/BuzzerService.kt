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
     * Handles an incoming buzz event by adding the player to the buzz queue if they haven't already buzzed
     * and notifies them of their position in the queue. If it's the first buzz, they are informed accordingly.
     * Additionally, this method publishes a `BuzzQueueChangedEvent` to notify listeners of the updated queue.
     *
     * @param event The `BuzzEvent` triggered when a player buzzes, containing details of the player who buzzed.
     */
    @EventListener
    private fun onBuzz(event: BuzzEvent) {
        var updated = false;

        val updatedQueue = queue.updateAndGet { currentQueue ->
            if (currentQueue.any { it.player == event.player }) return@updateAndGet currentQueue
            updated = true
            currentQueue + BuzzEntry(event.player, System.currentTimeMillis())
        }

        if (!updated) {
            logger.debug("Player {} has already buzzed!", event.player.username)
            return
        }

        val message = if (updatedQueue.size == 1) {
            "You buzzed first!"
        } else {
            val firstEntry = updatedQueue.first()
            val delta = System.currentTimeMillis() - firstEntry.timestamp
            "You are in ${updatedQueue.size}th place and were $delta milliseconds late."
        }
        logger.info("Player {} buzzed: {}th place", event.player.username, updatedQueue.size)
        event.player.session.sendMessage(TextMessage(message))
        applicationEventPublisher.publishEvent(BuzzQueueChangedEvent(this, queue.get()))
    }

    @EventListener(ResetBuzzersEvent::class)
    private fun onResetBuzzers() {
        queue.set(listOf())
    }
}
