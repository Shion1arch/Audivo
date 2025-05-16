
package com.mardous.booming.interfaces

import androidx.recyclerview.widget.RecyclerView
import com.mardous.booming.model.Suggestion

interface IHomeCallback {
    fun createSuggestionAdapter(suggestion: Suggestion): RecyclerView.Adapter<*>?
    fun suggestionClick(suggestion: Suggestion)
}
