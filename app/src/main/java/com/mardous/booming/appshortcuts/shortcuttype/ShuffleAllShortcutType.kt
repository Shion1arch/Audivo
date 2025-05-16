package com.mardous.booming.appshortcuts.shortcuttype

import android.content.Context
import android.content.pm.ShortcutInfo
import com.mardous.booming.R
import com.mardous.booming.appshortcuts.AppShortcutIconGenerator.generateThemedIcon
import com.mardous.booming.appshortcuts.AppShortcutLauncherActivity


class ShuffleAllShortcutType(context: Context) : BaseShortcutType(context) {

    override val shortcutInfo: ShortcutInfo
        get() = ShortcutInfo.Builder(context, ID)
            .setShortLabel(context.getString(R.string.app_shortcut_shuffle_all_short))
            .setLongLabel(context.getString(R.string.shuffle_all_label))
            .setIcon(generateThemedIcon(context, R.drawable.ic_app_shortcut_shuffle_all))
            .setIntent(getPlaySongsIntent(AppShortcutLauncherActivity.SHORTCUT_TYPE_SHUFFLE_ALL))
            .build()

    companion object {
        const val ID: String = ID_PREFIX + "shuffle_all"
    }
}
