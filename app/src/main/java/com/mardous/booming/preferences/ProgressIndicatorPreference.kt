
package com.mardous.booming.preferences

import android.content.Context
import android.util.AttributeSet
import androidx.preference.Preference
import androidx.preference.PreferenceViewHolder
import com.google.android.material.progressindicator.CircularProgressIndicator

class ProgressIndicatorPreference @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = androidx.preference.R.attr.preferenceStyle
) : Preference(context, attrs, defStyleAttr) {

    private var progressIndicator: CircularProgressIndicator? = null
    private var shouldShowProgress = false

    override fun onBindViewHolder(holder: PreferenceViewHolder) {
        super.onBindViewHolder(holder)
        progressIndicator = holder.findViewById(android.R.id.progress) as? CircularProgressIndicator
        applyProgressVisibility()
    }

    override fun onPrepareForRemoval() {
        super.onPrepareForRemoval()
        progressIndicator = null
    }

    fun showProgressIndicator() {
        shouldShowProgress = true
        applyProgressVisibility()
    }

    fun hideProgressIndicator() {
        shouldShowProgress = false
        applyProgressVisibility()
    }

    private fun applyProgressVisibility() {
        progressIndicator?.let {
            if (shouldShowProgress) {
                it.show()
            } else {
                it.hide()
            }
        }
    }
}
