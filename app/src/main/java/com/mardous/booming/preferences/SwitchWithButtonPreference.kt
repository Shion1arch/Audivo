
package com.mardous.booming.preferences

import android.content.Context
import android.util.AttributeSet
import android.widget.Button
import androidx.preference.PreferenceViewHolder
import androidx.preference.SwitchPreferenceCompat
import com.mardous.booming.R


class SwitchWithButtonPreference @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = androidx.preference.R.attr.switchPreferenceCompatStyle,
    defStyleRes: Int = 0
) : SwitchPreferenceCompat(context, attrs, defStyleAttr, defStyleRes) {

    interface OnButtonPressedListener {
        fun onButtonPressed()
    }

    private var button: Button? = null
    private var listener: OnButtonPressedListener? = null

    override fun onBindViewHolder(holder: PreferenceViewHolder) {
        super.onBindViewHolder(holder)
        button = holder.findViewById(R.id.button) as? Button
        button?.setOnClickListener {
            listener?.onButtonPressed()
        }
    }

    override fun onDetached() {
        super.onDetached()
        button = null
        listener = null
    }

    fun setButtonPressedListener(listener: OnButtonPressedListener) {
        this.listener = listener
    }
}