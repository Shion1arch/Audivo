
package com.mardous.booming.fragments.player.styles.defaultstyle

import android.os.Bundle
import android.view.Menu
import android.view.View
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsCompat.Type
import androidx.core.view.marginTop
import androidx.core.view.updatePadding
import com.mardous.booming.R
import com.mardous.booming.databinding.FragmentDefaultPlayerBinding
import com.mardous.booming.extensions.dp
import com.mardous.booming.extensions.whichFragment
import com.mardous.booming.fragments.player.base.AbsPlayerControlsFragment
import com.mardous.booming.fragments.player.base.AbsPlayerFragment
import com.mardous.booming.model.NowPlayingAction

class DefaultPlayerFragment : AbsPlayerFragment(R.layout.fragment_default_player) {

    private var _binding: FragmentDefaultPlayerBinding? = null
    private val binding get() = _binding!!

    private lateinit var controlsFragment: DefaultPlayerControlsFragment

    override val playerControlsFragment: AbsPlayerControlsFragment
        get() = controlsFragment

    override val playerToolbar: Toolbar
        get() = binding.toolbar

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentDefaultPlayerBinding.bind(view)
        setupToolbar()
        inflateMenuInView(playerToolbar)
        if (binding.albumCoverFragmentContainer != null) {
            val coverContainer = binding.albumCoverFragmentContainer!!
            coverContainer.viewTreeObserver.addOnGlobalLayoutListener(object : OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    val availablePanelHeight = view.height - coverContainer.height + coverContainer.marginTop
                    val minPanelHeight = (64 + 56 + 32 + 16 + (48 * 2)).dp(resources)
                    if (availablePanelHeight < minPanelHeight) {
                        coverContainer.layoutParams.height = coverContainer.height - (minPanelHeight - availablePanelHeight)
                        coverContainer.forceSquare(false)
                    }
                    view.viewTreeObserver.removeOnGlobalLayoutListener(this)
                }
            })
        }
        ViewCompat.setOnApplyWindowInsetsListener(view) { v: View, insets: WindowInsetsCompat ->
            val systemBars = insets.getInsets(Type.systemBars())
            v.updatePadding(top = systemBars.top, bottom = systemBars.bottom)
            val displayCutout = insets.getInsets(Type.displayCutout())
            v.updatePadding(left = displayCutout.left, right = displayCutout.right)
            WindowInsetsCompat.CONSUMED
        }
    }

    private fun setupToolbar() {
        playerToolbar.setNavigationOnClickListener {
            onQuickActionEvent(NowPlayingAction.SoundSettings)
        }
    }

    override fun onMenuInflated(menu: Menu) {
        super.onMenuInflated(menu)
        menu.removeItem(R.id.action_sound_settings)
    }

    override fun onCreateChildFragments() {
        super.onCreateChildFragments()
        controlsFragment = whichFragment(R.id.playbackControlsFragment)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}