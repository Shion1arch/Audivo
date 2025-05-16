
package com.mardous.booming.fragments.player

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.TimeInterpolator
import android.view.View
import androidx.core.animation.doOnStart
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import java.util.LinkedList

abstract class PlayerAnimator(val isEnabled: Boolean) {
    private var musicControllerAnimationSet: AnimatorSet? = null
    var isPrepared = false
        private set

    fun start() {
        if (!isPrepared) return

        if (musicControllerAnimationSet == null) {
            val interpolator = FastOutSlowInInterpolator()
            val animators = LinkedList<Animator>()

            onAddAnimation(animators, interpolator)

            musicControllerAnimationSet = AnimatorSet().apply {
                doOnStart {
                    onAnimationStarted()
                }
                playTogether(animators)
            }
        } else {
            musicControllerAnimationSet!!.cancel()
        }

        if (isEnabled) {
            musicControllerAnimationSet!!.start()
            isPrepared = false
        }
    }

    fun prepare() {
        if (isEnabled) {
            onPrepareForAnimation()

            musicControllerAnimationSet?.cancel()
            isPrepared = true
        }
    }

    protected fun addScaleAnimation(
        animators: MutableCollection<Animator>,
        view: View,
        interpolator: TimeInterpolator?,
        delay: Int
    ) {
        val scaleX = ObjectAnimator.ofFloat(view, View.SCALE_X, 0f, 1f).apply {
            this.interpolator = interpolator
            this.duration = 300
            this.startDelay = delay.toLong()
        }
        animators.add(scaleX)

        val scaleY = ObjectAnimator.ofFloat(view, View.SCALE_Y, 0f, 1f).apply {
            this.interpolator = interpolator
            this.duration = 300
            this.startDelay = delay.toLong()
        }
        animators.add(scaleY)
    }

    protected fun prepareForScaleAnimation(view: View?) {
        view?.scaleX = 0f
        view?.scaleY = 0f
    }

    open fun onAnimationStarted() {}

    /**
     * Called to allow implementors to add their custom animations to
     * the given *animators* list.
     *
     * Implementors are free to add any animation and/or interpolator they believe more
     * convenient for each case, however, there is a convenience method called [addScaleAnimation]
     * which adds a simple alpha animation and a default [TimeInterpolator] is given..
     */
    open fun onAddAnimation(animators: LinkedList<Animator>, interpolator: TimeInterpolator) {}
    open fun onPrepareForAnimation() {}
}