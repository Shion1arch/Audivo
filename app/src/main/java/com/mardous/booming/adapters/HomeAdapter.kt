
package com.mardous.booming.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mardous.booming.R
import com.mardous.booming.interfaces.IHomeCallback
import com.mardous.booming.model.Suggestion
import kotlin.properties.Delegates
import kotlin.reflect.KProperty


@SuppressLint("NotifyDataSetChanged")
class HomeAdapter(
    private val activity: FragmentActivity,
    dataSet: List<Suggestion>,
    private val callback: IHomeCallback
) : RecyclerView.Adapter<HomeAdapter.ViewHolder>() {

    var dataSet: List<Suggestion> by Delegates.observable(dataSet) { _: KProperty<*>, _: List<Suggestion>, _: List<Suggestion> ->
        notifyDataSetChanged()
    }

    init {
        setHasStableIds(true)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(activity).inflate(R.layout.item_suggestion, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val suggestion = dataSet[position]
        holder.headingTitle?.setText(suggestion.type.titleRes)
        if (holder.recyclerView != null) {
            holder.recyclerView.layoutManager = LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false)
            holder.recyclerView.adapter = callback.createSuggestionAdapter(suggestion)
            holder.recyclerView.setItemViewCacheSize(0)
        }
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }

    override fun getItemId(position: Int): Long {
        return dataSet[position].hashCode().toLong()
    }

    inner class ViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        internal val headingTitle: TextView? = itemView.findViewById(R.id.heading_title)
        internal val recyclerView: RecyclerView? = itemView.findViewById(R.id.recycler_view)

        init {
            headingTitle?.setOnClickListener(this)
        }

        private val current: Suggestion?
            get() = dataSet.getOrNull(layoutPosition)

        override fun onClick(view: View) {
            if (view === headingTitle) {
                val suggestion = current ?: return
                callback.suggestionClick(suggestion)
            }
        }
    }
}