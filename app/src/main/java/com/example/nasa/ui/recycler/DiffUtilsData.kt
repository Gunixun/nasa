package com.example.nasa.ui.recycler

import androidx.recyclerview.widget.DiffUtil

class DiffUtilsData (
    private val newData: List<Pair<Data, Boolean>>,
    private val oldData: List<Pair<Data, Boolean>>
) :
    DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return oldData.size
    }

    override fun getNewListSize(): Int {
        return newData.size
    }

    override fun areItemsTheSame(oldPos: Int, newPos: Int): Boolean {
        return newData[newPos].first.id == oldData[oldPos].first.id
    }

    override fun areContentsTheSame(oldPos: Int, newPos: Int): Boolean {
        val oldItem = oldData[newPos].first
        val newItem = newData[newPos].first
        return oldItem.name == newItem.name
                && oldItem.description == newItem.description
                && oldItem.type == newItem.type
    }


}