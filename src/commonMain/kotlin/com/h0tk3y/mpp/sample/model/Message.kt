package com.h0tk3y.mpp.sample.model

import kotlinx.serialization.Serializable

@Serializable
data class Message(
    val messageId: MessageId,
    val from: UserId,
    val chat: ChatId,
    var text: String,
    val time: Int,
    var isDeleted: Boolean = false,
    var isEdited: Boolean = false
)

@Serializable
class MessageList(val messages: List<Message>)