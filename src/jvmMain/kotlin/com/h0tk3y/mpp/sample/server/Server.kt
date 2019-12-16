package com.h0tk3y.mpp.sample.server

import com.h0tk3y.mpp.sample.api.*
import com.h0tk3y.mpp.sample.model.*
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.ContentNegotiation
import io.ktor.http.HttpStatusCode
import io.ktor.http.content.resource
import io.ktor.http.content.static
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.response.respondRedirect
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.routing
import io.ktor.serialization.serialization
//import io.ktor.serialization.DefaultJsonConfiguration
//import io.ktor.serialization.serialization
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty

fun main() {
    embeddedServer(Netty) {
        install(ContentNegotiation) {
            serialization(json = modelJson)
        }

        routing {
            get(chatListEndpoint.parameterizedPath) {
                call.respond(ChatList(toyChats))
            }
            get(userByIdEndpoint.parameterizedPath) {
                val userId = call.parameters["id"]?.toLongOrNull()
                val user = toyUsers.find { it.id.id == userId }
                if (user != null) {
                    call.respond(user)
                } else {
                    call.response.status(HttpStatusCode.NotFound)
                }
            }
            get(chatMessagesEndpoint.parameterizedPath) {
                val chatId = call.parameters["id"]?.toLongOrNull()
                val chat = toyChats.find { it.chatId.id == chatId }
                if (chat != null) {
                    call.respond(MessageList(messagesByChat.getOrPut(chat.chatId) { mutableListOf() }))
                } else {
                    call.response.status(HttpStatusCode.NotFound)
                }
            }
            post(chatMessagesEndpoint.parameterizedPath) {
                val chatId = call.parameters["id"]?.toLongOrNull()
                val chat = toyChats.find { it.chatId.id == chatId }
                if (chat == null) {
                    call.response.status(HttpStatusCode.NotFound)
                } else {
                    val message = call.receive<Message>()
                    val messages = messagesByChat.getOrPut(chat.chatId) { mutableListOf() }
                    messages.add(message)
                    call.respond(MessageList(listOf()))
                }
            }

            get("/") {
                if (call.request.headers.get("Accept")?.contains("text/html") == true) {
                    call.respondRedirect("static/index.html")
                }
            }
            static("/static") {
                resource("mpp-js-frontend-demo.js")
                resource("index.html")
            }
        }
    }.start(wait = true)
}