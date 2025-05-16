package com.mardous.booming.appshortcuts.shortcuttype

import android.content.Context
import android.content.Intent
import android.content.pm.ShortcutInfo
import androidx.core.os.bundleOf
import com.mardous.booming.appshortcuts.AppShortcutLauncherActivity


abstract class BaseShortcutType(val context: Context) {

    abstract val shortcutInfo: ShortcutInfo?

    /**
     * Creates an Intent that will launch MainActivity and immediately play {@param songs} in either shuffle or normal mode
     *
     * @param shortcutType Describes the type of shortcut to create (ShuffleAll, TopTracks, custom playlist, etc.)
     */
    fun getPlaySongsIntent(shortcutType: Int): Intent {
        val intent = Intent(context, AppShortcutLauncherActivity::class.java)
        intent.setAction(Intent.ACTION_VIEW)
        val b = bundleOf(AppShortcutLauncherActivity.KEY_SHORTCUT_TYPE to shortcutType)
        intent.putExtras(b)
        return intent
    }

    companion object {
        internal const val ID_PREFIX: String = "com.mardous.booming.appshortcuts.id."
    }
}
