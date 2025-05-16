
package com.mardous.booming.adapters.preference

import android.annotation.SuppressLint
import android.content.Context
import com.mardous.booming.R
import com.mardous.booming.adapters.DraggableItemAdapter
import com.mardous.booming.extensions.showToast
import com.mardous.booming.model.CategoryInfo

class CategoryInfoAdapter(
    private val context: Context,
    categoryInfos: MutableList<CategoryInfo>
) : DraggableItemAdapter<CategoryInfo>(categoryInfos) {

    @SuppressLint("ClickableViewAccessibility")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        val categoryInfo = items[position]

        holder.title.text = holder.title.resources.getString(categoryInfo.category.titleRes)
        holder.checkBox.isChecked = categoryInfo.visible

        holder.itemView.setOnClickListener {
            if (!canCheckCategory(categoryInfo)) {
                context.showToast(R.string.you_cannot_select_more_than_five_categories)
                return@setOnClickListener
            }

            if (!(categoryInfo.visible && isLastCheckedCategory(categoryInfo))) {
                categoryInfo.visible = !categoryInfo.visible
                holder.checkBox.isChecked = categoryInfo.visible
            } else {
                context.showToast(R.string.you_have_to_select_at_least_one_category)
            }
        }
    }

    private fun canCheckCategory(categoryInfo: CategoryInfo): Boolean {
        return items.count { it.visible } < CategoryInfo.MAX_VISIBLE_CATEGORIES || categoryInfo.visible
    }

    private fun isLastCheckedCategory(categoryInfo: CategoryInfo): Boolean {
        if (categoryInfo.visible) {
            for (c in items) {
                if (c != categoryInfo && c.visible) return false
            }
        }
        return true
    }

}