
package com.mardous.booming.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mardous.booming.R
import com.mardous.booming.interfaces.IStorageDeviceCallback
import com.mardous.booming.model.StorageDevice

class StorageAdapter(
    val storageList: List<StorageDevice>,
    val storageClickListener: IStorageDeviceCallback
) : RecyclerView.Adapter<StorageAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_storage, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindData(storageList[position])
    }

    override fun getItemCount(): Int {
        return storageList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val icon: ImageView = itemView.findViewById(R.id.icon)
        val title: TextView = itemView.findViewById(R.id.title)

        fun bindData(storage: StorageDevice) {
            title.text = storage.name
            icon.setImageResource(storage.iconRes)
        }

        init {
            itemView.setOnClickListener {
                storageClickListener.storageDeviceClick(storageList[bindingAdapterPosition])
            }
        }
    }
}