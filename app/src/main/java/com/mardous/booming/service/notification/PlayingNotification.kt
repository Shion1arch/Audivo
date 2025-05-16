
package com.mardous.booming.service.notification

import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import com.mardous.booming.R
import com.mardous.booming.extensions.createNotificationChannel
import com.mardous.booming.extensions.utilities.buildInfoString
import com.mardous.booming.model.Song
import com.mardous.booming.service.MusicService
import com.mardous.booming.util.NotificationExtraText
import com.mardous.booming.util.NotificationPriority
import com.mardous.booming.util.Preferences

abstract class PlayingNotification(protected val context: Context) :
    NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID) {

    abstract fun update(song: Song, onUpdate: () -> Unit)

    abstract fun setPlaying(isPlaying: Boolean)

    @Synchronized
    protected fun getExtraTextString(song: Song): String? {
        if (context is MusicService) {
            return when (Preferences.notificationExtraTextLine) {
                NotificationExtraText.ALBUM_NAME -> song.albumName
                NotificationExtraText.ALBUM_AND_YEAR -> buildInfoString(song.albumName, song.year.toString())
                NotificationExtraText.ALBUM_ARTIST_NAME -> song.albumArtistName
                NotificationExtraText.NEXT_SONG -> context.getQueueInfo(context)
                else -> song.albumName
            }
        }
        return null
    }

    @get:Synchronized
    protected val notificationPriority: Int
        get() = when (Preferences.notificationPriority) {
            NotificationPriority.HIGH -> NotificationCompat.PRIORITY_HIGH
            NotificationPriority.NORMAL -> NotificationCompat.PRIORITY_DEFAULT
            NotificationPriority.LOW -> NotificationCompat.PRIORITY_LOW
            NotificationPriority.MAXIMUM -> NotificationCompat.PRIORITY_MAX
            else -> NotificationCompat.PRIORITY_MAX
        }

    companion object {
        const val NOTIFICATION_ID = 1
        const val NOTIFICATION_CHANNEL_ID = "playing_notification"

        fun createNotificationChannel(context: Context, notificationManager: NotificationManager) {
            context.createNotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                context.getString(R.string.playing_notification_name),
                context.getString(R.string.playing_notification_description),
                notificationManager
            )
        }
    }
}