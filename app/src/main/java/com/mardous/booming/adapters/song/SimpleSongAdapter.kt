
package com.mardous.booming.adapters.song

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import com.bumptech.glide.RequestManager
import com.mardous.booming.extensions.media.durationStr
import com.mardous.booming.extensions.media.trackNumber
import com.mardous.booming.interfaces.ISongCallback
import com.mardous.booming.model.Song
import com.mardous.booming.util.sort.SortOrder

class SimpleSongAdapter(
    context: FragmentActivity,
    requestManager: RequestManager,
    songs: List<Song>,
    layoutRes: Int,
    sortOrder: SortOrder,
    callback: ISongCallback
) : SongAdapter(context, requestManager, songs, layoutRes, sortOrder, callback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(activity).inflate(itemLayoutRes, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        val fixedTrackNumber = dataSet[position].trackNumber.trackNumber()

        holder.imageText?.text = if (fixedTrackNumber > 0) fixedTrackNumber.toString() else "-"
        holder.time?.text = dataSet[position].duration.durationStr()
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }
}