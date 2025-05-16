
package com.mardous.booming.adapters.base

import android.view.ActionMode
import android.view.Menu
import android.view.MenuItem
import androidx.activity.OnBackPressedCallback
import androidx.annotation.MenuRes
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.mardous.booming.R

abstract class AbsMultiSelectAdapter<VH : RecyclerView.ViewHolder, I>(
    private val activity: FragmentActivity, @MenuRes protected var menuRes: Int
) : RecyclerView.Adapter<VH>(), ActionMode.Callback {

    var actionMode: ActionMode? = null
    private val checked: MutableList<I> = ArrayList()

    override fun onCreateActionMode(mode: ActionMode, menu: Menu): Boolean {
        val inflater = mode.menuInflater
        inflater?.inflate(menuRes, menu)
        return true
    }

    override fun onPrepareActionMode(mode: ActionMode, menu: Menu): Boolean {
        return false
    }

    override fun onActionItemClicked(mode: ActionMode, item: MenuItem): Boolean {
        if (item.itemId == R.id.action_multi_select_adapter_check_all) {
            checkAll()
        } else {
            onMultipleItemAction(item, ArrayList(checked))
            actionMode?.finish()
            clearChecked()
        }
        return true
    }

    override fun onDestroyActionMode(mode: ActionMode) {
        clearChecked()
        actionMode = null
        onBackPressedCallback.remove()
    }

    private fun checkAll() {
        if (actionMode != null) {
            checked.clear()
            for (i in 0 until itemCount) {
                val identifier = getIdentifier(i)
                if (identifier != null) {
                    checked.add(identifier)
                }
            }
            notifyDataSetChanged()
            updateCab()
        }
    }

    protected abstract fun getIdentifier(position: Int): I?

    protected open fun getName(item: I): String? {
        return item.toString()
    }

    protected fun isChecked(identifier: I): Boolean {
        return checked.contains(identifier)
    }

    protected val isInQuickSelectMode: Boolean
        get() = actionMode != null

    protected abstract fun onMultipleItemAction(menuItem: MenuItem, selection: List<I>)

    protected fun toggleChecked(position: Int): Boolean {
        val identifier = getIdentifier(position) ?: return false
        if (!checked.remove(identifier)) {
            checked.add(identifier)
        }
        notifyItemChanged(position)
        updateCab()
        return true
    }

    private fun clearChecked() {
        checked.clear()
        notifyDataSetChanged()
    }

    private fun updateCab() {
        if (actionMode == null) {
            actionMode = activity.startActionMode(this)
            activity.onBackPressedDispatcher.addCallback(onBackPressedCallback)
        }
        val size = checked.size
        when {
            size <= 0 -> actionMode?.finish()
            size == 1 -> actionMode?.title = getName(checked.single())
            else -> actionMode?.title = activity.getString(R.string.x_selected, size)
        }
    }

    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            if (actionMode != null) {
                actionMode?.finish()
                remove()
            }
        }
    }
}