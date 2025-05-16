
package com.mardous.booming.extensions

import android.app.Activity
import android.content.Intent
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import androidx.core.content.getSystemService

fun Activity.keepScreenOn(keepScreenOn: Boolean) {
    if (keepScreenOn) {
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    } else {
        window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
    }
}

fun Activity.hideSoftKeyboard() {
    val currentFocus = currentFocus
    if (currentFocus != null) {
        (getSystemService<InputMethodManager>())
            ?.hideSoftInputFromWindow(currentFocus.windowToken, 0)
    }
}

@Suppress("DEPRECATION")
inline fun <reified T : Any> Activity.extra(key: String, default: T? = null) = lazy {
    val value = intent?.extras?.get(key)
    value as? T ?: default
}

@Suppress("DEPRECATION")
inline fun <reified T : Any> Intent.extra(key: String, default: T? = null) = lazy {
    val value = extras?.get(key)
    value as? T ?: default
}

@Suppress("DEPRECATION")
inline fun <reified T : Any> Activity.extraNotNull(key: String, default: T? = null) = lazy {
    val value = intent?.extras?.get(key)
    requireNotNull(value as? T ?: default) { key }
}

inline val Activity.rootView: View get() = findViewById<ViewGroup>(android.R.id.content).getChildAt(0)