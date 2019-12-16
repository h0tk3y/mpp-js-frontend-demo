package com.h0tk3y.mpp.sample.model

import kotlinx.serialization.Serializable

@Serializable
data class PersonalChat(
    override val chatId: ChatId,
    val member1: UserId,
    val member2: UserId
) : Chat