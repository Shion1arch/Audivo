
package com.mardous.booming.model

import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.annotation.Px
import com.mardous.booming.R

enum class GridViewType(@IdRes val itemId: Int, @LayoutRes val layoutRes: Int, val margin: Int = 4) {
    Normal(R.id.action_view_type_normal, R.layout.item_grid),
    Card(R.id.action_view_type_card, R.layout.item_card),
    ColoredCard(R.id.action_view_type_colored_card, R.layout.item_card_color),
    Circle(R.id.action_view_type_circle, R.layout.item_grid_circle),
    Image(R.id.action_view_type_image, R.layout.item_image_gradient, 0);

    companion object {
        @Px
        fun getMarginForLayout(@LayoutRes layoutRes: Int): Int {
            return entries.firstOrNull { type -> type.layoutRes == layoutRes }?.margin ?: 0
        }
    }
}