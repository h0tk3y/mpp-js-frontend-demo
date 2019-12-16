package com.h0tk3y.mpp.sample.api

import com.h0tk3y.mpp.sample.model.*
import kotlin.reflect.KClass

val chatListEndpoint = ApiEndpoint<ChatList, EmptyParameters>("/chats", ChatList::class)
val userByIdEndpoint = ApiEndpoint<User, SingleParameter<UserId>>("/user/{id}", User::class)

val chatMessagesEndpoint = ApiEndpoint<MessageList, SingleParameter<ChatId>>(
    "/messages/{id}",
    MessageList::class
)

// ---

interface ApiParameters {
    fun get(name: String): String?
}

private class MapApiParameters(val baseMap: Map<String, Any>) :
    ApiParameters {
    override fun get(name: String): String? = baseMap[name]?.toString()
}

data class SingleParameter<T>(val value: T) : ApiParameters {
    override fun get(name: String): String? = value.toString()
}

data class ApiEndpoint<TResult : Any, TParams : ApiParameters>(
    val parameterizedPath: String,
    val resultType: KClass<TResult>
)

fun <TParams : ApiParameters> ApiEndpoint<*, TParams>.path(parameters: TParams) =
    parameterizedPath.replace("\\{(\\w+)}".toRegex()) {
        parameters.get(it.groupValues[1]).toString()
    }

object EmptyParameters : ApiParameters by MapApiParameters(mapOf())

