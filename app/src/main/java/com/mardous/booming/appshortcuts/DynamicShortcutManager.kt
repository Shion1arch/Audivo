package com.mardous.booming.appshortcuts

import android.content.Context
import android.content.pm.ShortcutInfo
import android.content.pm.ShortcutManager
import androidx.core.content.getSystemService
import com.mardous.booming.appshortcuts.shortcuttype.LastAddedShortcutType
import com.mardous.booming.appshortcuts.shortcuttype.ShuffleAllShortcutType
import com.mardous.booming.appshortcuts.shortcuttype.TopTracksShortcutType


class DynamicShortcutManager(private val context: Context) {

    private val shortcutManager: ShortcutManager? = context.getSystemService()

    private val defaultShortcuts: List<ShortcutInfo>
        get() = listOf(
            ShuffleAllShortcutType(context).shortcutInfo,
            TopTracksShortcutType(context).shortcutInfo,
            LastAddedShortcutType(context).shortcutInfo
        )

    fun initDynamicShortcuts() {
        if (shortcutManager == null)
            return

        if (shortcutManager.dynamicShortcuts.isEmpty()) {
            shortcutManager.dynamicShortcuts = defaultShortcuts
        }
    }

    fun updateDynamicShortcuts() {
        shortcutManager?.updateShortcuts(defaultShortcuts)
    }

    companion object {
        fun reportShortcutUsed(context: Context, shortcutId: String) {
            context.getSystemService(ShortcutManager::class.java).reportShortcutUsed(shortcutId)
        }
    }
}
