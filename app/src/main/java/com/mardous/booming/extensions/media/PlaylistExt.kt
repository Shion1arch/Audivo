
package com.mardous.booming.extensions.media

import android.content.Context
import com.mardous.booming.R
import com.mardous.booming.database.PlaylistEntity

fun PlaylistEntity.isFavorites(context: Context) = playlistName == context.getString(R.string.favorites_label)