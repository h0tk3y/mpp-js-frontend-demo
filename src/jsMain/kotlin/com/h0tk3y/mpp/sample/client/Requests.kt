package com.h0tk3y.mpp.sample.client

import com.h0tk3y.mpp.sample.api.*
import com.h0tk3y.mpp.sample.model.*
import io.ktor.client.HttpClient
import io.ktor.client.call.call
import io.ktor.client.engine.js.Js
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.http.ContentType
import io.ktor.http.Url
import io.ktor.http.contentType
import kotlinx.browser.window

private val client = HttpClient(Js) {
    install(JsonFeature) { serializer = KotlinxSerializer(modelJson) }
}

private fun endpointUrl(path: String): Url {
    val baseUrl = window.location.href.removeSuffix(window.location.pathname)
    return Url("$baseUrl/$path")
}

private inline suspend fun <reified T : Any, TParam : ApiParameters> requestEndpoint(
    endpoint: ApiEndpoint<T, TParam>,
    parameters: TParam
): T {
    val url = endpointUrl(endpoint.path(parameters))
    return client.get(url)
}

suspend fun getChats(): ChatList =
    requestEndpoint(
        chatListEndpoint,
        EmptyParameters
    )

suspend fun getUserById(userId: UserId): User =
    requestEndpoint(
        userByIdEndpoint,
        SingleParameter(userId)
    )

suspend fun getChatMessages(chatId: ChatId): MessageList =
    requestEndpoint(chatMessagesEndpoint, SingleParameter(chatId))

suspend fun sendChatMessage(chatId: ChatId, message: Message): Boolean {
    val url = endpointUrl(chatMessagesEndpoint.path(SingleParameter(chatId)))
    try {
        client.post<Any>(url) {
            contentType(ContentType.Application.Json)
            body = message
        }
        return true
    } catch (e: Exception) {
        return false
    }
}