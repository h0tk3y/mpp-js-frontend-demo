package com.h0tk3y.mpp.sample.client

import com.h0tk3y.mpp.sample.model.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import react.*
import react.dom.hr

class App : RComponent<RProps, AppState>() {
    override fun RBuilder.render(): Unit = with(state) {
        chatList(chatList, usersMap, currentChat) { selectedChat ->
            setState { currentChat = selectedChat }
        }
        state.currentChat?.let {
            hr { }
            chat(it, usersMap)
        }
    }

    override fun AppState.init() {
        chatList = ChatList(listOf())
        usersMap = emptyMap()

        GlobalScope.launch {
            val chats = getChats()

            val userIds = chats.chats.flatMap {
                when (it) {
                    is PersonalChat -> listOf(it.member1, it.member2)
                    is GroupChat -> it.userIds
                    else -> error("unexpected chat $it")
                }
            }

            val users = userIds.map { async { it to getUserById(it) } }.awaitAll().toMap()

            setState {
                chatList = chats
                usersMap = users
            }
        }
    }
}

interface AppState : RState {
    var chatList: ChatList
    var usersMap: Map<UserId, User>
    var currentChat: Chat?
}