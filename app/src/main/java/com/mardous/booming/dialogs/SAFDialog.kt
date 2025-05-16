
package com.mardous.booming.dialogs

import android.app.Dialog
import android.content.DialogInterface
import android.net.Uri
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.mardous.booming.R
import com.mardous.booming.extensions.create
import com.mardous.booming.extensions.files.saveTreeUri
import com.mardous.booming.util.Preferences

class SAFDialog : DialogFragment() {

    private lateinit var documentTreeLauncher: ActivityResultLauncher<Uri?>

    interface SAFResultListener {
        fun onSAFResult(treeUri: Uri?)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        documentTreeLauncher = registerForActivityResult(ActivityResultContracts.OpenDocumentTree()) { result ->
            val listener = listener
            if (result != null) {
                listener?.onSAFResult(context?.saveTreeUri(result))
            } else {
                listener?.onSAFResult(null)
            }
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return MaterialAlertDialogBuilder(requireActivity())
            .setTitle(R.string.saf_access_required_title)
            .setMessage(R.string.saf_access_required_message)
            .setPositiveButton(R.string.saf_show_files_button, null)
            .create { dialog ->
                dialog.getButton(DialogInterface.BUTTON_POSITIVE)?.setOnClickListener {
                    val sdcardUri = Preferences.sAFSDCardUri
                    if (!sdcardUri.isNullOrEmpty()) {
                        documentTreeLauncher.launch(Uri.parse(sdcardUri))
                    } else documentTreeLauncher.launch(null)
                }
            }
    }

    private val listener: SAFResultListener?
        get() = (parentFragment as? SAFResultListener) ?: (activity as? SAFResultListener)

    companion object {
        const val TAG = "SAFDialog"

        fun <T> show(activity: T) where T : AppCompatActivity, T : SAFResultListener? {
            SAFDialog().show(activity.supportFragmentManager, TAG)
        }

        fun <T> show(fragment: T) where T : Fragment, T : SAFResultListener? {
            SAFDialog().show(fragment.childFragmentManager, TAG)
        }
    }
}