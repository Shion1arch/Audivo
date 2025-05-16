package com.mardous.booming.mvvm.event

open class Event<out T>(private val content: T) {

    private var hasBeenConsumed = false

    fun getContentIfNotConsumed(): T? {
        return if (hasBeenConsumed) {
            null
        } else {
            hasBeenConsumed = true
            content
        }
    }

    fun peekContent(): T = content
}
