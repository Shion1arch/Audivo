
package com.mardous.booming.providers

import android.content.ContentResolver
import android.database.Cursor
import android.net.Uri
import android.util.Log
import androidx.annotation.WorkerThread
import com.mardous.booming.repository.RealSongRepository.Companion.getAudioContentUri
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

class MediaQueryDispatcher(val uri: Uri = getAudioContentUri()) : KoinComponent {

    private var projection: Array<String>? = null
    private var selection: String? = null
    private var selectionArguments: Array<String>? = null
    private var sortOrder: String? = null

    fun withColumns(vararg projection: String): MediaQueryDispatcher =
        apply { this.projection = arrayOf(*projection) }

    fun setProjection(projection: Array<String>?): MediaQueryDispatcher =
        apply { this.projection = projection }

    fun setSelection(selection: String?): MediaQueryDispatcher =
        apply { this.selection = selection }

    fun setSelectionArguments(selectionArguments: Array<String>?): MediaQueryDispatcher =
        apply { this.selectionArguments = selectionArguments }

    fun setSortOrder(sortOrder: String?): MediaQueryDispatcher =
        apply { this.sortOrder = sortOrder }

    fun addSelection(selection: String?, mode: String = "AND"): MediaQueryDispatcher =
        apply {
            if (!selection.isNullOrBlank()) {
                if (this.selection.isNullOrEmpty()) {
                    this.selection = selection
                } else this.selection += " $mode $selection"
            }
        }

    fun addArguments(vararg newArguments: String): MediaQueryDispatcher =
        apply {
            if (newArguments.isNotEmpty()) {
                val currentSelectionArguments = this.selectionArguments
                if (currentSelectionArguments.isNullOrEmpty()) {
                    this.selectionArguments = arrayOf(*newArguments)
                } else this.selectionArguments = currentSelectionArguments + newArguments
            }
        }

    @WorkerThread
    fun dispatch(): Cursor? {
        try {
            return get<ContentResolver>().query(uri, projection, selection, selectionArguments, sortOrder)
        } catch (e: IllegalArgumentException) {
            Log.e("QueryDispatcher", "Couldn't dispatch media query", e)
        }
        return null
    }
}