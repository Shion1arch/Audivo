
package com.mardous.booming.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.mardous.booming.R
import com.mardous.booming.helper.SwipeAndDragHelper
import kotlin.properties.Delegates
import kotlin.reflect.KProperty

open class DraggableItemAdapter<T>(items: MutableList<T>) :
    RecyclerView.Adapter<DraggableItemAdapter.ViewHolder>(),
    SwipeAndDragHelper.ActionCompletionContract {

    var items by Delegates.observable(items) { _: KProperty<*>, _: MutableList<T>, _: MutableList<T> ->
        notifyDataSetChanged()
    }

    private val touchHelper: ItemTouchHelper = ItemTouchHelper(SwipeAndDragHelper(this))

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.preference_dialog_draggable_item, parent, false)
        return ViewHolder(view)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.dragView.setOnTouchListener { _: View?, event: MotionEvent ->
            if (event.actionMasked == MotionEvent.ACTION_DOWN) {
                touchHelper.startDrag(holder)
            }
            false
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onViewMoved(oldPosition: Int, newPosition: Int) {
        val categoryInfo = items[oldPosition]
        items.removeAt(oldPosition)
        items.add(newPosition, categoryInfo)
        notifyItemMoved(oldPosition, newPosition)
    }

    fun attachToRecyclerView(recyclerView: RecyclerView?) {
        touchHelper.attachToRecyclerView(recyclerView)
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val checkBox: CheckBox = view.findViewById(R.id.checkbox)
        val title: TextView = view.findViewById(R.id.title)
        val dragView: View = view.findViewById(R.id.drag_view)
    }
}

