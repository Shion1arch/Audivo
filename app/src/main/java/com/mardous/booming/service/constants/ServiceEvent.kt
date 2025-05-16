
package com.mardous.booming.service.constants

import com.mardous.booming.BuildConfig

class ServiceEvent {
    companion object {
        const val BOOMING_PACKAGE_NAME = BuildConfig.APPLICATION_ID
        const val MUSIC_PACKAGE_NAME = "com.android.music"
        const val PLAY_STATE_CHANGED = "$BOOMING_PACKAGE_NAME.playstatechanged"
        const val META_CHANGED = "$BOOMING_PACKAGE_NAME.metachanged"
        const val QUEUE_CHANGED = "$BOOMING_PACKAGE_NAME.queuechanged"
        const val REPEAT_MODE_CHANGED = "$BOOMING_PACKAGE_NAME.repeatmodechanged"
        const val SHUFFLE_MODE_CHANGED = "$BOOMING_PACKAGE_NAME.shufflemodechanged"
        const val MEDIA_STORE_CHANGED = "$BOOMING_PACKAGE_NAME.mediastorechanged"
        const val FAVORITE_STATE_CHANGED = "$BOOMING_PACKAGE_NAME.favoritestatechanged"
    }
}