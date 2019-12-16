package com.h0tk3y.mpp.sample.client

import com.h0tk3y.mpp.sample.model.*
import kotlinx.html.js.onClickFunction
import react.RBuilder
import react.RComponent
import react.RProps
import react.RState
import react.dom.h3
import react.dom.p

interface ChatListProps : RProps {
    var chatList: ChatList
    var usersMap: Map<UserId, User>
    var currentChat: Chat?
    var onSelectChat: (Chat) -> Unit
}

fun RBuilder.chatList(
    chats: ChatList,
    usersMap: Map<UserId, User>,
    currentChat: Chat?,
    onSelectChat: (Chat) -> Unit
) = child(ChatListComponent::class) {
    attrs.chatList = chats
    attrs.usersMap = usersMap
    attrs.currentChat = currentChat
    attrs.onSelectChat = onSelectChat
}

class ChatListComponent(props: ChatListProps): RComponent<ChatListProps, RState>(props) {
    override fun RBuilder.render() {
        h3 { +"Chats: "}
        for (chat in props.chatList.chats) {
            p {
                if (chat == props.currentChat) {
                    +"▶ "
                }
                attrs.onClickFunction = {
                    props.onSelectChat(chat)
                }
                renderChatTitle("chatList", chat, props.usersMap)
            }
        }
    }
}