
package com.mardous.booming.adapters.extension

import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.mardous.booming.adapters.base.MediaEntryViewHolder
import com.mardous.booming.extensions.resources.toColorStateList
import com.mardous.booming.helper.color.MediaNotificationProcessor

fun MediaEntryViewHolder.setColors(colors: MediaNotificationProcessor) {
    if (paletteColorContainer != null) {
        if (paletteColorContainer is CardView) {
            paletteColorContainer.setCardBackgroundColor(colors.backgroundColor)
        } else {
            paletteColorContainer.setBackgroundColor(colors.backgroundColor)
        }
        imageGradient?.backgroundTintList = colors.backgroundColor.toColorStateList()
    } else return

    title?.setTextColor(colors.primaryTextColor)
    text?.setTextColor(colors.secondaryTextColor)
    imageText?.setTextColor(colors.secondaryTextColor)
    menu?.iconTint = colors.secondaryTextColor.toColorStateList()
}

val RecyclerView.ViewHolder.isValidPosition: Boolean
    get() = layoutPosition > -1

val RecyclerView.Adapter<*>?.isNullOrEmpty: Boolean
    get() = this == null || isEmpty

val RecyclerView.Adapter<*>.isEmpty: Boolean
    get() = itemCount == 0

var MediaEntryViewHolder.isActivated: Boolean
    get() = if (itemView is MaterialCardView) (itemView as MaterialCardView).isChecked else itemView.isActivated
    set(activated) {
        if (itemView is MaterialCardView) {
            (itemView as MaterialCardView).apply {
                isCheckable = true
                isChecked = activated
            }
        } else {
            itemView.isActivated = activated
        }
    }