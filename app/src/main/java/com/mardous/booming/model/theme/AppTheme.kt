
package com.mardous.booming.model.theme

import android.content.Context
import android.graphics.Color
import android.os.Build
import androidx.annotation.ColorInt
import androidx.annotation.StyleRes
import androidx.core.content.ContextCompat
import com.mardous.booming.R
import com.mardous.booming.util.GeneralTheme
import com.mardous.booming.util.Preferences

class AppTheme private constructor(
    val id: String,
    @StyleRes
    val themeRes: Int,
    @ColorInt
    val seedColor: Int = EMPTY_PRIMARY_COLOR
) {

    val isBlackTheme: Boolean
        get() = id == GeneralTheme.BLACK

    val hasSeedColor: Boolean
        get() = DYNAMIC_COLOR_SUPPORTED && seedColor != EMPTY_PRIMARY_COLOR

    enum class Mode(@StyleRes val themeRes: Int) {
        Light(R.style.Theme_Booming_Light),
        Dark(R.style.Theme_Booming),
        Black(R.style.Theme_Booming_Black),
        FollowSystem(R.style.Theme_Booming_FollowSystem)
    }

    companion object {
        private val DYNAMIC_COLOR_SUPPORTED = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
        private const val EMPTY_PRIMARY_COLOR = Color.TRANSPARENT

        fun createAppTheme(context: Context): AppTheme {
            val generalTheme = Preferences.generalTheme
            val themeMode = Preferences.getThemeMode(generalTheme)
            if (DYNAMIC_COLOR_SUPPORTED) {
                val themeRes = when (generalTheme) {
                    GeneralTheme.BLACK -> R.style.Theme_Booming_DynamicColors_Black
                    else -> R.style.Theme_Booming_DynamicColors
                }
                if (Preferences.materialYou) {
                    return AppTheme(generalTheme, themeRes)
                }
                return AppTheme(
                    generalTheme,
                    themeRes,
                    ContextCompat.getColor(context, R.color.md_theme_primary)
                )
            }
            return AppTheme(generalTheme, themeMode.themeRes)
        }
    }
}