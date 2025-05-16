
package com.mardous.booming.preferences.dialog

import android.content.Context
import android.util.AttributeSet
import androidx.preference.DialogPreference

class CategoriesPreference @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = androidx.preference.R.attr.dialogPreferenceStyle,
    defStyleRes: Int = 0
) : DialogPreference(context, attrs, defStyleAttr, defStyleRes)