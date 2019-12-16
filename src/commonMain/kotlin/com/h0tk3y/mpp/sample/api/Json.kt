package com.h0tk3y.mpp.sample.api

import com.h0tk3y.mpp.sample.model.Chat
import com.h0tk3y.mpp.sample.model.GroupChat
import com.h0tk3y.mpp.sample.model.PersonalChat
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import kotlinx.serialization.modules.SerializersModule

private val requestsJsonContext = SerializersModule {
    polymorphic(Chat::class) {
        PersonalChat::class with PersonalChat.serializer()
        GroupChat::class with GroupChat.serializer()
    }
}

val modelJson = Json(
    configuration = JsonConfiguration.Stable.copy(useArrayPolymorphism = true),
    context = requestsJsonContext
)