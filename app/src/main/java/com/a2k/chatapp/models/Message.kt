package com.a2k.chatapp.models

import com.google.firebase.firestore.ServerTimestamp
import java.util.Date

data class Message(
    val messageBody: String? = null,
    val senderId: String? = null,
    @ServerTimestamp
    val sentDate: Date? = null
)
