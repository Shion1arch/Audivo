
package com.mardous.booming.service

import android.database.ContentObserver
import android.os.Handler
import com.mardous.booming.service.constants.ServiceEvent

open class MediaStoreObserver(private val service: MusicService, private val uiHandler: Handler) :
    ContentObserver(uiHandler), Runnable {

    override fun onChange(selfChange: Boolean) {
        // if a change is detected, remove any scheduled callback
        // then post a new one. This is intended to prevent closely
        // spaced events from generating multiple refresh calls
        uiHandler.removeCallbacks(this)
        uiHandler.postDelayed(this, REFRESH_DELAY)
    }

    override fun run() {
        // actually call refresh when the delayed callback fires
        // do not send a sticky broadcast here
        service.handleAndSendChangeInternal(ServiceEvent.MEDIA_STORE_CHANGED)
    }

    companion object {
        // milliseconds to delay before calling refresh to aggregate events
        private const val REFRESH_DELAY: Long = 500
    }
}
