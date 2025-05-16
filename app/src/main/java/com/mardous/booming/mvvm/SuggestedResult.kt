
package com.mardous.booming.mvvm

import com.mardous.booming.model.Suggestion

data class SuggestedResult(val state: State = State.Idle, val data: List<Suggestion> = arrayListOf()) {

    val isLoading: Boolean
        get() = state == State.Loading

    enum class State {
        Ready,
        Loading,
        Idle
    }

    companion object {
        val Idle = SuggestedResult(State.Idle)
    }
}