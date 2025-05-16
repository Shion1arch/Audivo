
package com.mardous.booming.fragments.genres

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.core.content.edit
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.mardous.booming.R
import com.mardous.booming.adapters.GenreAdapter
import com.mardous.booming.extensions.navigation.genreDetailArgs
import com.mardous.booming.fragments.ReloadType
import com.mardous.booming.fragments.base.AbsRecyclerViewCustomGridSizeFragment
import com.mardous.booming.interfaces.IGenreCallback
import com.mardous.booming.model.Genre
import com.mardous.booming.model.GridViewType
import com.mardous.booming.util.sort.SortOrder
import com.mardous.booming.util.sort.prepareSortOrder
import com.mardous.booming.util.sort.selectedSortOrder

class GenresListFragment : AbsRecyclerViewCustomGridSizeFragment<GenreAdapter, GridLayoutManager>(),
    IGenreCallback {

    override val titleRes: Int = R.string.genres_label
    override val isShuffleVisible: Boolean = false
    override val emptyMessageRes: Int = R.string.no_genres_label

    override val maxGridSize: Int
        get() = if (isLandscape) resources.getInteger(R.integer.max_genre_columns_land)
        else resources.getInteger(R.integer.max_genre_columns)

    override val itemLayoutRes: Int
        get() = if (isGridMode) R.layout.item_grid_gradient
        else R.layout.item_list

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        libraryViewModel.getGenres().observe(viewLifecycleOwner) { genres ->
            adapter?.dataSet = genres
        }
    }

    override fun onResume() {
        super.onResume()
        libraryViewModel.forceReload(ReloadType.Genres)
    }

    override fun createLayoutManager(): GridLayoutManager {
        return GridLayoutManager(requireActivity(), gridSize)
    }

    override fun createAdapter(): GenreAdapter {
        notifyLayoutResChanged(itemLayoutRes)
        val dataSet = adapter?.dataSet ?: ArrayList()
        return GenreAdapter(mainActivity, Glide.with(this), dataSet, itemLayoutRes, lifecycleScope, this)
    }

    override fun genreClick(genre: Genre) {
        findNavController().navigate(R.id.nav_genre_detail, genreDetailArgs(genre))
    }

    override fun onCreateMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateMenu(menu, inflater)
        menu.removeItem(R.id.action_view_type)
        val sortOrderSubmenu = menu.findItem(R.id.action_sort_order)?.subMenu
        if (sortOrderSubmenu != null) {
            sortOrderSubmenu.clear()
            sortOrderSubmenu.add(0, R.id.action_sort_order_az, 0, R.string.sort_order_az)
            sortOrderSubmenu.add(0, R.id.action_sort_order_number_of_songs, 1, R.string.sort_order_number_of_songs)
            sortOrderSubmenu.add(1, R.id.action_sort_order_descending, 3, R.string.sort_order_descending)
            sortOrderSubmenu.setGroupCheckable(0, true, true)
            sortOrderSubmenu.prepareSortOrder(SortOrder.genreSortOrder)
        }
    }

    override fun onMenuItemSelected(item: MenuItem): Boolean {
        if (item.selectedSortOrder(SortOrder.genreSortOrder)) {
            libraryViewModel.forceReload(ReloadType.Genres)
            return true
        }
        return super.onMenuItemSelected(item)
    }

    override fun onMediaStoreChanged() {
        super.onMediaStoreChanged()
        libraryViewModel.forceReload(ReloadType.Genres)
    }

    override fun getSavedViewType(): GridViewType {
        // this value is actually ignored by the implementation
        return GridViewType.Normal
    }

    override fun saveViewType(viewType: GridViewType) {}

    override fun getSavedGridSize(): Int {
        return sharedPreferences.getInt(GENRES_GRID_SIZE_KEY, defaultGridSize)
    }

    override fun saveGridSize(newGridSize: Int) {
        sharedPreferences.edit {
            putInt(GENRES_GRID_SIZE_KEY, newGridSize)
        }
    }

    override fun onGridSizeChanged(isLand: Boolean, gridColumns: Int) {
        layoutManager?.spanCount = gridColumns
        adapter?.notifyDataSetChanged()
    }

    companion object {
        private const val GENRES_GRID_SIZE_KEY = "genres_grid_size"
    }
}