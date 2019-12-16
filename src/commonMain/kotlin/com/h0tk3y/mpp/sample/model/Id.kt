package com.h0tk3y.mpp.sample.model

import kotlinx.serialization.Serializable

@Serializable
data class Id(val id: Long)

@Serializable
data class UserId(val id: Long) {
    override fun toString(): String = "$id"
}

@Serializable
data class ChatId(val id: Long) {
    override fun toString(): String = "$id"
}

@Serializable
data class MessageId(val id: Long)