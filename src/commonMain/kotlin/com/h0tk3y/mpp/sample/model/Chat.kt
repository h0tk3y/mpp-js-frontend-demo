package com.h0tk3y.mpp.sample.model

import kotlinx.serialization.Serializable

interface Chat {
    val chatId: ChatId
}

@Serializable
data class ChatList(
    val chats: List<Chat>
)
