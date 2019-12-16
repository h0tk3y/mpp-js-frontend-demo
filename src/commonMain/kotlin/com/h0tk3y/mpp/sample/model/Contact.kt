package com.h0tk3y.mpp.sample.model

import kotlinx.serialization.Serializable

@Serializable
class Contact(
    val id: Id,
    val name: String
) {
    override fun equals(other: Any?): Boolean =
        (other as? Contact)?.id == id

    override fun hashCode(): Int = id.hashCode()
}