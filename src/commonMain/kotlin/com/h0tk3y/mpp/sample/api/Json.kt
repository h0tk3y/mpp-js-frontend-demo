package com.h0tk3y.mpp.sample.api

import com.h0tk3y.mpp.sample.model.Chat
import com.h0tk3y.mpp.sample.model.GroupChat
import com.h0tk3y.mpp.sample.model.PersonalChat
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule

private val requestsJsonContext = SerializersModule {
    polymorphic(Chat::class, PersonalChat::class, PersonalChat.serializer())
    polymorphic(Chat::class, GroupChat::class, GroupChat.serializer())
}

val modelJson = Json {
    serializersModule = requestsJsonContext
    useArrayPolymorphism = true
}