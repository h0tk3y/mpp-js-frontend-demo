package com.h0tk3y.mpp.sample.client

import com.h0tk3y.mpp.sample.model.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.html.InputType
import kotlinx.html.currentTimeMillis
import kotlinx.html.js.onChangeFunction
import kotlinx.html.js.onClickFunction
import kotlinx.html.js.onKeyDownFunction
import org.w3c.dom.HTMLInputElement
import react.*
import react.dom.*

interface ChatProps : RProps {
    var chat: Chat
    var usersMap: Map<UserId, User>
}

interface ChatState : RState {
    var messageList: MessageList
    var currentText: String
}

fun RBuilder.chat(
    chat: Chat,
    usersMap: Map<UserId, User>
) = child(ChatComponent::class) {
    attrs.chat = chat
    attrs.usersMap = usersMap
}

class ChatComponent(props: ChatProps) : RComponent<ChatProps, ChatState>(props) {
    override fun ChatState.init(props: ChatProps) {
        messageList = MessageList(emptyList())
        currentText = ""
        cycleUpdateJob = GlobalScope.launch {
            while (true) {
                if (updateJob == null) {
                    updateMessages()
                }
                delay(2000)
            }
        }
    }

    private var cycleUpdateJob: Job? = null
    private var updateJob: Job? = null

    fun updateMessages(resetList: Boolean = false) {
        if (resetList) {
            setState { messageList = MessageList(emptyList()) }
        }
        updateJob = GlobalScope.launch {
            try {
                val messagesResult = getChatMessages(props.chat.chatId)
                setState {
                    messageList = messagesResult
                }
            } catch (t: Throwable) {
                console.log("Couldn't reach the backend: $t")
            }

            updateJob = null
        }
    }

    override fun componentDidMount() {
        updateMessages(true)
    }

    override fun componentWillReceiveProps(nextProps: ChatProps) {
        val currentChat = if (props !== undefined) props.chat else null
        if (nextProps.chat.chatId != currentChat?.chatId) {
            state.currentText = ""
            updateMessages(true)
        }
    }

    override fun componentWillUnmount() {
        cycleUpdateJob?.cancel()
        updateJob?.cancel()
    }

    override fun RBuilder.render() {
        val chat = props.chat
        h3 { renderChatTitle("chatTitle", chat, props.usersMap) }
        if (chat is GroupChat) {
            small {
                +"In this chat: ${chat.userIds.joinToString { props.usersMap.getValue(it).name }}"
            }
        }
        state.messageList.messages.forEach {
            p { +"${props.usersMap.getValue(it.from).name}: ${it.text}" }
        }
        input(type = InputType.text) {
            attrs {
                key = "input"
                value = state.currentText
                size = "100"
                placeholder = "Write something..."
                onKeyDownFunction = { event ->
                    val e = event.asDynamic()
                    if (
                        (e.ctrlKey as Boolean || e.metaKey as Boolean) &&
                        e.keyCode == 13 && state.currentText.isNotBlank()
                    ) {
                        sendMessage()
                    }
                }
                onChangeFunction = {
                    val target = it.target as HTMLInputElement
                    setState {
                        currentText = target.value
                    }
                }
            }
        }
        button {
            key = "btnSend"
            +"Send"
            attrs.disabled = state.currentText.isBlank()
            attrs.onClickFunction = {
                sendMessage()
            }
        }
    }

    private fun sendMessage() {
        GlobalScope.launch {
            val result = sendChatMessage(
                props.chat.chatId, Message(
                    MessageId(currentTimeMillis()),
                    UserId(0),
                    props.chat.chatId,
                    state.currentText,
                    0
                )
            )
            if (result) {
                setState {
                    currentText = ""
                }
            }
            updateMessages()
        }
    }
}

internal fun RBuilder.renderChatTitle(keyPrefix: String, chat: Chat, usersMap: Map<UserId, User>) =
    span {
        key = keyPrefix + "/" + chat.chatId
        when (chat) {
            is PersonalChat -> {
                val name = usersMap.getValue(chat.member2).name
                +name
                small { +" (private chat)" }
            }
            is GroupChat -> {
                +chat.chatName
                small { +" (${chat.userIds.size} participants)" }
            }
            else -> error("invalid chat $chat")
        }
    }
