
package com.mardous.booming.helper.handler

import android.annotation.SuppressLint
import android.os.Handler
import android.os.Looper
import android.view.MotionEvent
import android.view.View
import android.view.View.OnGenericMotionListener
import android.view.View.OnTouchListener
import com.mardous.booming.service.MusicPlayer
import com.mardous.booming.util.Preferences

class PrevNextButtonOnTouchHandler(direction: Int) : OnTouchListener {

    private val handler: Handler
    private val handlerRunnable: Runnable

    private val genericMotionListener: OnGenericMotionListener
    private var touchedView: View? = null

    private var wasHeld = false

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouch(view: View, motionEvent: MotionEvent): Boolean {
        when (motionEvent.action) {
            MotionEvent.ACTION_DOWN -> {
                handler.removeCallbacks(handlerRunnable)
                handler.postDelayed(handlerRunnable, SKIP_TRIGGER_INITIAL_INTERVAL_MILLIS.toLong())
                touchedView = view
                touchedView!!.isPressed = true
                return true
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                if (!wasHeld) {
                    genericMotionListener.onGenericMotion(touchedView, MotionEvent.obtain(0, 0, MotionEvent.ACTION_CANCEL, 0f, 0f, 0))
                }
                handler.removeCallbacks(handlerRunnable)
                touchedView!!.isPressed = false
                touchedView = null
                wasHeld = false
                return true
            }
        }
        return false
    }

    init {
        val fastForward = Preferences.fastForward
        val fastBackward = Preferences.fastBackward

        genericMotionListener = OnGenericMotionListener { _: View?, motionEvent: MotionEvent ->
            when (motionEvent.action) {
                MotionEvent.ACTION_DOWN -> {
                    if (fastForward || fastBackward) {
                        var seek = MusicPlayer.songProgressMillis
                        if (direction == DIRECTION_NEXT) {
                            seek += PLAYBACK_SKIP_AMOUNT_MILLI
                        } else if (direction == DIRECTION_PREVIOUS) {
                            seek -= PLAYBACK_SKIP_AMOUNT_MILLI
                        }
                        if (direction == DIRECTION_NEXT && fastForward || direction == DIRECTION_PREVIOUS && fastBackward) {
                            MusicPlayer.seekTo(seek)
                        }
                    }
                    return@OnGenericMotionListener true
                }
                MotionEvent.ACTION_CANCEL -> {
                    if (direction == DIRECTION_NEXT) {
                        MusicPlayer.playNextSong()
                    } else if (direction == DIRECTION_PREVIOUS) {
                        MusicPlayer.back()
                    }
                    return@OnGenericMotionListener true
                }
            }
            false
        }

        handler = Handler(Looper.getMainLooper())
        handlerRunnable = object : Runnable {
            override fun run() {
                if (touchedView!!.isEnabled) {
                    wasHeld = true
                    handler.postDelayed(this, SKIP_TRIGGER_NORMAL_INTERVAL_MILLIS.toLong())
                    genericMotionListener.onGenericMotion(touchedView, MotionEvent.obtain(0, 0, MotionEvent.ACTION_DOWN, 0f, 0f, 0))
                } else {
                    // if the view was disabled by the clickListener, remove the callback
                    handler.removeCallbacks(this)
                    touchedView!!.isPressed = false
                    touchedView = null
                    wasHeld = false
                }
            }
        }
    }

    companion object {
        private const val PLAYBACK_SKIP_AMOUNT_MILLI = 3500
        private const val SKIP_TRIGGER_INITIAL_INTERVAL_MILLIS = 1000
        private const val SKIP_TRIGGER_NORMAL_INTERVAL_MILLIS = 250

        const val DIRECTION_NEXT = 1
        const val DIRECTION_PREVIOUS = 2
    }
}