
package com.mardous.booming.fragments.settings

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.mardous.booming.R
import com.mardous.booming.databinding.FragmentSettingsBinding
import com.mardous.booming.extensions.applyHorizontalWindowInsets
import com.mardous.booming.extensions.getOnBackPressedDispatcher
import com.mardous.booming.fragments.base.AbsMainActivityFragment

class SettingsFragment : AbsMainActivityFragment(R.layout.fragment_settings), NavController.OnDestinationChangedListener {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    private var childNavController: NavController? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentSettingsBinding.bind(view)
        with(binding.appBarLayout.toolbar) {
            setNavigationIcon(R.drawable.ic_back_24dp)
            isTitleCentered = false
            setNavigationOnClickListener {
                getOnBackPressedDispatcher().onBackPressed()
            }
        }

        view.applyHorizontalWindowInsets()

        val navHostFragment = childFragmentManager.findFragmentById(R.id.contentFrame) as NavHostFragment
        childNavController = navHostFragment.navController.apply {
            addOnDestinationChangedListener(this@SettingsFragment)
        }
        getOnBackPressedDispatcher().addCallback(viewLifecycleOwner, onBackPressedCallback)
    }

    override fun onDestinationChanged(controller: NavController, destination: NavDestination, arguments: Bundle?) {
        binding.appBarLayout.title = destination.label ?: getString(R.string.settings_title)
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {}

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean = false

    override fun onDestroy() {
        super.onDestroy()
        childNavController?.removeOnDestinationChangedListener(this)
    }

    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            if (mainActivity.panelState != BottomSheetBehavior.STATE_COLLAPSED) {
                mainActivity.collapsePanel()
                return
            }
            if (childNavController?.popBackStack() == false) {
                remove()
                getOnBackPressedDispatcher().onBackPressed()
                return
            }
        }
    }
}