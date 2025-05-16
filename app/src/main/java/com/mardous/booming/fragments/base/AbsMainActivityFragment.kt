
package com.mardous.booming.fragments.base

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.core.view.isGone
import androidx.core.view.updateLayoutParams
import androidx.lifecycle.Lifecycle
import androidx.navigation.navOptions
import com.mardous.booming.R
import com.mardous.booming.activities.MainActivity
import com.mardous.booming.extensions.applyWindowInsets
import com.mardous.booming.extensions.dip
import com.mardous.booming.extensions.isLandscape
import com.mardous.booming.fragments.LibraryViewModel
import org.koin.androidx.viewmodel.ext.android.activityViewModel


abstract class AbsMainActivityFragment @JvmOverloads constructor(@LayoutRes layoutRes: Int = 0) :
    AbsMusicServiceFragment(layoutRes), MenuProvider {

    val libraryViewModel: LibraryViewModel by activityViewModel()

    protected val mainActivity: MainActivity
        get() = requireActivity() as MainActivity

    protected val navOptions by lazy {
        navOptions {
            launchSingleTop = false
            anim {
                enter = R.anim.booming_fragment_open_enter
                exit = R.anim.booming_fragment_open_exit
                popEnter = R.anim.booming_fragment_close_enter
                popExit = R.anim.booming_fragment_close_exit
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.STARTED)
    }

    protected fun applyWindowInsetsFromView(view: View) {
        view.applyWindowInsets(
            left = isLandscape() && mainActivity.navigationView.isGone, right = true, bottom = true
        )
    }

    protected fun checkForMargins(view: View) {
        if (mainActivity.isBottomNavVisible) {
            view.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                bottomMargin = dip(R.dimen.bottom_nav_height)
            }
        }
    }
}