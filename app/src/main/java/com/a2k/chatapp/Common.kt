package com.a2k.chatapp

import android.content.Context
import android.view.Window
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import com.google.android.material.appbar.MaterialToolbar


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
fun setupUI(context: Context, window: Window, toolbar: MaterialToolbar) {
    window.navigationBarDividerColor =
        ContextCompat.getColor(context, android.R.color.transparent)
    window.navigationBarColor = ContextCompat.getColor(context, android.R.color.transparent)
    window.statusBarColor = ContextCompat.getColor(context, android.R.color.transparent)
    WindowCompat.setDecorFitsSystemWindows(window, false)

    ViewCompat.setOnApplyWindowInsetsListener(toolbar) { view, windowInsets ->
        val insets = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars())
        view.updatePadding(top = insets.top, bottom = insets.bottom, left = insets.left, right = insets.right)
        WindowInsetsCompat.CONSUMED
    }
}

/**
 *  setupUIWithNavigationListener is used to provide a immersive experience for people who
 *  are using gesture mode and provide custom behaviours on when Navigation button is used
 *  in the app.
 *
 *  It also makes sure that app is drawing behind the system components
 *  such as Status Bar and Navigation Bar.
 *
 *  @param listener takes a function that you want to be called when Navigation Icon is press.
 *  @param context takes android context
 *  @param window takes window to modify status bar, etc
 *  @param toolbar toolbar view to update its padding based on the device.
 * */
fun setupUIWithNavigationListener(context: Context, window: Window, toolbar: MaterialToolbar, listener : () -> Unit) {
    setupUI(context, window, toolbar)
    toolbar.setNavigationOnClickListener {
        listener()
    }
}