
package com.mardous.booming.dialogs

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.mardous.booming.R
import com.mardous.booming.databinding.DialogMarkdownBinding
import com.mardous.booming.extensions.readStringFromAsset
import com.mardous.booming.extensions.resources.setMarkdownText


class MarkdownDialog : DialogFragment() {
    private var _binding: DialogMarkdownBinding? = null
    private val binding get() = _binding!!

    private var title: String? = null
    private var markdownContent: String? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = DialogMarkdownBinding.inflate(layoutInflater)
        if (!markdownContent.isNullOrEmpty()) {
            binding.message.setMarkdownText(markdownContent!!)
        }
        return MaterialAlertDialogBuilder(requireContext())
            .setTitle(title)
            .setView(binding.root)
            .setPositiveButton(R.string.close_action, null)
            .create()
    }

    fun setTitle(title: String): MarkdownDialog =
        apply { this.title = title }

    fun setContentFromAsset(context: Context, assetName: String): MarkdownDialog =
        apply { this.markdownContent = context.readStringFromAsset(assetName) }

    fun setContentFromText(markdown: String): MarkdownDialog =
        apply { this.markdownContent = markdown }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}