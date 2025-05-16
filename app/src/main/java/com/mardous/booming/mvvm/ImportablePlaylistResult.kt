
package com.mardous.booming.mvvm

import android.content.Context
import com.mardous.booming.R
import com.mardous.booming.model.Song

class ImportablePlaylistResult(val playlistName: String, val songs: List<Song>)

class ImportResult(val resultMessage: String) {
    companion object {
        fun success(ctx: Context, res: ImportablePlaylistResult): ImportResult {
            return ImportResult(ctx.getString(R.string.imported_playlist_x, res.playlistName))
        }

        fun error(ctx: Context, res: ImportablePlaylistResult): ImportResult {
            return ImportResult(ctx.getString(R.string.could_not_import_the_playlist_x, res.playlistName))
        }
    }
}