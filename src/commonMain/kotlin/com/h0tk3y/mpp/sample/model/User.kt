package com.h0tk3y.mpp.sample.model

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: UserId,
    val name: String,
    val email: String,
    val phoneNumber: String,
    var login: String,
    var password: String
)
