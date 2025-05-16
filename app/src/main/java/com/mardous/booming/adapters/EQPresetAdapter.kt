
package com.mardous.booming.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.mardous.booming.R
import com.mardous.booming.interfaces.IEQPresetCallback
import com.mardous.booming.model.EQPreset
import kotlin.properties.Delegates
import kotlin.reflect.KProperty

class EQPresetAdapter(val context: Context, presets: List<EQPreset>, val callback: IEQPresetCallback) :
    RecyclerView.Adapter<EQPresetAdapter.ViewHolder>() {

    var presets: List<EQPreset> by Delegates.observable(presets) { _: KProperty<*>, _: List<EQPreset>, _: List<EQPreset> ->
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return LayoutInflater.from(context).inflate(R.layout.item_equalizer_preset, parent, false).let { itemView ->
            ViewHolder(itemView)
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val preset = presets[position]

        holder.title?.text = preset.getName(context)
        holder.edit?.isVisible = !preset.isCustom
        holder.delete?.isVisible = !preset.isCustom
    }

    override fun getItemCount(): Int {
        return presets.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        val title: TextView? = itemView.findViewById(R.id.title)
        val edit: Button? = itemView.findViewById(R.id.edit)
        val delete: Button? = itemView.findViewById(R.id.delete)

        init {
            itemView.setOnClickListener(this)

            edit?.setOnClickListener(this)
            delete?.setOnClickListener(this)
        }

        private val preset: EQPreset
            get() = presets[bindingAdapterPosition]

        override fun onClick(view: View?) {
            when (view) {
                itemView -> callback.eqPresetSelected(preset)
                edit -> callback.editEQPreset(preset)
                delete -> callback.deleteEQPreset(preset)
            }
        }
    }
}