
package com.mardous.booming.fragments.base

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.mardous.booming.R
import com.mardous.booming.extensions.dip
import com.mardous.booming.extensions.isLandscape
import com.mardous.booming.model.GridViewType

abstract class AbsRecyclerViewCustomGridSizeFragment<Adt : RecyclerView.Adapter<*>, LM : RecyclerView.LayoutManager> :
    AbsRecyclerViewFragment<Adt, LM>() {

    protected var gridSize: Int
        get() = getSavedGridSize()
        set(newGridSize) {
            val oldLayoutRes = itemLayoutRes
            saveGridSize(newGridSize)
            // only recreate the adapter and layout manager if the layout currentLayoutRes has changed
            if (oldLayoutRes != itemLayoutRes) {
                invalidateLayoutManager()
                invalidateAdapter()
            } else {
                onGridSizeChanged(isLandscape, newGridSize)
            }
        }

    protected var viewType: GridViewType
        get() = getSavedViewType()
        set(newViewType) {
            saveViewType(newViewType)
            invalidateAdapter()
        }

    protected val isGridMode: Boolean
        get() = gridSize > maxGridSizeForList

    protected open val maxGridSize: Int
        get() = if (isLandscape) {
            resources.getInteger(R.integer.max_columns_land)
        } else resources.getInteger(R.integer.max_columns)

    protected open val maxGridSizeForList: Int
        get() = if (isLandscape) {
            resources.getInteger(R.integer.default_list_columns_land)
        } else resources.getInteger(R.integer.default_list_columns)

    protected open val defaultGridSize: Int
        get() = if (isLandscape) resources.getInteger(R.integer.default_grid_columns_land)
        else resources.getInteger(R.integer.default_grid_columns)

    private var currentLayoutRes = 0

    @get:LayoutRes
    protected open val itemLayoutRes: Int
        get() = if (isGridMode) {
            viewType.layoutRes
        } else R.layout.item_list

    protected val isLandscape: Boolean
        get() = resources.isLandscape

    protected fun notifyLayoutResChanged(@LayoutRes res: Int) {
        currentLayoutRes = res
        applyRecyclerViewPaddingForLayoutRes(recyclerView, currentLayoutRes)
    }

    private fun applyRecyclerViewPaddingForLayoutRes(recyclerView: RecyclerView, @LayoutRes itemLayoutRes: Int) {
        val padding = GridViewType.getMarginForLayout(itemLayoutRes)
        recyclerView.setPadding(padding, padding, padding, padding + dip(R.dimen.mini_player_height))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        applyRecyclerViewPaddingForLayoutRes(recyclerView, currentLayoutRes)
    }

    override fun onCreateMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateMenu(menu, inflater)
        menu.findItem(R.id.action_view_type).run {
            this.isEnabled = isGridMode
            this.subMenu?.findItem(viewType.itemId)?.isChecked = true
        }
        menu.findItem(R.id.action_grid_size).run {
            if (resources.isLandscape) {
                this.setTitle(R.string.action_grid_size_land)
            }
            this.subMenu?.let { setUpGridSizeMenu(it) }
        }
    }

    override fun onMenuItemSelected(item: MenuItem): Boolean {
        val selectedViewType = GridViewType.entries.firstOrNull { type -> type.itemId == item.itemId }
        if (selectedViewType != null) {
            item.isChecked = !item.isChecked
            this.viewType = selectedViewType
            return true
        }
        if (handleGridSizeMenuItem(item)) {
            return true
        }
        return super.onMenuItemSelected(item)
    }

    protected abstract fun getSavedViewType(): GridViewType
    protected abstract fun saveViewType(viewType: GridViewType)
    protected abstract fun getSavedGridSize(): Int
    protected abstract fun saveGridSize(newGridSize: Int)
    protected abstract fun onGridSizeChanged(isLand: Boolean, gridColumns: Int)

    private fun setUpGridSizeMenu(gridSizeMenu: Menu) {
        when (this.gridSize) {
            1 -> gridSizeMenu.findItem(R.id.action_grid_size_1).isChecked = true
            2 -> gridSizeMenu.findItem(R.id.action_grid_size_2).isChecked = true
            3 -> gridSizeMenu.findItem(R.id.action_grid_size_3).isChecked = true
            4 -> gridSizeMenu.findItem(R.id.action_grid_size_4).isChecked = true
            5 -> gridSizeMenu.findItem(R.id.action_grid_size_5).isChecked = true
            6 -> gridSizeMenu.findItem(R.id.action_grid_size_6).isChecked = true
            7 -> gridSizeMenu.findItem(R.id.action_grid_size_7).isChecked = true
            8 -> gridSizeMenu.findItem(R.id.action_grid_size_8).isChecked = true
        }
        val maxGridSize = this.maxGridSize
        if (maxGridSize < 8) {
            gridSizeMenu.findItem(R.id.action_grid_size_8).isVisible = false
        }
        if (maxGridSize < 7) {
            gridSizeMenu.findItem(R.id.action_grid_size_7).isVisible = false
        }
        if (maxGridSize < 6) {
            gridSizeMenu.findItem(R.id.action_grid_size_6).isVisible = false
        }
        if (maxGridSize < 5) {
            gridSizeMenu.findItem(R.id.action_grid_size_5).isVisible = false
        }
        if (maxGridSize < 4) {
            gridSizeMenu.findItem(R.id.action_grid_size_4).isVisible = false
        }
        if (maxGridSize < 3) {
            gridSizeMenu.findItem(R.id.action_grid_size_3).isVisible = false
        }
    }

    private fun handleGridSizeMenuItem(item: MenuItem): Boolean {
        var gridSize = 0
        when (item.itemId) {
            R.id.action_grid_size_1 -> gridSize = 1
            R.id.action_grid_size_2 -> gridSize = 2
            R.id.action_grid_size_3 -> gridSize = 3
            R.id.action_grid_size_4 -> gridSize = 4
            R.id.action_grid_size_5 -> gridSize = 5
            R.id.action_grid_size_6 -> gridSize = 6
            R.id.action_grid_size_7 -> gridSize = 7
            R.id.action_grid_size_8 -> gridSize = 8
        }
        if (gridSize > 0) {
            item.isChecked = true
            this.gridSize = gridSize
            this.toolbar.menu
                .findItem(R.id.action_view_type)
                ?.isEnabled = this.isGridMode
            return true
        }
        return false
    }
}