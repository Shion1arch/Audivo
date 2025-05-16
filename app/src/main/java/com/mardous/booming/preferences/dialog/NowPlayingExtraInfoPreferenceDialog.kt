
package com.mardous.booming.preferences.dialog

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.core.os.BundleCompat
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.mardous.booming.R
import com.mardous.booming.adapters.preference.ExtraInfoAdapter
import com.mardous.booming.databinding.DialogRecyclerViewBinding
import com.mardous.booming.extensions.create
import com.mardous.booming.extensions.utilities.toMutableListIfRequired
import com.mardous.booming.model.NowPlayingInfo
import com.mardous.booming.util.Preferences

class NowPlayingExtraInfoPreferenceDialog : DialogFragment() {

    private lateinit var adapter: ExtraInfoAdapter

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        var extraInfoList = Preferences.nowPlayingExtraInfoList
        if (savedInstanceState != null && savedInstanceState.containsKey(SAVED_KEY)) {
            extraInfoList =
                BundleCompat.getParcelableArrayList(savedInstanceState, SAVED_KEY, NowPlayingInfo::class.java)!!
        }

        adapter = ExtraInfoAdapter(extraInfoList.toMutableList())

        val binding = DialogRecyclerViewBinding.inflate(layoutInflater)
        binding.recyclerView.layoutManager = LinearLayoutManager(activity)
        binding.recyclerView.adapter = adapter
        adapter.attachToRecyclerView(binding.recyclerView)

        return MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.select_extra_info_title)
            .setView(binding.root)
            .setPositiveButton(android.R.string.ok) { _: DialogInterface, _: Int ->
                Preferences.nowPlayingExtraInfoList = adapter.items
            }
            .setNeutralButton(R.string.reset_action, null)
            .create { dialog ->
                dialog.getButton(DialogInterface.BUTTON_NEUTRAL).setOnClickListener {
                    adapter.items = Preferences.getDefaultNowPlayingInfo().toMutableListIfRequired()
                }
            }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList(SAVED_KEY, ArrayList(adapter.items))
    }

    companion object {
        private const val SAVED_KEY = "SavedKey.list"
    }
}