
package com.mardous.booming.service.constants

interface ServiceAction {
    interface Extras {
        companion object {
            const val EXTRA_PLAYLIST = "${ServiceEvent.BOOMING_PACKAGE_NAME}.extra.playlist"
            const val EXTRA_SHUFFLE_MODE = ServiceEvent.BOOMING_PACKAGE_NAME + ".extra.shufflemode"
            const val EXTRA_APP_WIDGET_NAME = "${ServiceEvent.BOOMING_PACKAGE_NAME}.app_widget_name"
        }
    }

    companion object {
        const val ACTION_TOGGLE_PAUSE = "${ServiceEvent.BOOMING_PACKAGE_NAME}.togglepause"
        const val ACTION_PLAY = "${ServiceEvent.BOOMING_PACKAGE_NAME}.play"
        const val ACTION_PLAY_PLAYLIST = "${ServiceEvent.BOOMING_PACKAGE_NAME}.play.playlist"
        const val ACTION_PAUSE = "${ServiceEvent.BOOMING_PACKAGE_NAME}.pause"
        const val ACTION_STOP = "${ServiceEvent.BOOMING_PACKAGE_NAME}.stop"
        const val ACTION_QUIT = "${ServiceEvent.BOOMING_PACKAGE_NAME}.quit"
        const val ACTION_PENDING_QUIT = "${ServiceEvent.BOOMING_PACKAGE_NAME}.pendingquit"
        const val ACTION_NEXT = "${ServiceEvent.BOOMING_PACKAGE_NAME}.next"
        const val ACTION_PREVIOUS = "${ServiceEvent.BOOMING_PACKAGE_NAME}.previous"
        const val ACTION_APP_WIDGET_UPDATE = "${ServiceEvent.BOOMING_PACKAGE_NAME}.appwidgetupdate"
    }
}