
package com.mardous.booming.adapters.preference

import com.mardous.booming.adapters.DraggableItemAdapter
import com.mardous.booming.model.NowPlayingInfo

class ExtraInfoAdapter(extraInfo: MutableList<NowPlayingInfo>) : DraggableItemAdapter<NowPlayingInfo>(extraInfo) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        val extraInfo = items[position]

        holder.title.text = holder.title.resources.getString(extraInfo.info.displayNameRes)
        holder.checkBox.isChecked = extraInfo.isEnabled

        holder.itemView.setOnClickListener {
            extraInfo.isEnabled = !extraInfo.isEnabled
            holder.checkBox.isChecked = extraInfo.isEnabled
        }
    }
}