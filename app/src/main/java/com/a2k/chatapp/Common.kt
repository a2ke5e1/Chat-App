package com.a2k.chatapp

import android.content.Context
import android.view.ViewGroup.MarginLayoutParams
import android.view.Window
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updateLayoutParams
import androidx.core.view.updatePadding
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.CollapsingToolbarLayout
import com.google.android.material.appbar.MaterialToolbar
import java.security.MessageDigest


/**
 *  setup UI is used to provide a immersive experience for people who
 *  are using gesture mode.
 *
 *  It makes sure that app is drawing behind the system components
 *  such as Status Bar and Navigation Bar.
 *
 *  @param context takes android context
 *  @param window takes window to modify status bar, etc
 *  @param toolbar toolbar view to update its padding based on the device.
 * */
fun setupUI(context: Context, window: Window, appBarLayout: AppBarLayout, isCollapsingToolbarLayout: Boolean = true) {
    window.navigationBarDividerColor =
        ContextCompat.getColor(context, android.R.color.transparent)
    window.navigationBarColor = ContextCompat.getColor(context, android.R.color.transparent)
    window.statusBarColor = ContextCompat.getColor(context, android.R.color.transparent)
    WindowCompat.setDecorFitsSystemWindows(window, false)

    ViewCompat.setOnApplyWindowInsetsListener(appBarLayout) { view, windowInsets ->
        val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())
        if (isCollapsingToolbarLayout) {
            view.updateLayoutParams<MarginLayoutParams> {
                topMargin = insets.top
            }
        } else {
            view.updatePadding(top = insets.top)
        }
        WindowInsetsCompat.CONSUMED
    }
}



/** It is used to generate a same unique chat id, even when two uids are interchanged when
 *  two functions are called.
 *
 *  @param senderUid Sender's uid
 *  @param receiverUid Receiver's uid
 */
fun generateChatId(senderUid: String, receiverUid: String): String {
    // uniqueString joins senderUid and receiverUid such that
    // generated string is same even both are interchanged.
    val uniqueString = listOf(senderUid, receiverUid).sorted().joinToString("")

    // We are running a SHA-256 hash algorithm on uniqueString
    // to ensure that no one can regenerate user's uid from
    // the uniqueString.
    val md = MessageDigest.getInstance("SHA-256")
    val digest = md.digest(uniqueString.toByteArray())
    return digest.fold("") { str, it -> str + "%02x".format(it) }
}