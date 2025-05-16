
package com.mardous.booming.preferences

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.CheckedTextView
import androidx.core.content.res.ResourcesCompat
import androidx.preference.Preference
import androidx.preference.PreferenceViewHolder
import com.mardous.booming.R
import com.mardous.booming.util.GeneralTheme
import com.mardous.booming.util.Preferences


class ThemePreference @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = androidx.preference.R.attr.preferenceStyle
) : Preference(context, attrs, defStyleAttr), View.OnClickListener {

    var customCallback: Callback? = null
    private var widgetView: View? = null
    private val selectorIds = hashMapOf(
        GeneralTheme.LIGHT to R.id.light_theme,
        GeneralTheme.DARK to R.id.dark_theme,
        GeneralTheme.AUTO to R.id.system_default
    )

    override fun onBindViewHolder(holder: PreferenceViewHolder) {
        super.onBindViewHolder(holder)
        holder.itemView.background = null

        val selectedTheme = Preferences.generalTheme
        widgetView = holder.findViewById(android.R.id.widget_frame)
        selectorIds.forEach { (theme, id) ->
            val textView = holder.findViewById(id) as CheckedTextView
            textView.setOnClickListener(this)
            textView.isEnabled = (selectedTheme != GeneralTheme.BLACK)
            textView.isChecked = (theme == selectedTheme)
            textView.typeface = if (textView.isChecked) {
                ResourcesCompat.getFont(context, R.font.manrope_semibold)
            } else {
                ResourcesCompat.getFont(context, R.font.manrope_regular)
            }
        }
    }

    override fun onPrepareForRemoval() {
        super.onPrepareForRemoval()
        customCallback = null
        widgetView = null
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.light_theme -> customCallback?.onThemeSelected(GeneralTheme.LIGHT)
            R.id.dark_theme -> customCallback?.onThemeSelected(GeneralTheme.DARK)
            R.id.system_default -> customCallback?.onThemeSelected(GeneralTheme.AUTO)
        }
    }

    interface Callback {
        fun onThemeSelected(themeName: String)
    }
}
