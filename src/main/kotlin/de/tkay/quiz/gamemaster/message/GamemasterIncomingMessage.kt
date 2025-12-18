package de.tkay.quiz.gamemaster.message

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonClassDiscriminator

@OptIn(ExperimentalSerializationApi::class)
@Serializable
@JsonClassDiscriminator("type")
sealed class GamemasterIncomingMessage {

    @Serializable
    @SerialName("RESET_BUZZERS")
    data object ResetBuzzers : GamemasterIncomingMessage()

}
