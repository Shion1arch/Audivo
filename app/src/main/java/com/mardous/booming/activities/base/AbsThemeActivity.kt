
package com.mardous.booming.activities.base

import android.graphics.Color.TRANSPARENT
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.SystemBarStyle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewGroupCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.google.android.material.color.DynamicColors
import com.google.android.material.color.DynamicColorsOptions
import com.mardous.booming.R
import com.mardous.booming.extensions.createAppTheme
import com.mardous.booming.extensions.hasQ
import com.mardous.booming.extensions.resources.isColorLight
import com.mardous.booming.extensions.resources.surfaceColor
import com.mardous.booming.util.Preferences

abstract class AbsThemeActivity : AppCompatActivity() {

    private var windowInsetsController: WindowInsetsControllerCompat? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        updateTheme()
        enableEdgeToEdge(navigationBarStyle = SystemBarStyle.auto(TRANSPARENT, TRANSPARENT))
        super.onCreate(savedInstanceState)
        windowInsetsController = WindowCompat.getInsetsController(window, window.decorView)
        if (hasQ()) {
            window.isNavigationBarContrastEnforced = false
            window.decorView.isForceDarkAllowed = false
        }
        ViewGroupCompat.installCompatInsetsDispatch(window.decorView)
    }

    private fun updateTheme() {
        val appTheme = createAppTheme()
        setTheme(appTheme.themeRes)
        if (appTheme.hasSeedColor) {
            DynamicColors.applyToActivityIfAvailable(this,
                DynamicColorsOptions.Builder()
                    .setContentBasedSource(appTheme.seedColor)
                    .setOnAppliedCallback {
                        if (appTheme.isBlackTheme) {
                            setTheme(R.style.BlackThemeOverlay)
                        }
                    }
                    .build()
            )
        }
        if (Preferences.isCustomFont) {
            setTheme(R.style.ManropeThemeOverlay)
        }
    }

    protected open fun postRecreate() {
        // hack to prevent java.lang.RuntimeException: Performing pause of activity that is not resumed
        // makes sure recreate() is called right after and not in onResume()
        Handler(Looper.getMainLooper()).post { recreate() }
    }

    fun setLightStatusBar(lightStatusBar: Boolean = surfaceColor().isColorLight) {
        windowInsetsController?.isAppearanceLightStatusBars = lightStatusBar
    }

    fun setLightNavigationBar(lightNavigationBar: Boolean = surfaceColor().isColorLight) {
        windowInsetsController?.isAppearanceLightNavigationBars = lightNavigationBar
    }

    override fun onDestroy() {
        super.onDestroy()
        windowInsetsController = null
    }
}