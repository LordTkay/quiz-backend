package de.tkay.quiz.player.message

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator

@OptIn(ExperimentalSerializationApi::class)
@Serializable
@JsonClassDiscriminator("type")
sealed class PlayerIncomingMessage {

    @Serializable
    @SerialName("BUZZ")
    data object Buzz : PlayerIncomingMessage()

}
