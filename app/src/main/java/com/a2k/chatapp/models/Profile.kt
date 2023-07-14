package com.a2k.chatapp.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Profile(
    val name: String? = null,
    val email: String? = null,
    val photoUrl: String? = null,
    val uid: String? = null,
) : Parcelable