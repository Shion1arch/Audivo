
package com.mardous.booming.util.cursor

import android.database.Cursor
import android.provider.MediaStore.Audio.AudioColumns
import com.mardous.booming.repository.RealSongRepository
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

object SortedCursorUtil : KoinComponent {

    fun makeSortedCursor(cursor: Cursor?, idColumn: Int): SortedLongCursor? {
        if (cursor != null && cursor.moveToFirst()) {
            // create the list of ids to select against
            val selection = StringBuilder()
            selection.append(AudioColumns._ID)
            selection.append(" IN (")

            // this tracks the order of the ids
            val order = LongArray(cursor.count)
            var id = cursor.getLong(idColumn)
            selection.append(id)
            order[cursor.position] = id
            while (cursor.moveToNext()) {
                selection.append(",")
                id = cursor.getLong(idColumn)
                order[cursor.position] = id
                selection.append(id)
            }
            selection.append(")")

            // get a list of songs with the data given the selection statement
            val songRepository = get<RealSongRepository>()
            val songCursor = songRepository.makeSongCursor(selection.toString(), null)
            if (songCursor != null) {
                // now return the wrapped cursor to handle sorting given order
                return SortedLongCursor(
                    songCursor,
                    order,
                    AudioColumns._ID
                )
            }
        }
        return null
    }
}