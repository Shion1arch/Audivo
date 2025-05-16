
package com.mardous.booming.preferences.dialog

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.mardous.booming.R
import com.mardous.booming.databinding.PreferenceDialogNowPlayingScreenBinding
import com.mardous.booming.databinding.PreferenceNowPlayingScreenItemBinding
import com.mardous.booming.extensions.dp
import com.mardous.booming.model.theme.NowPlayingScreen
import com.mardous.booming.util.Preferences

class NowPlayingScreenPreferenceDialog : DialogFragment(), ViewPager.OnPageChangeListener {

    private var viewPagerPosition = 0

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val binding = PreferenceDialogNowPlayingScreenBinding.inflate(layoutInflater)

        binding.nowPlayingScreenViewPager.adapter = NowPlayingScreenAdapter(context)
        binding.nowPlayingScreenViewPager.addOnPageChangeListener(this)
        binding.nowPlayingScreenViewPager.pageMargin = 32.dp(resources)
        binding.nowPlayingScreenViewPager.currentItem = Preferences.nowPlayingScreen.ordinal

        return MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.now_playing_screen_title)
            .setView(binding.root)
            .setPositiveButton(android.R.string.ok) { _: DialogInterface, _: Int ->
                Preferences.nowPlayingScreen = NowPlayingScreen.entries[viewPagerPosition]
            }
            .setNegativeButton(android.R.string.cancel, null)
            .create()
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}
    override fun onPageSelected(position: Int) {
        viewPagerPosition = position
    }

    override fun onPageScrollStateChanged(state: Int) {}

    private class NowPlayingScreenAdapter(private val context: Context?) : PagerAdapter() {

        override fun instantiateItem(collection: ViewGroup, position: Int): Any {
            val nowPlayingScreen = NowPlayingScreen.entries[position]
            val inflater = LayoutInflater.from(context)

            val binding = PreferenceNowPlayingScreenItemBinding.inflate(inflater)
            collection.addView(binding.root)

            binding.image.setImageResource(nowPlayingScreen.drawableResId)
            return binding.root
        }

        override fun destroyItem(collection: ViewGroup, position: Int, view: Any) {
            collection.removeView(view as View)
        }

        override fun getCount(): Int {
            return NowPlayingScreen.entries.size
        }

        override fun isViewFromObject(view: View, `object`: Any): Boolean {
            return view === `object`
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return context?.getString(NowPlayingScreen.entries[position].titleRes)
        }
    }
}