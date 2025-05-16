
package com.mardous.booming.preferences.dialog

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import androidx.core.os.BundleCompat
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.mardous.booming.R
import com.mardous.booming.adapters.preference.CategoryInfoAdapter
import com.mardous.booming.databinding.DialogRecyclerViewBinding
import com.mardous.booming.extensions.create
import com.mardous.booming.extensions.utilities.toMutableListIfRequired
import com.mardous.booming.model.CategoryInfo
import com.mardous.booming.util.LIBRARY_CATEGORIES
import com.mardous.booming.util.Preferences

class CategoriesPreferenceDialog : DialogFragment() {

    private lateinit var adapter: CategoryInfoAdapter

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val binding = DialogRecyclerViewBinding.inflate(layoutInflater)

        var categoryInfos = Preferences.libraryCategories
        if (savedInstanceState != null && savedInstanceState.containsKey(LIBRARY_CATEGORIES)) {
            categoryInfos = BundleCompat.getParcelableArrayList(
                savedInstanceState, LIBRARY_CATEGORIES, CategoryInfo::class.java
            )!!
        }

        adapter = CategoryInfoAdapter(requireContext(), categoryInfos.toMutableList())

        binding.recyclerView.layoutManager = LinearLayoutManager(activity)
        binding.recyclerView.adapter = adapter
        adapter.attachToRecyclerView(binding.recyclerView)

        return MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.library_categories_title)
            .setView(binding.root)
            .setPositiveButton(android.R.string.ok) { _: DialogInterface, _: Int ->
                updateCategories(adapter.items)
            }
            .setNeutralButton(R.string.reset_action, null)
            .create { dialog ->
                dialog.getButton(DialogInterface.BUTTON_NEUTRAL).setOnClickListener {
                    adapter.items = Preferences.getDefaultLibraryCategoryInfos()
                        .toMutableListIfRequired()
                }
            }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList(LIBRARY_CATEGORIES, ArrayList(adapter.items))
    }

    private fun updateCategories(categories: List<CategoryInfo>) {
        if (categories.any { category -> category.visible }) {
            Preferences.libraryCategories = categories
        }
    }
}