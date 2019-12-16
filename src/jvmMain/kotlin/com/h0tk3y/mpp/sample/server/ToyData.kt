package com.h0tk3y.mpp.sample.server

import com.h0tk3y.mpp.sample.model.*

internal val toyUsers = listOf(
    User(UserId(0), "John Doe", "johndoe@gmail.com", "3120574", "johndoe", "..."),
    User(UserId(1), "Jane Dae", "janedae@spbsu.edu.ru", "3131743", "janedae", "..."),
    User(UserId(2), "Larry Loe", "larryloe@hotmail.com", "3132345", "larryloe", "..."),
    User(UserId(3), "Marry Moe", "marrymoe@yandex.com", "33312345", "marrymoe", "...")
)

internal val toyChats = mutableListOf<Chat>(
    PersonalChat(ChatId(0), toyUsers[0].id, toyUsers[1].id),
    PersonalChat(ChatId(1), toyUsers[0].id, toyUsers[2].id),
    PersonalChat(ChatId(2), toyUsers[0].id, toyUsers[3].id),
    GroupChat(ChatId(3), toyUsers[0].id, "Friday Bar Plans", "none", toyUsers.map { it.id }),
    GroupChat(ChatId(4), toyUsers[1].id, "Project Bottom", "none", toyUsers.take(3).map { it.id })
)

internal var nextMessageId = 0L
    get() = field++

internal val messagesByChat = mutableMapOf<ChatId, MutableList<Message>>(
    toyChats[0].chatId to mutableListOf(
        Message(MessageId(nextMessageId), toyUsers[0].id, toyChats[0].chatId, "Hey!", 0),
        Message(MessageId(nextMessageId), toyUsers[1].id, toyChats[0].chatId, "Hello!", 0)
    ),
    toyChats[1].chatId to mutableListOf(
        Message(MessageId(nextMessageId), toyUsers[0].id, toyChats[0].chatId, "Hi!", 0),
        Message(MessageId(nextMessageId), toyUsers[2].id, toyChats[0].chatId, "Uhm, who is this?", 0)
    ),
    toyChats[2].chatId to mutableListOf(
        Message(MessageId(nextMessageId), toyUsers[3].id, toyChats[0].chatId, "Hey John!", 0),
        Message(MessageId(nextMessageId), toyUsers[0].id, toyChats[0].chatId, "Hi Marry", 0),
        Message(MessageId(nextMessageId), toyUsers[0].id, toyChats[0].chatId, "How are you?", 0)
    ),
    toyChats[3].chatId to mutableListOf(
        Message(MessageId(nextMessageId), toyUsers[3].id, toyChats[0].chatId, "How about some beer this Friday?", 0),
        Message(MessageId(nextMessageId), toyUsers[0].id, toyChats[0].chatId, "No way", 0),
        Message(MessageId(nextMessageId), toyUsers[1].id, toyChats[0].chatId, "Sorry, I'm busy", 0),
        Message(MessageId(nextMessageId), toyUsers[2].id, toyChats[0].chatId, "Can't go, my dog is giving birth", 0)
    ),
    toyChats[4].chatId to mutableListOf(
        Message(MessageId(nextMessageId), toyUsers[2].id, toyChats[0].chatId, "Hi guys, I'm your new manager", 0),
        Message(MessageId(nextMessageId), toyUsers[0].id, toyChats[0].chatId, "Where did the previous one go?", 0),
        Message(MessageId(nextMessageId), toyUsers[1].id, toyChats[0].chatId, "Oh, not again...", 0)
    )
)