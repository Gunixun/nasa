package com.example.nasa.ui.recycler.diffUtils

import androidx.recyclerview.widget.DiffUtil
import com.example.nasa.ui.recycler.Data

class DiffUtilsCallback (
    private val newItems: List<Pair<Data, Boolean>>,
    private val oldItems: List<Pair<Data, Boolean>>
) :
    DiffUtil.Callback() {

    override fun getOldListSize() = oldItems.size

    override fun getNewListSize() = newItems.size

    override fun areItemsTheSame(oldPos: Int, newPos: Int): Boolean {
        return newItems[newPos].first.id == oldItems[oldPos].first.id
    }

    override fun areContentsTheSame(oldPos: Int, newPos: Int): Boolean {
        val oldItem = oldItems[newPos].first
        val newItem = newItems[newPos].first
        return oldItem.name == newItem.name
                && oldItem.description == newItem.description
                && oldItem.type == newItem.type
    }

    override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
        val oldItem = oldItems[oldItemPosition]
        val newItem = newItems[newItemPosition]
        return Change(oldItem, newItem)
    }

}