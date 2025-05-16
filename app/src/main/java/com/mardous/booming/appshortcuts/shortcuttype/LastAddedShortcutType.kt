package com.mardous.booming.appshortcuts.shortcuttype

import android.content.Context
import android.content.pm.ShortcutInfo
import com.mardous.booming.R
import com.mardous.booming.appshortcuts.AppShortcutIconGenerator.generateThemedIcon
import com.mardous.booming.appshortcuts.AppShortcutLauncherActivity

class LastAddedShortcutType(context: Context) : BaseShortcutType(context) {

    override val shortcutInfo: ShortcutInfo
        get() = ShortcutInfo.Builder(context, ID)
            .setShortLabel(context.getString(R.string.app_shortcut_last_added_short))
            .setLongLabel(context.getString(R.string.last_added_label))
            .setIcon(generateThemedIcon(context, R.drawable.ic_app_shortcut_last_added))
            .setIntent(getPlaySongsIntent(AppShortcutLauncherActivity.SHORTCUT_TYPE_LAST_ADDED))
            .build()

    companion object {
        const val ID: String = ID_PREFIX + "last_added"
    }
}
