
package com.mardous.booming.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

@SuppressLint("NotifyDataSetChanged")
class SimpleItemAdapter<T>(
    private val context: Context,
    private val layoutRes: Int = android.R.layout.simple_list_item_1,
    private val textViewId: Int = android.R.id.text1,
    items: List<T> = listOf(),
    private val callback: Callback<T>
) : RecyclerView.Adapter<SimpleItemAdapter<T>.ViewHolder>() {

    var items: List<T> = items
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return LayoutInflater.from(context).inflate(layoutRes, parent, false).let {
            ViewHolder(it)
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (callback.bindData(holder.itemView, position, items[position]))
            return

        holder.textView.text = items[position].toString()
    }

    override fun getItemCount(): Int = items.size

    interface Callback<T> {
        fun bindData(itemView: View, position: Int, item: T) = false
        fun itemClick(itemView: View, position: Int, item: T)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        val textView: TextView = itemView.findViewById(textViewId)

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(view: View) {
            val position = layoutPosition
            callback.itemClick(view, position, items[position])
        }
    }
}